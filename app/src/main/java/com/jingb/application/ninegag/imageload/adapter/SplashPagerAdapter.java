package com.jingb.application.ninegag.imageload.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jingb.application.ninegag.imageload.fragment.SlidePageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SplashPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> picList = new ArrayList();

    private List<String> mContentList;

    /***
     * 是否从网络下载图片做引导页背景图
     *
     * are the background pictures from network,
     * this field is left to entend in the future
     */
    private boolean picFromNetWork = false;

    private static final Map<Integer, SlidePageFragment> mFragmentsMap;

    static {
        mFragmentsMap = new HashMap<>();
    }

    public SplashPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Map<Integer, SlidePageFragment> getFragmentsMap() {
        return mFragmentsMap;
    }

    public SlidePageFragment getFragment(int position) {
        Iterator<Map.Entry<Integer, SlidePageFragment>> iterator = mFragmentsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, SlidePageFragment> entry = iterator.next();
            if (entry.getKey() == position) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Fragment getItem(int i) {
        SlidePageFragment fragment;
        if (picFromNetWork) {
            fragment = SlidePageFragment.newInstance(true, picList.get(i), i);
        } else {
            fragment = SlidePageFragment.newInstance(false, mContentList.get(i), i);
        }
        mFragmentsMap.put(i, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        if (picFromNetWork) {
            return picList.size();
        }
        return mContentList.size();
    }

    /**
     * 启动的引导如果是图片则调用此方法把图片url传给Fragment
     *
     * if the pictures are from network and
     * just invoke this method and pass the urls
     */
    public void addPics(List<String> picList) {
        this.picList = picList;
        picFromNetWork = true;
    }

    public void addContents(List<String> contentsList) {
        this.mContentList = contentsList;
        picFromNetWork = false;
    }


}
