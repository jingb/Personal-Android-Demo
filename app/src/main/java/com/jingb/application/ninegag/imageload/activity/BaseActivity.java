package com.jingb.application.ninegag.imageload.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.jingb.application.ninegag.imageload.fragment.BaseFragment;

/**
 * Created by jingb on 16/6/9.
 */
public class BaseActivity extends FragmentActivity {

    protected void replaceFragment(int viewId, BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(viewId, fragment)
                .commit();
    }

}
