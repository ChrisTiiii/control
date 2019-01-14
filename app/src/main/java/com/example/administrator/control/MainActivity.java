package com.example.administrator.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.administrator.control.adapter.ComupterAdapter;
import com.example.administrator.control.fragment.ControlFragmnet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.computer_list)
    RecyclerView computerList;


    private ComupterAdapter comupterAdapter;

    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        computerList.setLayoutManager(new LinearLayoutManager(this));
        comupterAdapter = new ComupterAdapter(this, list);
        computerList.setAdapter(comupterAdapter);
        initRight();
    }

    private void initRight() {
        ControlFragmnet controlFragmnet = new ControlFragmnet("asdwqe");
//        getSupportFragmentManager().beginTransaction().replace(R.id.right_fragment, controlFragmnet).commit();
    }


    public void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("ID:SFJEIR" + i);
        }
    }
}
