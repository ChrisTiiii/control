package com.example.administrator.control.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.control.MainActivity;
import com.example.administrator.control.R;
import com.example.administrator.control.util.SharedPreferencesUtils;

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
    private SharedPreferencesUtils helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        login();
    }

    private void login() {
        if (!getLocalName().equals("")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("account", getLocalName());
            startActivity(intent);
            finish();
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

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
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

    }
}
