package com.zhoubenliang.mykebiao.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.zhoubenliang.mykebiao.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MiMaZhaoHuiActivity extends AppCompatActivity {

    @Bind(R.id.email_edit)
    EditText emailEdit;
    @Bind(R.id.btn_send)
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_ma_zhao_hui);
        ButterKnife.bind(this);
    }
}
