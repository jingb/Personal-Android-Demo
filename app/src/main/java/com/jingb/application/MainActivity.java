package com.jingb.application;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.jingb.application.newslistdemo.NewsListMainActivity;
import com.jingb.application.ninegag.foldablelayout.FoldableMainActivity;
import com.jingb.application.ninegag.fresco.FrescoMainActivity;
import com.jingb.application.ninegag.imageload.activity.BaseActivity;
import com.jingb.application.ninegag.imageload.activity.ImageLoadMainActivity;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tv_copyable)
    TextView tv_copyable;

    @Bind(R.id.showTheDpiOfYourScreen)
    Button showTheDpiOfYourScreen;

    @Bind(R.id.newsListFragmentDemo)
    Button newsListFragmentDemo;

    @Bind(R.id.foldableLayoutDemo)
    Button foldableLayoutDemo;

    @Bind(R.id.imageLoadDemo)
    Button imageLoadDemo;

    @Bind(R.id.frescoDemo)
    Button frescoDemo;

    @Bind(R.id.floatView)
    Button floatView;

    @Bind(R.id.PagerSlidingTabStrip)
    Button pagerSlidingTabStrip;

    String lineSeperator = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        showTheDpiOfYourScreen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTheDpiOfYourScreen();
//                        req();
                    }
                }
        );

        newsListFragmentDemo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpToActivity(NewsListMainActivity.class);
                    }
                }
        );

        foldableLayoutDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(FoldableMainActivity.class);
            }
        });

        imageLoadDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(ImageLoadMainActivity.class);
            }
        });

        frescoDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(FrescoMainActivity.class);
            }
        });

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFloatView();
            }
        });

        pagerSlidingTabStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(com.jingb.application.ninegag.pager_sliding_tabstrip.MainActivity.class);
            }
        });

    }

    /**
     * 测试volley硬盘缓存
     */
    public void req() {
        String url = "http://tse1.mm.bing.net/th?id=OIP.Mb0b50663139f0e7fe6abda23e2a51afaH0&pid=15.1";
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = App.getRequestQueue();
        Log.i(Jingb.TAG, "cache key: " + request.getCacheKey());
        byte[] bytes = requestQueue.getCache().get(request.getCacheKey()) != null ?
                requestQueue.getCache().get(request.getCacheKey()).data : null;
        if (bytes != null) {
            Toast.makeText(MainActivity.this, "get data from cache", Toast.LENGTH_SHORT).show();
        } else {
            requestQueue.add(request);
            Log.e(Jingb.TAG, "requestQueue.add(request)");
        }
    }

    public void jumpToActivity(Class clazz) {
        Intent intent = new Intent(MainActivity.this, clazz);
        startActivity(intent);
    }

    private void showTheDpiOfYourScreen() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        String info = "dpi 等于widthPixels/widthInch " + lineSeperator
                + "widthInch是手机屏幕的物理宽度,英寸为单位" + lineSeperator
                + "displayMetrics.density: " + displayMetrics.density + lineSeperator
                + "displayMetrics.densityDpi: " + displayMetrics.densityDpi + lineSeperator
                + "displayMetrics.scaledDensity: " + displayMetrics.scaledDensity + lineSeperator;
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        float xdpi = displayMetrics.xdpi;
        float ydpi = displayMetrics.ydpi;
        double widthInch = widthPixels / xdpi;
        double heighInch = heightPixels / ydpi;
        info += "the widthPixels is " + widthPixels + lineSeperator
                + "the heightPixels is " + heightPixels + lineSeperator
                + "xdpi is " + xdpi + lineSeperator
                + " ydpi is " + ydpi + lineSeperator
                + " 屏幕物理宽度为: " + widthInch + "英寸" + lineSeperator
                + " 屏幕物理高度为: " + heighInch + "英寸";

        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        Logger.i(info);
    }


    private void clickEmail() {
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName("com.android.email",
                "com.android.email.activity.Welcome");
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.MAIN");
        startActivity(mIntent);
    }

    private void createFloatView() {
        Log.i(Jingb.TAG, "createFloatView");

        Button btn_floatView = new Button(getApplicationContext());
        btn_floatView.setText("悬浮窗");

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        //params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
         * 即拉下通知栏不可见
         */

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        // 设置悬浮窗的长得宽
        params.width = 200;
        params.height = 200;

        wm.addView(btn_floatView, params);
    }
}
