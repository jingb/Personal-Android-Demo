package com.jingb.application.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.jingb.application.App;
import com.jingb.application.Jingb;

public class ImageCacheManager {

    private static RequestQueue mRequestQueue;
    private static Context mContext;
    private static ImageLoader mImageLoader;

    public static void init(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (mRequestQueue == null) {
            mRequestQueue = App.getRequestQueue();
        }
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        }
    }

    static int requestCount = 0;


    public static ImageLoader.ImageContainer loadImage(String requestUrl, ImageLoader.ImageListener imageListener,
                                                       int maxWidth, int maxHeigh, ImageView.ScaleType scaleType) {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            return mImageLoader.get(requestUrl, imageListener, maxWidth, maxHeigh, scaleType);
        } else {
            Bitmap bitmap = getDataFromDiskCache(requestUrl);
            ImageLoader.ImageContainer data = mImageLoader.new ImageContainer(
                    bitmap, requestUrl, null, imageListener);
            imageListener.onResponse(data, true);

            return data;
        }
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl, ImageLoader.ImageListener imageListener) {
        return loadImage(requestUrl, imageListener, 0, 0);
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl,
                                                       ImageLoader.ImageListener imageListener, int maxWidth, int maxHeight) {
        return loadImage(requestUrl, imageListener, maxWidth, maxHeight, ImageView.ScaleType.CENTER_INSIDE);
    }


    public static ImageLoader.ImageListener getImageListener(final ImageView imageView,
             final Drawable defaultImageDrawable, final Drawable errorImageDrawable) {
        return new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                if (response.getBitmap() != null) {
                    if (!isImmediate && defaultImageDrawable != null) {
                        /*TransitionDrawable transitionDrawable = new TransitionDrawable(
                                new Drawable[]{
                                        defaultImageDrawable,
                                        new BitmapDrawable(mContext.getResources(),
                                                response.getBitmap())
                                }
                        );
                        transitionDrawable.setCrossFadeEnabled(true);
                        imageView.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(50);*/
                    } else {
                        imageView.setImageBitmap(response.getBitmap());
                    }
                } else if (defaultImageDrawable != null) {
                    //imageView.setImageDrawable(defaultImageDrawable);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageDrawable != null) {
                    imageView.setImageDrawable(errorImageDrawable);
                }
            }
        };
    }

    private static Bitmap getDataFromDiskCache(String url) {
        if (mRequestQueue.getCache().get(url) != null) {
            byte[] bytes = mRequestQueue.getCache().get(url).data;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        } else {
            Log.i(Jingb.TAG, "没有缓存数据");
            return null;
        }
    }
}