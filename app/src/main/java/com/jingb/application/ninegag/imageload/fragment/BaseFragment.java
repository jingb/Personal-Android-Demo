package com.jingb.application.ninegag.imageload.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jingb.application.ninegag.imageload.activity.BaseActivity;

/**
 * 作为程序内部fragment的一个基类
 * 以后扩展直接加抽象方法
 * 程序内部和fragment相关的方法传参都使用该类
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
