package com.zhoubenliang.mykebiao.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.contonle.SharedPreferencesUtils;
import com.zhoubenliang.mykebiao.mode.MyUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    @Bind(R.id.rl_splash)
    RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
        //绑定布局
        ButterKnife.bind(this);
//        rl_splash= (RelativeLayout) findViewById(R.id.rl_splash);
        System.out.println("--->" + rl_splash.toString());
        //开启动画
        startAnimotion();
        System.out.println("test Git");
    }

    private void startAnimotion() {
        //设置渐变动画
        Animation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(2000);
        animation.setAnimationListener(this);
        rl_splash.setAnimation(animation);
    }


    //动画监听
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //判断是否第一次登陆/
        String isFirst = SharedPreferencesUtils.getString("isFirst", this);
        if (isFirst == null) {
            //说明第一次登陆,进入欢迎页面
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();

        } else {
            //校验登陆状态,检查账号密码是否匹配
            String username = SharedPreferencesUtils.getString("username", this);
            String userpwd = SharedPreferencesUtils.getString("userpwd", this);
            checkLogin(username, userpwd);
            System.out.println("test  GitHub ");
        }
    }

    private boolean checkLogin(String username, String userpwd) {
        MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(userpwd);
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        return false;
    }

    public void check(final String username, final String userpwd) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                MyUser user = new MyUser();
                user.setUsername(username);
                user.setPassword(userpwd);
                user.login(SplashActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        subscriber.onError(new Throwable(s));
                    }
                });
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                })
        ;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
