package com.example.administrator.control.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.example.administrator.control.R;
import com.example.administrator.control.bean.ComputerBean;
import com.example.administrator.control.tcp.ClientThread;
import com.example.administrator.control.util.MessageEvent;
import com.example.administrator.control.util.TimeUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: ZhongMing
 * DATE: 2019/1/11 0011
 * Description:
 **/
public class ComputerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gridview)
    GridView gridview;

    private ClientThread clientThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_computer);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initView();
        initSocket();
    }

    private void initSocket() {
        clientThread = new ClientThread("123");
        new Thread(clientThread).start();
    }


    private void initView() {
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.computer_item, new String[]{"text", "image"}, new int[]{R.id.tv_computerid, R.id.img_computer});
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("text", "ID:" + "00" + i);
            map.put("image", R.drawable.computer);
            dataList.add(map);
        }
        return dataList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ComputerBean computerBean = new ComputerBean("111111", "server", TimeUtil.nowTime(), "getId", "test", "SDJAGHSJFKASH" + position, "test");
        Message msg = new Message();
        msg.what = 0;
        msg.obj = new Gson().toJson(computerBean);
        clientThread.revHandler.sendMessage(msg);
    }


//    //用于接收到的服务端的消息，显示在界面上
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void setData(MessageEvent messageEvent) {
//        switch (messageEvent.getTAG()) {
//            case MyApp.ACCEPPT:
//                if (messageEvent.getMessage() != null) {
//                    Toast.makeText(ComputerActivity.this, messageEvent.getMessage(), Toast.LENGTH_SHORT).show();
//                    System.out.println(messageEvent.getMessage());
//                }
//                break;
//        }
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
