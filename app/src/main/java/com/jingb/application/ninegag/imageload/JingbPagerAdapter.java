package com.jingb.application.ninegag.imageload;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jingb.application.Jingb;

import java.util.HashMap;
import java.util.Map;

public class JingbPagerAdapter extends FragmentPagerAdapter {

    private Class<?> mClazz;

    private Category mCategory;

    private Map<String, BaseFragment> fragments;

    public JingbPagerAdapter(FragmentManager fm, Class<? extends BaseFragment> clazz) {
        super(fm);
        mClazz = clazz;
    }

    public JingbPagerAdapter(FragmentManager fm, Category category) {
        super(fm);
        mCategory = category;
        fragments = new HashMap<>();
    }

    public BaseFragment getFragmentByCategory(String category) {
        return fragments.get(category);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Jingb.CATEGORYS[position];
    }

    @Override
    public Fragment getItem(int position) {
        //ContentFragment fragment = ContentFragment.newInstance(mCategory);
        String category = Category.values()[position].getDisplayName();
        ContentFragment fragment = ContentFragment.newInstance(Category.values()[position]);
        if (!fragments.containsKey(category)) {
            Log.i(Jingb.SECOND_TAG, "put into fragments: " + category);
            fragments.put(category, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return Jingb.CATEGORYS.length;
    }
}