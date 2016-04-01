package com.zhoubenliang.mykebiao.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.contonle.SharedPreferencesUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.viewpage)
    ViewPager viewpage;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    private ArrayList<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initData();
        initViewPager();
    }

    private void initData() {
        int[] imageId = {R.drawable.welpic1,
                R.drawable.welpic2, R.drawable.welpic3};
        imageViews = new ArrayList<>();
        for (int id : imageId) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setAdjustViewBounds(true);
            imageViews.add(imageView);
            ImageView dotview = new ImageView(this);
            dotview.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams prams = new LinearLayout.LayoutParams(50, 50);
            dotview.setLayoutParams(prams);
            dotview.setImageResource(R.mipmap.page);
            llRoot.addView(dotview);
            imageView.setImageResource(id);

        }
        //设置默认第一
        ImageView childView = (ImageView) llRoot.getChildAt(0);
        childView.setImageResource(R.mipmap.page_now);
    }

    private void initViewPager() {

        PagerAdapter adapter = new MyWelPageAdapter();
        viewpage.setAdapter(adapter);
        viewpage.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView childView = (ImageView) llRoot.getChildAt(i);
            if (i == position) {
                childView.setImageResource(R.mipmap.page_now);
            } else
                childView.setImageResource(R.mipmap.page);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int currentItem = viewpage.getCurrentItem();
        if (currentItem == imageViews.size() - 1 &&
                state == ViewPager.SCROLL_STATE_DRAGGING) {
            //跳转登陆界面
            startActivity(new Intent(this, LoginActivity.class));
            SharedPreferencesUtils.putString(this, "isFirst", "noFirst");
            finish();
        }
    }

    private class MyWelPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
