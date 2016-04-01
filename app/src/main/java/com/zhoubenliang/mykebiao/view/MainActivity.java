package com.zhoubenliang.mykebiao.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.view.customview.ChangeColorIconWithTextView;
import com.zhoubenliang.mykebiao.view.fragment.FoundFragment;
import com.zhoubenliang.mykebiao.view.fragment.KeBiaoFragment;
import com.zhoubenliang.mykebiao.view.fragment.MeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.id_viewpager)
    ViewPager myViewpager;
    @Bind(R.id.id_indicator_one)
    ChangeColorIconWithTextView idIndicatorOne;
    @Bind(R.id.id_indicator_two)
    ChangeColorIconWithTextView idIndicatorTwo;
    @Bind(R.id.id_indicator_three)
    ChangeColorIconWithTextView idIndicatorThree;
    private KeBiaoFragment kebiaoFragment;
    private FoundFragment foundFragment;
    private MeFragment meFragment;
    private List<ChangeColorIconWithTextView> mTabIndicator;
    private Timer mtimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        myViewpager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        myViewpager.addOnPageChangeListener(this);
        initTabIndicator();
        intiToolbar();
    }

    private void intiToolbar() {
        int currentItem = myViewpager.getCurrentItem();
        if (currentItem == 0) {
            toolbar.setTitle("课表");
        } else if (currentItem == 1) {
            toolbar.setTitle("发现");
        } else if (currentItem == 2) {
            toolbar.setTitle("我");
        }
    }

    private void initTabIndicator() {
        mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
        mTabIndicator.add(idIndicatorOne);
        mTabIndicator.add(idIndicatorTwo);
        mTabIndicator.add(idIndicatorThree);
        idIndicatorOne.setIconAlpha(1.0f);
    }

    @OnClick({R.id.id_indicator_one, R.id.id_indicator_two, R.id.id_indicator_three})
    public void onClick(View view) {
        resetOtherTabs();
        switch (view.getId()) {
            case R.id.id_indicator_one:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                myViewpager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicator.get(1).setIconAlpha(1.0f);
                myViewpager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicator.get(2).setIconAlpha(1.0f);
                myViewpager.setCurrentItem(2, false);
                break;
        }
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

    private boolean isExit = false;

    @Override
    public void onBackPressed() {
        if (isExit) {
            //直接退出
            mtimer.cancel();
            finish();
        } else {
            //
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mtimer = new Timer();
            mtimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 3000);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIconWithTextView left = mTabIndicator.get(position);
            ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (kebiaoFragment == null) {
                        kebiaoFragment = new KeBiaoFragment();
                    }
                    return kebiaoFragment;
                case 1:
                    if (foundFragment == null) {
                        foundFragment = new FoundFragment();
                    }
                    return foundFragment;
                case 2:
                    if (meFragment == null) {
                        meFragment = new MeFragment();
                    }
                    return meFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
