package com.jingb.application.ninegag.pager_sliding_tabstrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.jingb.application.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.tabs)
    PagerSlidingTabStrip mTabs;

    @Bind(R.id.pager)
    ViewPager mPager;

    private JingbPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_sliding_tabstrip_main);
        ButterKnife.bind(this);

        mAdapter = new JingbPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabs.setViewPager(mPager);
    }

    public class JingbPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"hot", "trending", "fresh"};

        public JingbPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt("position", position);
            BasicFragment fragment = new BasicFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
