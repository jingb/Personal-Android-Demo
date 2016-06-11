package com.jingb.application;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {
    private static Context sContext;
    private static RequestQueue mRequestQueue;

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(sContext);
        }
        return mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        /** Fresco初始化 **/
        Fresco.initialize(sContext);
    }

    public static Context getContext() {
        return sContext;
    }

}
