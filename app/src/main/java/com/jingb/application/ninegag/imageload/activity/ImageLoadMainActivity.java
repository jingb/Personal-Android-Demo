package com.jingb.application.ninegag.imageload.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.jingb.application.R;
import com.jingb.application.ninegag.imageload.adapter.JingbPagerAdapter;
import com.jingb.application.ninegag.imageload.fragment.BaseFragment;
import com.jingb.application.ninegag.imageload.fragment.ContentFragment;
import com.jingb.application.ninegag.imageload.model.Category;
import com.jingb.application.ninegag.imageload.model.ListViewAppearenceStyle;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageLoadMainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    Category mCategory = Category.hot;

    private JingbPagerAdapter mAdapter;

    @Bind(R.id.maintabs)
    PagerSlidingTabStrip mTabs;

    @Bind(R.id.mainpager)
    ViewPager mPager;

    private Map<String, BaseFragment> fragmentsMap;
    private List<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageload_main);
        ButterKnife.bind(ImageLoadMainActivity.this);

        fragmentsMap = new HashMap<>();
        fragments = new ArrayList<>();
        for (int i = 0; i < Category.values().length; i++) {
            Category category = Category.values()[i];
            ContentFragment fragment = ContentFragment.newInstance(category);
            if (!fragmentsMap.containsKey(category.getDisplayName())) {
                fragmentsMap.put(category.getDisplayName(), fragment);
            }
            if (!fragments.contains(fragment)) {
                fragments.add(fragment);
            }
        }

        mAdapter = new JingbPagerAdapter(getSupportFragmentManager(), mCategory, getAllFragments());
        mPager.setAdapter(mAdapter);
        mTabs.setViewPager(mPager);
        mTabs.setOnPageChangeListener(this);

    }

    public List<BaseFragment> getAllFragments() {
        return fragments;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_right:
                setListviewApparenceStyleForAllFragments(fragments, ListViewAppearenceStyle.BOTTOM_RIGHT);
                return true;
            case R.id.scale:
                setListviewApparenceStyleForAllFragments(fragments, ListViewAppearenceStyle.SCALE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setListviewApparenceStyleForAllFragments(List<BaseFragment> fragments, String style) {
        for (BaseFragment item: fragments) {
            Logger.i(item.getName() + " fragment setListviewApparenceStyle!");
            ((ContentFragment) item).setListviewApparenceStyle(style);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCategory = Category.values()[position];
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
