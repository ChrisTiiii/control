package com.example.administrator.control.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.control.MainActivity;
import com.example.administrator.control.R;
import com.example.administrator.control.util.SharedPreferencesUtils;
import com.example.administrator.control.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: ZhongMing
 * DATE: 2019/1/14 0014
 * Description:
 **/
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_login)
    TextInputEditText etLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    private SharedPreferencesUtils helper;
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        login();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVal()) {
            btnRegister.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
        } else {
            btnRegister.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
        }
    }

    /**
     * 判断激活码是否到期
     *
     * @return
     */
    private Boolean isVal() {
        helper = new SharedPreferencesUtils(this, "setting");
        if (helper.getString("end") != null) {
            String end = helper.getString("end");
            String def = TimeUtil.getDef(TimeUtil.nowTime(), end);
            if (!(Integer.parseInt(def) > 0)) {
                helper.putValues(new SharedPreferencesUtils.ContentValue("isVal", false));
            }
        }
        return helper.getBoolean("isVal", false);
    }


    private void login() {
        System.out.println(getLocalName());
        if (getLocalName() != null) {
            if (!getLocalName().equals("")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("account", getLocalName());
                startActivity(intent);
                finish();
            }
        }
    }


    /**
     * 获得保存在本地的用户名
     */
    public String getLocalName() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        helper = new SharedPreferencesUtils(this, "setting");
        String name = helper.getString("name");
        return name;
    }


    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String str = etLogin.getText().toString();
                if (str.length() != 5) {
                    Toast.makeText(this, "输入格式有误", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("account", str);
                    //获取SharedPreferences对象，使用自定义类的方法来获取对象
                    helper = new SharedPreferencesUtils(this, "setting");
                    //创建一个ContentVa对象（自定义的）
                    helper.putValues(new SharedPreferencesUtils.ContentValue("name", str));
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
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
}
