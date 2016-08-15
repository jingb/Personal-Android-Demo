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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageLoadMainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    Category mCategory = Category.hot;

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
        ButterKnife.bind(ImageLoadMainActivity.this);

        mAdapter = new JingbPagerAdapter(getSupportFragmentManager(), mCategory);
        mPager.setAdapter(mAdapter);
        mTabs.setViewPager(mPager);
        mTabs.setOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<BaseFragment> fragments = mAdapter.getAllFragments();
        //ContentFragment fragment = (ContentFragment) mAdapter.getFragmentByCategory(mCategory.getDisplayName());
        switch (item.getItemId()) {
            case R.id.alpha:
                setListviewApparenceStyleForAllFragments(fragments, ListViewAppearenceStyle.ALPHA);
                return true;
            case R.id.bottom_right:
                setListviewApparenceStyleForAllFragments(fragments, ListViewAppearenceStyle.BOTTOM_RIGHT);
                return true;
            case R.id.scale:
                setListviewApparenceStyleForAllFragments(fragments, ListViewAppearenceStyle.SCALE);
                return true;
            case R.id.cards:
                setListviewApparenceStyleForAllFragments(fragments, ListViewAppearenceStyle.CARDS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setListviewApparenceStyleForAllFragments(List<BaseFragment> fragments, String style) {
        for (BaseFragment item: fragments) {
            ((ContentFragment) item).setListviewApparenceStyle(style);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.i(Jingb.SECOND_TAG, "onPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {
        mCategory = Category.values()[position];
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Log.i(Jingb.SECOND_TAG, "onPageScrollStateChanged");
    }

}
