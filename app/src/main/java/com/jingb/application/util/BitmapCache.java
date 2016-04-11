package com.jingb.application.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.jingb.application.Jingb;

public class BitmapCache implements ImageLoader.ImageCache {
  
    private LruCache<String, Bitmap> mCache;
  
    public BitmapCache() {  
        int maxSize = (int)(Runtime.getRuntime().maxMemory() / 8);
        Log.i(Jingb.TAG, "cache size: " + maxSize / 1024 / 1024 + "MB");
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override  
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getRowBytes() * bitmap.getHeight();  
            }  
        };  
    }  
  
    @Override  
    public Bitmap getBitmap(String url) {
        //Log.e(Jingb.TAG, "getBitmap from cache: " + url);
        return mCache.get(url);  
    }  
  
    @Override  
    public void putBitmap(String url, Bitmap bitmap) {
        //Log.e(Jingb.TAG, "putBitmap to cache: " + url);
        mCache.put(url, bitmap);  
    }  
  
} 