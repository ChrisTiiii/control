package com.example.administrator.control.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.administrator.control.R;
import com.example.administrator.control.util.AllCapTransformationMethod;
import com.example.administrator.control.util.SharedPreferencesUtils;
import com.example.administrator.control.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.btn_begin_time)
    Button btnBeginTime;
    @BindView(R.id.tv_begin)
    TextView tvBegin;
    @BindView(R.id.btn_end_time)
    Button btnEndTime;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et_2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.et_4)
    EditText et4;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private String begin;
    private String end;
    private SharedPreferencesUtils helper;
    String code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setContentView(R.layout.register_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        et1.setTransformationMethod(new AllCapTransformationMethod(true));
        et2.setTransformationMethod(new AllCapTransformationMethod(true));
        et3.setTransformationMethod(new AllCapTransformationMethod(true));
        et4.setTransformationMethod(new AllCapTransformationMethod(true));
    }

    @OnClick({R.id.btn_begin_time, R.id.btn_end_time, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_begin_time:
                setTime(1);
                break;
            case R.id.btn_end_time:
                setTime(2);
                break;
            case R.id.btn_register:
                if (getCode()) {
                    helper = new SharedPreferencesUtils(this, "setting");
                    helper.putValues(new SharedPreferencesUtils.ContentValue("begin", begin));
                    helper.putValues(new SharedPreferencesUtils.ContentValue("end", end));
                    helper.putValues(new SharedPreferencesUtils.ContentValue("valid", getTimeDiff()));
                    helper.putValues(new SharedPreferencesUtils.ContentValue("isVal", true));
                    if (Integer.parseInt(getTimeDiff()) > 0)
                        new SweetAlertDialog(this)
                                .setTitleText("激活详情")
                                .setContentText("起始时间:" + helper.getString("begin") + "\n截止时间:" + helper.getString("end") + "\n剩余有效期:" + TimeUtil.getDef(TimeUtil.nowTime(), end) + "天")
                                .setConfirmText("马上去登录")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        try {
                                            Thread.sleep(100);
                                            finish();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .show();
                    else Toast.makeText(this, "注册码已过期", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "激活码有误", Toast.LENGTH_SHORT).show();
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");
                    tvBegin.setText("");
                    tvEnd.setText("");
                }
                break;
        }
    }


    /**
     * 激活码编码
     * 第3位为S
     * 第8位为L
     * 第10位为K
     * 第14位为J
     *
     * @return
     */

    public boolean getCode() {
        String str1 = et1.getText().toString();
        String str2 = et2.getText().toString();
        String str3 = et3.getText().toString();
        String str4 = et4.getText().toString();
        code = str1 + str2 + str3 + str4;
        if (getTimeDiff() != null) {
            if (code.length() != 16) {
                return false;
            } else {
                if (code.charAt(2) == 'S' || code.charAt(2) == 's') {
                    if (code.charAt(7) == 'L' || code.charAt(7) == 'l') {
                        if (code.charAt(9) == 'K' || code.charAt(9) == 'k') {
                            if (code.charAt(13) == 'J' || code.charAt(13) == 'j') {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * 获取注册时间差
     *
     * @return
     */
    public String getTimeDiff() {
        return begin == null || end == null ? null : TimeUtil.getDef(begin, end);
    }


    public void setTime(final int type) {
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(RegisterActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (type == 1) {
                    begin = sdf.format(date);
                    tvBegin.setText(begin);
                } else if (type == 2) {
                    end = sdf.format(date);
                    tvEnd.setText(end);
                }
            }
        }).setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

}
