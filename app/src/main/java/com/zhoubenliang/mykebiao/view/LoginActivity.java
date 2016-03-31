package com.zhoubenliang.mykebiao.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.contonle.CommonUtils;
import com.zhoubenliang.mykebiao.contonle.SharedPreferencesUtils;
import com.zhoubenliang.mykebiao.mode.MyUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_name_edit)
    EditText loginNameEdit;
    @Bind(R.id.login_pwd_edit)
    EditText loginPwdEdit;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_face_login)
    Button btnFaceLogin;
    @Bind(R.id.tv_zhaohui)
    TextView tvZhaohui;
    @Bind(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.btn_login, R.id.btn_face_login, R.id.tv_zhaohui, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                loginUserpwd();
                break;
            case R.id.btn_face_login:
                break;
            case R.id.tv_zhaohui:
                startActivity(new Intent(this, MiMaZhaoHuiActivity.class));
                break;
            case R.id.tv_register:
                //跳转注册界面
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

                break;
        }
    }

    /**
     * 普通密码登陆
     */
    private void loginUserpwd() {
        final String userNameString = loginNameEdit.getText().toString().trim();
        final String userPwdString = loginPwdEdit.getText().toString().trim();
        if (TextUtils.isEmpty(userNameString) || TextUtils.isEmpty(userPwdString)) {
            Toast.makeText(this, "用户名密码不能空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            final ProgressDialog dialog = CommonUtils.getProcessDialog(this, "正在登陆");
            dialog.show();
            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
            query.addWhereEqualTo("username", userNameString);
            query.addWhereEqualTo("password", userPwdString);
            query.count(this, MyUser.class, new CountListener() {
                @Override
                public void onSuccess(int i) {
                    Log.d("LoginActivity", "onSuccess--" + i);
                    BmobQuery<MyUser> query2 = new BmobQuery<MyUser>();
                    query2.addWhereEqualTo("username", userNameString);
                    query2.findObjects(LoginActivity.this, new FindListener<MyUser>() {
                        @Override
                        public void onSuccess(List<MyUser> list) {
                            int size = list.size();
                            String face_id = list.get(0).getFace_id();
                            if (size > 0) {
                                //登陆成功
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                SharedPreferencesUtils.putString(LoginActivity.this, "username", userNameString);
                                SharedPreferencesUtils.putString(LoginActivity.this, "userpwd", userPwdString);
                                dialog.dismiss();
                                finish();
                            } else {
                                //登陆失败
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
