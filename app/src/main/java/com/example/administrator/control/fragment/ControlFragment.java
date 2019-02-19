package com.example.administrator.control.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.control.R;
import com.example.administrator.control.bean.EqupmentBean;
import com.example.administrator.control.bean.SendCommand;
import com.example.administrator.control.tcp.ClientThread;
import com.example.administrator.control.tcp.ControlThread;
import com.example.administrator.control.util.NetWorkUtil;
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
    @BindView(R.id.computer_status)
    TextView computerStatus;
    @BindView(R.id.ll_wel)
    LinearLayout llWel;
    @BindView(R.id.et_wel1)
    EditText etWel1;
    @BindView(R.id.et_wel2)
    EditText etWel2;
    @BindView(R.id.et_wel3)
    EditText etWel3;
    @BindView(R.id.btn_wel_clear)
    Button btnWelClear;
    @BindView(R.id.btn_create_wel)
    Button btnCreateWel;
    @BindView(R.id.btn_show_wel)
    Button btnShowWel;
    @BindView(R.id.btn_close_wel)
    Button btnCloseWel;

    private EqupmentBean equpBean;
    private ClientThread thread;
    private ControlThread controlThread;
    private String account;

    public ControlFragment(String account, EqupmentBean equpBean, ClientThread thread, ControlThread controlThread) {
        this.account = account;
        this.equpBean = equpBean;
        this.thread = thread;
        this.controlThread = controlThread;
    }

    public ControlFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.computer_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (equpBean != null) {
            if (equpBean.getName().equals("全部设备")) {
                llAll.setVisibility(View.VISIBLE);
                llOption.setVisibility(View.GONE);
                llWel.setVisibility(View.VISIBLE);
            } else {
                llAll.setVisibility(View.GONE);
                llOption.setVisibility(View.VISIBLE);
                llWel.setVisibility(View.GONE);
            }
            switch (equpBean.getStatus()) {
                case 1:
                    computerId.setText(equpBean.getName());
                    computerStatus.setTextColor(Color.parseColor("#43fdff"));
                    computerStatus.setText("• 设备在线");
                    break;
                case -1:
                    computerStatus.setTextColor(Color.parseColor("#ff6e6e"));
                    computerId.setText(equpBean.getName());
                    computerStatus.setText("• 设备不在线");
                    break;
                case 0:
                    computerId.setText(equpBean.getName());
                    break;
            }
        }
        return view;
    }


    /**
     * 默认发送给具体设备
     *
     * @param kind
     * @param order
     * @param msg
     */
    public void sendMsg(final int kind, final int order, final String msg) {
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendCommand.Command command = new SendCommand.Command(kind, order);
                    SendCommand sendCommand = new SendCommand(account, equpBean.getName(), TimeUtil.nowTime(), "Command", "client", command, msg);
                    thread.sendData(new Gson().toJson(sendCommand));
                }
            }).start();
        } else
            Toast.makeText(getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param kind
     * @param order
     * @param sendTo 指定发送给全部设备
     * @param msg
     */
    public void sendMsg(final int kind, final int order, final String sendTo, final String type, final String msg) {
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendCommand.Command command = new SendCommand.Command(kind, order);
                    SendCommand sendCommand = new SendCommand(account, sendTo, TimeUtil.nowTime(), type, "client", command, msg);
                    thread.sendData(new Gson().toJson(sendCommand));
                }
            }).start();
        } else
            Toast.makeText(getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
    }

    /**
     * 发送指令给继电器
     * "aa0a0101010101010101010101010101010101bb"
     */
    public void sendOrder(final String order) {
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    controlThread.sendData(order);
                }
            }).start();
        } else
            Toast.makeText(getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.btn_close_wel, R.id.btn_wel_clear, R.id.btn_create_wel, R.id.btn_show_wel, R.id.btn_video_normal, R.id.btn_computer_open, R.id.fullscreen, R.id.btn_computer_close, R.id.btn_light_open, R.id.btn_light_close, R.id.btn_img_last, R.id.btn_img_next, R.id.btn_video_open, R.id.btn_video_close, R.id.btn_video_start, R.id.btn_video_stop, R.id.btn_video_up, R.id.btn_video_down, R.id.btn_video_speed, R.id.btn_video_backup, R.id.btn_video_voiceup, R.id.btn_video_voicelow, R.id.btn_video_mute, R.id.btn_img_start, R.id.btn_img_bigger, R.id.btn_img_smaller, R.id.btn_img_full, R.id.btn_img_exit, R.id.ppt_start, R.id.ppt_last, R.id.ppt_next, R.id.ppt_first, R.id.ppt_lastest, R.id.ppt_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wel_clear:
                etWel1.setText("");
                etWel2.setText("");
                etWel3.setText("");
                break;
            case R.id.btn_create_wel:
                String wel1 = etWel1.getText().toString();
                String wel2 = etWel2.getText().toString();
                String wel3 = etWel3.getText().toString();
                if (wel1 == null || wel1.equals("")) {
                    wel1 = "热烈欢迎";
                }
                if (wel2 == null || wel2.equals("")) {
                    wel2 = "江苏三棱智慧物联发展股份有限公司";
                }
                if (wel3 == null || wel3.equals("")) {
                    wel3 = "莅临我司参观指导";
                }
                final String temp = wel1 + "," + wel2 + "," + wel3;
                sendMsg(-1, -1, "All", "Wel", temp);
                break;
            case R.id.btn_show_wel:
                sendMsg(5, 1, "All", "Command", "open welcome");
                break;
            case R.id.btn_close_wel:
                sendMsg(5, 2, "All", "Command", "close welcome");
                break;
            case R.id.btn_computer_open:
                sendOrder("aa0a0101010101010101010101010101010101bb");
                break;
            case R.id.btn_computer_close:
                sendOrder("aa0b0202020202020202020202020202020201bb");
                break;
            case R.id.btn_light_open:
                sendMsg(4, 1, "open light");
                break;
            case R.id.btn_light_close:
                sendMsg(4, 1, "close light");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
