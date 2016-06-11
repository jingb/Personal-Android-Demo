package com.jingb.application.ninegag.imageload;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.jingb.application.BaseActivity;
import com.jingb.application.Jingb;
import com.jingb.application.ListViewAppearenceStyle;
import com.jingb.application.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageLoadMainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    Category mCategory = Category.hot;

    public Category getmCategory() {
        return mCategory;
    }

    BaseFragment mContentFragment;

    //viewpager和fragment配合的adapter
    private JingbPagerAdapter mAdapter;

    @Bind(R.id.maintabs)
    PagerSlidingTabStrip mTabs;

    @Bind(R.id.mainpager)
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageload_main);
        /** ButterKnife初始化 ***/
        ButterKnife.bind(ImageLoadMainActivity.this);

        mAdapter = new JingbPagerAdapter(getSupportFragmentManager(), mCategory);
        mPager.setAdapter(mAdapter);
        mTabs.setViewPager(mPager);
        mTabs.setOnPageChangeListener(this);
    }

    public void setCategory(Category category) {
        if (mCategory == category) {
            return;
        }
        mCategory = category;
        setTitle(mCategory.getDisplayName());
        Bundle args = new Bundle();
        args.putString(Jingb.CATEGORY, mCategory.getDisplayName());
        mContentFragment = new ContentFragment();
        mContentFragment.setArguments(args);
        //replaceFragment(R.id.content_frame, mContentFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContentFragment fragment = (ContentFragment) mAdapter.getFragmentByCategory(mCategory.getDisplayName());
        switch (item.getItemId()) {
            case R.id.alpha:
                fragment.setListviewApparenceStyle(ListViewAppearenceStyle.ALPHA);
                return true;
            case R.id.bottom_right:
                fragment.setListviewApparenceStyle(ListViewAppearenceStyle.BOTTOM_RIGHT);
                return true;
            case R.id.scale:
                fragment.setListviewApparenceStyle(ListViewAppearenceStyle.SCALE);
                return true;
            case R.id.cards:
                fragment.setListviewApparenceStyle(ListViewAppearenceStyle.CARDS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.i(Jingb.SECOND_TAG, "onPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {
        Log.i(Jingb.SECOND_TAG, "onPageSelected position: " + position);
        mCategory = Category.values()[position];
        Log.i(Jingb.SECOND_TAG, "mCategory: " + mCategory);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Log.i(Jingb.SECOND_TAG, "onPageScrollStateChanged");
    }
}
