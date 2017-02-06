package com.jingb.application;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jingb.application.util.ImageCacheManager;

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
        /** Fresco **/
        Fresco.initialize(sContext);
        /** custom ImageCacheManager **/
        ImageCacheManager.init(this);
    }

    public static Context getContext() {
        return sContext;
    }

}
