package com.zhoubenliang.mykebiao.view;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding.view.RxView;
import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.contonle.CommonUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import rx.functions.Action1;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.bt_register_jiance)
    Button btRegisterJiance;
    @Bind(R.id.pwd)
    EditText pwd;
    @Bind(R.id.pwdqr)
    EditText pwdqr;
    @Bind(R.id.bitmapaddress)
    EditText bitmapaddress;
    @Bind(R.id.photobtn)
    Button photobtn;
    @Bind(R.id.btn_img)
    Button btnImg;
    @Bind(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, CommonUtils.BMOB_APPID);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initToolbar();
        initBtn();

    }

    private void initBtn() {
        RxView.clicks(btnImg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        RxView.clicks(btnRegister).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        RxView.clicks(photobtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                btnPhotoClick();
            }
        });

    }

    private void btnPhotoClick() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("RegisterActivity", "SD卡不存在");
            Toast.makeText(RegisterActivity.this, "SD卡不存在",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(RegisterActivity.this, "测试", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPwd(String pwd) {
        String str = "^[a-zA-Z]\\w{5,17}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    /**
     * 判断用户名是否合法
     * @param name   name用户名
     * @return
     */
    private boolean isUsername(String name) {
        String str = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation_back_selector);
        toolbar.setTitle("用户注册");
        RxToolbar.navigationClicks(toolbar).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onToolbarNavigationClicked();
            }
        });
    }

    // 浏览点击
    private void onToolbarNavigationClicked() {
        finish();
    }

}
