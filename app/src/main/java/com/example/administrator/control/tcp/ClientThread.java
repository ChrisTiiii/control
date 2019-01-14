package com.example.administrator.control.tcp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import com.example.administrator.control.MyApp;
import com.example.administrator.control.bean.ComputerBean;
import com.example.administrator.control.util.MessageEvent;
import com.example.administrator.control.util.TimeUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * author: ZhongMing
 * DATE: 2019/1/11 0011
 * Description:tcp客户端线程
 **/
public class ClientThread implements Runnable {
    private static final String IP = "192.168.0.121";
    private static final int SPORT = 2222;
    private static final int TIME_OUT = 10000;//超时
    private OutputStream os;
    private BufferedReader br;
    private Socket socket;

    //接收UI线程的消息
    public Handler revHandler;


    public ClientThread() {
    }

    @Override
    public void run() {
        //创建一个无连接的Socket
        socket = new Socket();
        try {
            //连接到指定的IP和端口号，并指定10s的超时时间
            socket.connect(new InetSocketAddress(IP, SPORT), TIME_OUT);
            //向服务端发送数据
            os = socket.getOutputStream();
            if (socket.isConnected()) {
                Log.v("sd", "socket已连接");
                ComputerBean computerBean = new ComputerBean("33333", "server", TimeUtil.nowTime(), "Connect", null, null, null);
                sendData(new Gson().toJson(computerBean));
                ComputerBean computerBean1 = new ComputerBean("33333", "server", TimeUtil.nowTime(), "GetId", "test", "client", "client");
                sendData(new Gson().toJson(computerBean1));
            } else {
                socket.connect(new InetSocketAddress(IP, SPORT), TIME_OUT);
            }
            //接收服务端的数据
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            getData();
//            sendData(null);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof SocketTimeoutException) {
                Log.v("xx", "连接超时，正在重连");
            } else if (e instanceof NoRouteToHostException) {
                Log.v("dd", "该地址不存在，请检查");
            } else if (e instanceof ConnectException) {
                Log.v("dd", "连接异常或被拒绝，请检查");
            }
        }
    }

    //读取数据
    private void getData() {
        //读取数据会阻塞，所以创建一个线程来读取
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收服务器的消息，发送显示在主界面
                String content;
                try {
                    while ((content = br.readLine()) != null) {
                        MessageEvent messageEvent = new MessageEvent(MyApp.ACCEPPT);
                        messageEvent.setMessage(content);
                        EventBus.getDefault().post(messageEvent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (e instanceof SocketException) {
                        Log.v("xx", "连接超时，正在重连");
                    }
                }
            }
        }).start();

    }

    //发送数据
    public void sendData(String msg) {
        System.out.println("send:" + msg);
        if (msg != null) {
            try {
                os.write(msg.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //非UI线程，自己创建
            Looper.prepare();
            revHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //将用户输入的内容写入到服务器
                    try {
                        os.write(((msg.obj) + "").getBytes("utf-8"));
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            };
            Looper.loop();
        }

    }
}
