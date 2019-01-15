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


import com.example.administrator.control.activity.LoginActivity;
import com.example.administrator.control.adapter.ComupterAdapter;
import com.example.administrator.control.bean.Connect;
import com.example.administrator.control.bean.ComputerListBean;
import com.example.administrator.control.fragment.ControlFragment;
import com.example.administrator.control.tcp.ClientThread;
import com.example.administrator.control.util.MessageEvent;
import com.example.administrator.control.util.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    private List<String> list;
    private String account;
    private SharedPreferencesUtils helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
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
                    System.out.println(messageEvent.getMessage());
                    Gson gson = new Gson();
                    Object obj = gson.fromJson(messageEvent.getMessage(), ComputerListBean.class);
                    for (String str : ((ComputerListBean) obj).getMsg()) {
                        list.add(str);
                    }
                    removeDuplicateWithOrder(list);
                    comupterAdapter.notifyDataSetChanged();
                    if (list.size() > 0) {
                        initRight();
                    }
                }
                break;
        }
    }

    private void setClick() {
        comupterAdapter.setOnItemClickListener(new ComupterAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Connect connect = new Connect(account, "Connect", list.get(position), "client");
                clientThread.sendData(new Gson().toJson(connect));
                getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, new ControlFragment(account, list.get(position), clientThread)).commit();
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
        Connect connect = new Connect(account, "Connect", list.get(0), "client");
        clientThread.sendData(new Gson().toJson(connect));
        getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, new ControlFragment(account, list.get(0), clientThread)).commit();
    }

    private void logOut() {
        Connect connect = new Connect(account, "Logout", "server", "client logout");
        clientThread.sendData(new Gson().toJson(connect));
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
        System.out.println(" remove duplicate " + list);
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
//                clientThread.destorySocket();
//                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
