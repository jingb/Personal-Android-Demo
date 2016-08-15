package com.jingb.application.util;

import android.widget.Toast;

import com.jingb.application.App;

/**
 * Created by jingb on 16/7/5.
 */
public class ToastUtils {

    public static void showLong(String content) {
        Toast.makeText(App.getContext(), content, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String content) {
        Toast.makeText(App.getContext(), content, Toast.LENGTH_SHORT).show();
    }
}
