package com.jingb.application.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.jingb.application.App;
import com.jingb.application.Jingb;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by jingb on 16/4/13.
 */
public class NetworkUtils {

    static RetryPolicy mRetryPolicy;

    static {
        mRetryPolicy = new DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            if (context == null) {
                context = App.getContext();
            }
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            if (info != null) {
                return info.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Jingb.TAG, e.getMessage());
            return false;
        }
    }

    /**
     * Volley network request default error listener
     * simply toast the msg toastContent
     */
    public static Response.ErrorListener getDefaultErrorListener(final String toastContent) {
         return new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Logger.e(error, error.getMessage());
                 if (!StringUtils.isEmpty(toastContent)) {
                    ToastUtils.showLong(toastContent);
                 }
             }
         };
    }

    public static <T> void gsonRequest(String url, Class clazz, final Response.Listener<T> successListener, final Response.ErrorListener errorListener) {

        GsonRequest<T> request = new GsonRequest<>(url, clazz, successListener,
                errorListener, mRetryPolicy);

        if (NetworkUtils.isNetworkAvailable(App.getContext())) {
            App.getRequestQueue().add(request);
        } else {
            ToastUtils.showLong(Jingb.DEFAULT_NET_REQUEST_ERROR_MSG);
        }
    }
}
