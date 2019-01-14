package com.example.administrator.control.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.control.R;
import com.example.administrator.control.bean.CommandBean;
import com.example.administrator.control.tcp.ClientThread;
import com.example.administrator.control.util.TimeUtil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: ZhongMing
 * DATE: 2019/1/14 0014
 * Description:
 **/
@SuppressLint("ValidFragment")
public class ControlFragment extends Fragment {
    @BindView(R.id.computer_id)
    TextView computerId;
    @BindView(R.id.btn_computer_open)
    Button btnComputerOpen;
    @BindView(R.id.btn_computer_close)
    Button btnComputerClose;
    @BindView(R.id.btn_light_open)
    Button btnLightOpen;
    @BindView(R.id.btn_light_close)
    Button btnLightClose;
    @BindView(R.id.btn_img_last)
    Button btnImgLast;
    @BindView(R.id.btn_img_next)
    Button btnImgNext;
    @BindView(R.id.btn_video_open)
    Button btnVideoOpen;
    @BindView(R.id.btn_video_close)
    Button btnVideoClose;
    @BindView(R.id.btn_video_start)
    Button btnVideoStart;
    @BindView(R.id.btn_video_stop)
    Button btnVideoStop;
    @BindView(R.id.btn_video_up)
    Button btnVideoUp;
    @BindView(R.id.btn_video_down)
    Button btnVideoDown;
    @BindView(R.id.btn_video_speed)
    Button btnVideoSpeed;
    @BindView(R.id.btn_video_backup)
    Button btnVideoBackup;
    Unbinder unbinder;
    @BindView(R.id.btn_video_voiceup)
    Button btnVideoVoiceup;
    @BindView(R.id.btn_video_voicelow)
    Button btnVideoVoicelow;
    @BindView(R.id.btn_video_mute)
    Button btnVideoMute;
    @BindView(R.id.fullscreen)
    Button fullscreen;

    private String compId;
    private ClientThread thread;
    private String account;

    @SuppressLint("ValidFragment")
    public ControlFragment(String account, String compId, ClientThread thread) {
        this.account = account;
        this.compId = compId;
        this.thread = thread;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.computer_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        computerId.setText(compId);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    {
//        'From': 'Server',   # send information terminal
//        'SendTo': contact,  # recive information terminal
//        'Time': ctime()     # sending time
//        'Type': 'command',    # information type
//        'Status': 'Success' # reply status
//        'Command': {
//        Kind:1,  # video 1,img 2,serialport 3
//        Order:2  # open 1,play/pause 2, stop3, fullscreen 4,voiceup 5, voicelow 6, Mute 7, speed up 8,  speed low 9, speed normal 10, next 11, previous 12
//    } # command
//        'Msg': server_msg,  # message
//    }


    public void sendMsg(int kind, int order, String msg) {
        CommandBean.Command command = new CommandBean.Command(kind, order);
        CommandBean commandBean = new CommandBean("33333", compId, TimeUtil.nowTime(), "Command", "client", command, msg);
        thread.sendData(new Gson().toJson(commandBean));
    }


    @OnClick({R.id.btn_computer_open, R.id.fullscreen, R.id.btn_computer_close, R.id.btn_light_open, R.id.btn_light_close, R.id.btn_img_last, R.id.btn_img_next, R.id.btn_video_open, R.id.btn_video_close, R.id.btn_video_start, R.id.btn_video_stop, R.id.btn_video_up, R.id.btn_video_down, R.id.btn_video_speed, R.id.btn_video_backup, R.id.btn_video_voiceup, R.id.btn_video_voicelow, R.id.btn_video_mute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_computer_open:
                break;
            case R.id.btn_computer_close:
                break;
            case R.id.btn_light_open:
                break;
            case R.id.btn_light_close:
                break;
            case R.id.btn_img_last:
                sendMsg(2, 12, "last");
                break;
            case R.id.btn_img_next:
                sendMsg(2, 11, "next");
                break;
            case R.id.btn_video_open:
                sendMsg(1, 1, "open");
                break;
            case R.id.btn_video_close:
                sendMsg(1, 3, "close");
                break;
            case R.id.btn_video_start:
                sendMsg(1, 2, "start");
                break;
            case R.id.btn_video_stop:
                sendMsg(1, 2, "stop");
                break;
            case R.id.btn_video_up:
                sendMsg(1, 12, "previous");
                break;
            case R.id.btn_video_down:
                sendMsg(1, 11, "next");
                break;
            case R.id.btn_video_speed:
                sendMsg(1, 8, "speed up");
                break;
            case R.id.btn_video_backup:
                sendMsg(1, 9, "speed low");
                break;
            case R.id.btn_video_voiceup:
                sendMsg(1, 5, "up");
                break;
            case R.id.btn_video_voicelow:
                sendMsg(1, 6, "down");
                break;
            case R.id.btn_video_mute:
                sendMsg(1, 7, "mute");
                break;
            case R.id.fullscreen:
                sendMsg(1, 4, "fullscreen");
                break;
        }
    }
}
