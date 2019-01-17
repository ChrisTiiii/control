package com.example.administrator.control;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.administrator.control.activity.LoginActivity;
import com.example.administrator.control.adapter.ComupterAdapter;
import com.example.administrator.control.bean.EqupmentBean;
import com.example.administrator.control.bean.SendCommand;
import com.example.administrator.control.bean.AcceptCommand;
import com.example.administrator.control.fragment.ControlFragment;
import com.example.administrator.control.tcp.ClientThread;
import com.example.administrator.control.util.MessageEvent;
import com.example.administrator.control.util.SharedPreferencesUtils;
import com.example.administrator.control.util.TimeUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @Author ZhongMing
 * @Date 2019/1/15 0015 上午 11:22
 * @Description:
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.computer_list)
    RecyclerView computerList;
    private ClientThread clientThread;
    private ComupterAdapter comupterAdapter;
    private List<EqupmentBean> list;
    private String account;
    private SharedPreferencesUtils helper;
    private int _position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        _position = -1;
        Intent intent = getIntent();
        if (intent != null)
            account = intent.getExtras().getString("account");
        initSocket();
        computerList.setLayoutManager(new LinearLayoutManager(this));
        comupterAdapter = new ComupterAdapter(this, list);
        computerList.setAdapter(comupterAdapter);
        setClick();
    }

    //用于接收到的服务端的消息，显示在界面上
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setData(MessageEvent messageEvent) {
        switch (messageEvent.getTAG()) {
            case MyApp.ACCEPPT:
                if (messageEvent.getMessage() != null) {
                    System.out.println("accept:" + messageEvent.getMessage());
                    Gson gson = new Gson();
                    try {
                        Object obj = gson.fromJson(messageEvent.getMessage(), AcceptCommand.class);
                        //获取列表数据
                        if (((AcceptCommand) obj).getType().equals("userlist")) {
                            list.clear();
                            list.add(new EqupmentBean("all", 1));
                            for (String str : (List<String>) ((AcceptCommand) obj).getMsg()) {
                                list.add(new EqupmentBean(str, 1));
                            }
                            removeDuplicateWithOrder(list);
                            comupterAdapter.notifyDataSetChanged();
                            if (list.size() > 0) {
                                initRight();
                            }
                        }
                        //判断设备是否在线
                        if (((AcceptCommand) obj).getType().equals("Connect"))
                            if (((AcceptCommand) obj).getStatus().equals("error4")) {
                                list.get(_position).setStatus(-1);
                                Toast.makeText(this, "设备：" + list.get(_position).getName() + "不在线", Toast.LENGTH_SHORT).show();
                            } else if (((AcceptCommand) obj).getStatus().equals("Success")) {
                                list.get(_position).setStatus(1);
                                Toast.makeText(this, "设备：" + list.get(_position).getName() + "在线", Toast.LENGTH_SHORT).show();
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void setClick() {
        comupterAdapter.setOnItemClickListener(new ComupterAdapter.ItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                comupterAdapter.setPosition(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        _position = position;
                        SendCommand connect = new SendCommand(account, list.get(position).getName(), TimeUtil.nowTime(), "Connect", "client", null, "client");
                        clientThread.sendData(new Gson().toJson(connect));
                        try {
                            Thread.sleep(100);
                            getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, new ControlFragment(account, list.get(position), clientThread)).commit();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void initSocket() {
        clientThread = new ClientThread(account);
        new Thread(clientThread).start();
        initLeft();
    }

    public void initLeft() {
        list = new ArrayList<>();
    }

    private void initRight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SendCommand connect = new SendCommand(account, list.get(0).getName(), TimeUtil.nowTime(), "Connect", "client", null, "client");
                clientThread.sendData(new Gson().toJson(connect));
            }
        }).start();
        getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, new ControlFragment(account, list.get(0), clientThread)).commit();
    }

    private void logOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SendCommand connect = new SendCommand(account, "server", TimeUtil.nowTime(), "Logout", "client", null, "client logout");
                clientThread.sendData(new Gson().toJson(connect));
            }
        }).start();
    }

    // 删除ArrayList中重复元素，保持顺序
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_normal_1:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                logOut();
                //获取SharedPreferences对象，使用自定义类的方法来获取对象
                helper = new SharedPreferencesUtils(this, "setting");
                //创建一个ContentVa对象（自定义的）
                helper.putValues(new SharedPreferencesUtils.ContentValue("name", ""));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
