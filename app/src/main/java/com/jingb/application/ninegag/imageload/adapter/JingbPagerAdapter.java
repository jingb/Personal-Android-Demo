package com.jingb.application.ninegag.imageload.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jingb.application.Jingb;
import com.jingb.application.ninegag.imageload.fragment.BaseFragment;
import com.jingb.application.ninegag.imageload.model.Category;

import java.util.List;

/***
 * viewpager和fragment配合的adapter
  */
public class JingbPagerAdapter extends FragmentStatePagerAdapter {

    private Class<?> mClazz;

    private Category mCategory;

    private List<BaseFragment> mFragments;

    public JingbPagerAdapter(FragmentManager fm, Class<? extends BaseFragment> clazz) {
        super(fm);
        mClazz = clazz;
    }

    public JingbPagerAdapter(FragmentManager fm, Category category, List<BaseFragment> fragments) {
        super(fm);
        mCategory = category;
        mFragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Jingb.CATEGORYS[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}