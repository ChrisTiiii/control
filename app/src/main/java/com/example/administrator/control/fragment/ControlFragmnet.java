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
public class ControlFragmnet extends Fragment {
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

    private String id;

    @SuppressLint("ValidFragment")
    public ControlFragmnet(String id) {
        this.id = id;
    }

//    public static ControlFragmnet newInstance(String id) {
//        ControlFragmnet fragment = new ControlFragmnet();
//        Bundle args = new Bundle();
//        args.putString("computer", id);
//        fragment.setArguments(args);
//        return fragment;
//    }
//

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            id = getArguments().getString("computer");
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.computer_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (id != null) {
            computerId.setText(id);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_computer_open)
    public void onBtnComputerOpenClicked() {

    }

    @OnClick(R.id.btn_computer_close)
    public void onBtnComputerCloseClicked() {
    }

    @OnClick(R.id.btn_light_open)
    public void onBtnLightOpenClicked() {
    }

    @OnClick(R.id.btn_light_close)
    public void onBtnLightCloseClicked() {
    }

    @OnClick(R.id.btn_img_last)
    public void onBtnImgLastClicked() {
    }

    @OnClick(R.id.btn_img_next)
    public void onBtnImgNextClicked() {
    }

    @OnClick(R.id.btn_video_open)
    public void onBtnVideoOpenClicked() {
    }

    @OnClick(R.id.btn_video_close)
    public void onBtnVideoCloseClicked() {
    }

    @OnClick(R.id.btn_video_start)
    public void onBtnVideoStartClicked() {
    }

    @OnClick(R.id.btn_video_stop)
    public void onBtnVideoStopClicked() {
    }

    @OnClick(R.id.btn_video_up)
    public void onBtnVideoUpClicked() {
    }

    @OnClick(R.id.btn_video_down)
    public void onBtnVideoDownClicked() {
    }

    @OnClick(R.id.btn_video_speed)
    public void onBtnVideoSpeedClicked() {
    }

    @OnClick(R.id.btn_video_backup)
    public void onBtnVideoBackupClicked() {
    }
}
