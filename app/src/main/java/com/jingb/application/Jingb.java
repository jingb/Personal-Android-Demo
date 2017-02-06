package com.jingb.application;

import android.net.Uri;
import android.util.Log;

import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.core.ImagePipeline;

/**
 * Created by jingb on 16/4/2.
 */
public class Jingb {

    /**
     * sth done when the app
     * come into the main page from the splash
     * and will be invoked once
     */
    public static boolean appStart = true;

    public static final String TAG = "jingb";
    public static final String SECOND_TAG = "eliza";
    public static final int FAIL = -1;
    public static final int SUCCESS = 0;

    public static final int[] COLORS = {R.color.holo_blue_light, R.color.holo_green_light,
            R.color.holo_orange_light, R.color.holo_purple_light, R.color.holo_red_light};

    public static final String[] CATEGORYS = {"hot", "trending", "fresh"};

    public static final int COMMON_ANIMATION_DURATION = 2000;

    public static final String DEFAULT_NET_REQUEST_ERROR_MSG = "Bad network!Try it again later!";

    /**
     * 在logcat打印出 图片是否从内存或者硬盘取的
     */
    public static void recordPicSituation(ImagePipeline imagePipeline, Uri uri) {
        String inMemory = imagePipeline.isInBitmapMemoryCache(uri) ? "true" : "false";
        DataSource<Boolean> inDiskFuture = imagePipeline.isInDiskCache(uri);
        String inDisk = "未知";
        if (inDiskFuture.isFinished()) {
            inDisk = inDiskFuture.getResult() ? "true" : "false";
        }

        Log.i(Jingb.TAG, "在内存  " + inMemory + "  " + uri.getPath());
        Log.i(Jingb.TAG, "在硬盘  " + inDisk + "  " + uri.getPath());
    }

}
