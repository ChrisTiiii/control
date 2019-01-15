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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.control.R;
import com.example.administrator.control.bean.SendCommand;
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
    @BindView(R.id.btn_video_normal)
    Button btnVideoNormal;
    @BindView(R.id.btn_img_start)
    Button btnImgStart;
    @BindView(R.id.btn_img_bigger)
    Button btnImgBigger;
    @BindView(R.id.btn_img_smaller)
    Button btnImgSmaller;
    @BindView(R.id.btn_img_full)
    Button btnImgFull;
    @BindView(R.id.btn_img_exit)
    Button btnImgExit;
    @BindView(R.id.ppt_start)
    Button pptStart;
    @BindView(R.id.ppt_last)
    Button pptLast;
    @BindView(R.id.ppt_next)
    Button pptNext;
    @BindView(R.id.ppt_first)
    Button pptFirst;
    @BindView(R.id.ppt_lastest)
    Button pptLastest;
    @BindView(R.id.ppt_exit)
    Button pptExit;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.option)
    LinearLayout llOption;


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
        if (compId.equals("all")) {
            llAll.setVisibility(View.VISIBLE);
            llOption.setVisibility(View.GONE);
        } else {
            llAll.setVisibility(View.GONE);
            llOption.setVisibility(View.VISIBLE);
        }
        computerId.setText(compId);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void sendMsg(int kind, int order, String msg) {
        SendCommand.Command command = new SendCommand.Command(kind, order);
        SendCommand sendCommand = new SendCommand(account, compId, TimeUtil.nowTime(), "Command", "client", command, msg);
        thread.sendData(new Gson().toJson(sendCommand));
    }


    @OnClick({R.id.btn_video_normal, R.id.btn_computer_open, R.id.fullscreen, R.id.btn_computer_close, R.id.btn_light_open, R.id.btn_light_close, R.id.btn_img_last, R.id.btn_img_next, R.id.btn_video_open, R.id.btn_video_close, R.id.btn_video_start, R.id.btn_video_stop, R.id.btn_video_up, R.id.btn_video_down, R.id.btn_video_speed, R.id.btn_video_backup, R.id.btn_video_voiceup, R.id.btn_video_voicelow, R.id.btn_video_mute, R.id.btn_img_start, R.id.btn_img_bigger, R.id.btn_img_smaller, R.id.btn_img_full, R.id.btn_img_exit, R.id.ppt_start, R.id.ppt_last, R.id.ppt_next, R.id.ppt_first, R.id.ppt_lastest, R.id.ppt_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_computer_open:
                Toast.makeText(getContext(), "open all", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_computer_close:
                Toast.makeText(getContext(), "close all", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_light_open:
                Toast.makeText(getContext(), "open light", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_light_close:
                Toast.makeText(getContext(), "close light", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_img_last:
                sendMsg(2, 4, "img last");
                break;
            case R.id.btn_img_next:
                sendMsg(2, 5, "img next");
                break;
            case R.id.btn_img_start:
                sendMsg(2, 1, "img start");
                break;
            case R.id.btn_img_bigger:
                sendMsg(2, 2, "img bigger");
                break;
            case R.id.btn_img_smaller:
                sendMsg(2, 3, "img smaller");
                break;
            case R.id.btn_img_full:
                sendMsg(2, 7, "img full");
                break;
            case R.id.btn_img_exit:
                sendMsg(2, 6, "img exit");
                break;
            case R.id.btn_video_open:
                sendMsg(1, 1, "video open");
                break;
            case R.id.btn_video_close:
                sendMsg(1, 3, "video close");
                break;
            case R.id.btn_video_start:
                sendMsg(1, 2, "video start");
                break;
            case R.id.btn_video_stop:
                sendMsg(1, 2, "video stop");
                break;
            case R.id.btn_video_up:
                sendMsg(1, 12, "video previous");
                break;
            case R.id.btn_video_down:
                sendMsg(1, 11, "video next");
                break;
            case R.id.btn_video_speed:
                sendMsg(1, 8, "video speed up");
                break;
            case R.id.btn_video_backup:
                sendMsg(1, 9, "video speed low");
                break;
            case R.id.btn_video_voiceup:
                sendMsg(1, 5, "video up");
                break;
            case R.id.btn_video_voicelow:
                sendMsg(1, 6, "video down");
                break;
            case R.id.btn_video_mute:
                sendMsg(1, 7, "video mute");
                break;
            case R.id.fullscreen:
                sendMsg(1, 4, "video fullscreen");
                break;
            case R.id.btn_video_normal:
                sendMsg(1, 10, "video normal");
                break;
            case R.id.ppt_start:
                sendMsg(3, 1, "ppt start");
                break;
            case R.id.ppt_last:
                sendMsg(3, 2, "ppt last");
                break;
            case R.id.ppt_next:
                sendMsg(3, 3, "ppt next");
                break;
            case R.id.ppt_first:
                sendMsg(3, 4, "ppt first");
                break;
            case R.id.ppt_lastest:
                sendMsg(3, 5, "ppt lastest");
                break;
            case R.id.ppt_exit:
                sendMsg(3, 6, "ppt exit");
                break;

        }
    }


}
