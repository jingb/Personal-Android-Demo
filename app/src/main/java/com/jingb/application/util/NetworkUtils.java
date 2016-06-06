package com.jingb.application.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.jingb.application.App;
import com.jingb.application.Jingb;

/**
 * Created by jingb on 16/4/13.
 */
public class NetworkUtils {

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
}
