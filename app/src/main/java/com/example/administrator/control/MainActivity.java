package com.example.administrator.control;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.control.activity.LoginActivity;
import com.example.administrator.control.adapter.ComupterAdapter;
import com.example.administrator.control.bean.AcceptCommand;
import com.example.administrator.control.bean.EqupmentBean;
import com.example.administrator.control.bean.SendCommand;
import com.example.administrator.control.fragment.ControlFragment;
import com.example.administrator.control.tcp.ClientThread;
import com.example.administrator.control.tcp.ControlThread;
import com.example.administrator.control.util.MessageEvent;
import com.example.administrator.control.util.NetWorkUtil;
import com.example.administrator.control.util.SharedPreferencesUtils;
import com.example.administrator.control.util.TimeUtil;
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
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * @Author ZhongMing
 * @Date 2019/1/15 0015 上午 11:22
 * @Description: 应用主界面
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.computer_list)
    RecyclerView computerList;

    @BindView(R.id.menu)
    LinearLayout menu;
    @BindView(R.id.menu1)
    Button menu1;


    private ClientThread clientThread;
    private ControlThread controlThread;
    private ComupterAdapter comupterAdapter;
    private List<EqupmentBean> list;
    private String account;
    private SharedPreferencesUtils helper;
    private int _position;
    private long exitTime = 0;

    final String[] items = new String[]{"重新连接", "有效期", "退出登录"};//创建item


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
//        computerList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        computerList.setLayoutManager(new LinearLayoutManager(this));
        comupterAdapter = new ComupterAdapter(this, list);
        computerList.setAdapter(comupterAdapter);
        setClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper = new SharedPreferencesUtils(this, "setting");
        //创建一个ContentVa对象（自定义的）
        String begin = helper.getString("begin");
        String end = helper.getString("end");
        String def = TimeUtil.getDef(TimeUtil.nowTime(), end);
        String deft = TimeUtil.getDef(begin, TimeUtil.nowTime());
        if (!(Integer.parseInt(def) > 0) || !(Integer.parseInt(deft) >= 0)) {
            new SweetAlertDialog(this)
                    .setTitleText("用户须知")
                    .setContentText("您的软件还没激活授权")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            //获取SharedPreferences对象，使用自定义类的方法来获取对象
                            helper = new SharedPreferencesUtils(MainActivity.this, "setting");
                            //创建一个ContentVa对象（自定义的）
                            helper.putValues(new SharedPreferencesUtils.ContentValue("name", ""));
                            helper.putValues(new SharedPreferencesUtils.ContentValue("isVal", false));
                            finish();
                        }
                    })
                    .show();
        }
    }

    /**
     * 用于接收到的服务端的消息，更新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setData(MessageEvent messageEvent) {
        switch (messageEvent.getTAG()) {
            case MyApp.ERROR:
                reConnect();
                break;
            case MyApp.ACCEPPT:
                if (messageEvent.getMessage() != null) {
                    System.out.println("accept:" + messageEvent.getMessage());
                    Gson gson = new Gson();
                    try {
                        Object obj = gson.fromJson(messageEvent.getMessage(), AcceptCommand.class);
                        //获取列表数据
                        if (((AcceptCommand) obj).getType().equals("userlist")) {
                            list.clear();
                            list.add(new EqupmentBean("全部设备", 0));
                            for (String str : (List<String>) ((AcceptCommand) obj).getMsg()) {
                                list.add(new EqupmentBean(str, -1));
                            }
                            removeDuplicateWithOrder(list);
                            comupterAdapter.update(list);
                            if (list.size() > 0) {
                                initRight();
                            }
                        }
                        //判断设备是否在线
                        if (((AcceptCommand) obj).getType().equals("Connect")) {
                            switch (((AcceptCommand) obj).getStatus()) {
                                case "error4":
                                    list.get(_position).setStatus(-1);
                                    Toast.makeText(this, "设备：" + list.get(_position).getName() + "不在线", Toast.LENGTH_SHORT).show();
                                    comupterAdapter.update(list);
                                    break;
                                case "Success":
                                    list.get(_position).setStatus(1);
                                    Toast.makeText(this, "设备：" + list.get(_position).getName() + "在线", Toast.LENGTH_SHORT).show();
                                    comupterAdapter.update(list);
                                    break;
                            }
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
                        if (!list.get(position).getName().equals("全部设备")) {
                            SendCommand connect = new SendCommand(account, list.get(position).getName(), TimeUtil.nowTime(), "Connect", "client", null, "client");
                            clientThread.sendData(new Gson().toJson(connect));
                        }
                        try {
                            Thread.sleep(100);
                            getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, new ControlFragment(account, list.get(position), clientThread, controlThread)).commit();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }


    /**
     * 初始化sockect
     */
    private void initSocket() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            initLeft();
            clientThread = new ClientThread(account);
            new Thread(clientThread).start();
            controlThread = new ControlThread();
            new Thread(controlThread).start();
        } else
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
    }

    public void initLeft() {
        list = new ArrayList<>();
    }


    /**
     * 初始化右侧fragmnet
     */
    private void initRight() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, new ControlFragment(account, list.get(0), clientThread, controlThread)).commit();
        } else
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
    }


    /**
     * 登出操作
     */
    private void logOut() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendCommand connect = new SendCommand(account, "server", TimeUtil.nowTime(), "Logout", "client", null, "client logout");
                    clientThread.sendData(new Gson().toJson(connect));
                }
            }).start();
        } else
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
    }

    // 删除ArrayList中重复元素，保持顺序
    public void removeDuplicateWithOrder(List list) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }


    /**
     * 菜单点击
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_login_lout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                logOut();
                //获取SharedPreferences对象，使用自定义类的方法来获取对象
                helper = new SharedPreferencesUtils(this, "setting");
                //创建一个ContentVa对象（自定义的）
                helper.putValues(new SharedPreferencesUtils.ContentValue("name", ""));
                clientThread.destorySocket();
                controlThread.destorySocket();
                finish();
                break;
            case R.id.option_date:
                //获取SharedPreferences对象，使用自定义类的方法来获取对象
                helper = new SharedPreferencesUtils(this, "setting");
                //创建一个ContentVa对象（自定义的）
                String begin = helper.getString("begin");
                String end = helper.getString("end");
                String def = TimeUtil.getDef(TimeUtil.nowTime(), end);
                String deft = TimeUtil.getDef(begin, TimeUtil.nowTime());
                if (Integer.parseInt(def) > 0 && Integer.parseInt(deft) >= 0)
                    new SweetAlertDialog(this)
                            .setTitleText("激活详情")
                            .setContentText("起始时间:" + begin + "\n截止时间:" + end + "\n剩余有效期:" + def + "天")
                            .show();
                else Toast.makeText(this, "激活码已到期", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_connect:
                reConnect();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 重新连接
     */
    public void reConnect() {
        try {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this).setTitleText("即将断开连接")
                    .setContentText("尝试重新连接")
                    .setCancelText("取消")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmText("确认")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, new ControlFragment()).commit();
                            clientThread.destorySocket();
                            controlThread.destorySocket();
                            list.clear();
                            comupterAdapter.update(list);
                            initSocket();
                            try {
                                Thread.sleep(100);
                                sweetAlertDialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        clientThread.destorySocket();
        controlThread.destorySocket();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.menu, R.id.menu1})
    public void onViewClicked() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("工具栏")
                .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0://重新连接
                                reConnect();
                                break;
                            case 1://有效期
                                //获取SharedPreferences对象，使用自定义类的方法来获取对象
                                helper = new SharedPreferencesUtils(MainActivity.this, "setting");
                                //创建一个ContentVa对象（自定义的）
                                String begin = helper.getString("begin");
                                String end = helper.getString("end");
                                String def = TimeUtil.getDef(TimeUtil.nowTime(), end);
                                String deft = TimeUtil.getDef(begin, TimeUtil.nowTime());
                                if (Integer.parseInt(def) > 0 && Integer.parseInt(deft) >= 0)
                                    new SweetAlertDialog(MainActivity.this)
                                            .setTitleText("激活详情")
                                            .setContentText("起始时间:" + begin + "\n截止时间:" + end + "\n剩余有效期:" + def + "天")
                                            .show();
                                else
                                    Toast.makeText(MainActivity.this, "激活码已到期", Toast.LENGTH_SHORT).show();
                                break;
                            case 2://退出登录
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                logOut();
                                //获取SharedPreferences对象，使用自定义类的方法来获取对象
                                helper = new SharedPreferencesUtils(MainActivity.this, "setting");
                                //创建一个ContentVa对象（自定义的）
                                helper.putValues(new SharedPreferencesUtils.ContentValue("name", ""));
                                clientThread.destorySocket();
                                controlThread.destorySocket();
                                finish();
                                break;
                        }
                    }
                }).create();
        alertDialog.show();
    }
}
