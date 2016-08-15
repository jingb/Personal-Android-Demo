package com.jingb.application.ninegag.imageload.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jingb.application.Jingb;
import com.jingb.application.ninegag.imageload.fragment.BaseFragment;
import com.jingb.application.ninegag.imageload.fragment.ContentFragment;
import com.jingb.application.ninegag.imageload.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    public List<BaseFragment> getAllFragments() {
        List<BaseFragment> result = new ArrayList<>();
        Iterator<Map.Entry<String, BaseFragment>> it = fragments.entrySet().iterator();
        while (it.hasNext()) {
            result.add(it.next().getValue());
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Jingb.CATEGORYS[position];
    }

    @Override
    public Fragment getItem(int position) {
        String category = Category.values()[position].getDisplayName();
        ContentFragment fragment = ContentFragment.newInstance(Category.values()[position]);
        if (!fragments.containsKey(category)) {
            //Logger.i(Jingb.SECOND_TAG, "put into fragments: " + category);
            fragments.put(category, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return Jingb.CATEGORYS.length;
    }
}