package com.jingb.application.ninegag.imageload.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.imageload.adapter.SplashPagerAdapter;
import com.jingb.application.ninegag.imageload.fragment.SlidePageFragment;
import com.liangfeizc.slidepageindicator.CirclePageIndicator;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @Bind(R.id.guide_indicator)
    CirclePageIndicator mPageIndicator;

    @Bind(R.id.btn_enter)
    ImageButton mEnterBtn;

    SplashPagerAdapter mSplashPagerAdapter;

    private static final String[] IMAGES = new String[] {
            "http://img1.cache.netease.com/catchpic/B/B2/B2F274C1CCD5A89133261E6252A0C8E9.jpg",
            "http://img5.duitang.com/uploads/item/201408/09/20140809204759_CwtQN.jpeg",
            "http://images.qianlong.com/mmsource/imghylanda/201108/25/21/7131919880274907937.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/78370e46f91582056a63e576.jpg"
    };

    private static final String[] CONTENTSLIST = new String[] {
            "不会打篮球的斯诺克运动员\n" +
            "       不是好的程序猿",
            "我是景b 想去新西兰放羊嗯"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        ButterKnife.bind(SplashActivity.this);

        ViewPager pager = (ViewPager) findViewById(R.id.vp_guide);

        mSplashPagerAdapter =
                new SplashPagerAdapter(getSupportFragmentManager());
        //pagerAdapter.addPics(Arrays.asList(IMAGES));
        mSplashPagerAdapter.addContents(Arrays.asList(CONTENTSLIST));
        pager.setAdapter(mSplashPagerAdapter);
        pager.addOnPageChangeListener(this);

        mPageIndicator.setViewPager(pager);

        mEnterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SplashActivity.this, ImageLoadMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPageSelected(int position) {
        SlidePageFragment fragment = mSplashPagerAdapter.getFragmentsMap().get(position);
        fragment.doAnimation();
        /**
         * 最后一张图片显示进入主程序按钮，隐藏下方的指示按钮栏
         * the btn to enter the main page just show in the last fragment
         */
        if (position == CONTENTSLIST.length - 1) {
            mEnterBtn.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInUp).
                    duration(Jingb.COMMON_ANIMATION_DURATION).
                    playOn(mEnterBtn);
            mPageIndicator.setVisibility(View.INVISIBLE);
        } else {
            mEnterBtn.setVisibility(View.INVISIBLE);
            mPageIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
