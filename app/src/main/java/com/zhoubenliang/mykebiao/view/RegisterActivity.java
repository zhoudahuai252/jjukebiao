package com.zhoubenliang.mykebiao.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.contonle.CommonUtils;

import cn.bmob.v3.Bmob;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, CommonUtils.BMOB_APPID);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation_back_selector);
        toolbar.setTitle("用户注册");


    }

}
