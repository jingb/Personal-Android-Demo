package com.jingb.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class TestActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        View view = new View(this);
        view.setVisibility(View.VISIBLE);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setBackgroundColor(getResources().getColor(R.color.cube_holo_blue_light));

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.CENTER | Gravity.TOP;

        //params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //params.height = WindowManager.LayoutParams.MATCH_PARENT;

        /*ImageView img_diamond_mask = (ImageView) getResources().getDrawable(R.drawable.loading);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        img_diamond_mask.setLayoutParams(layoutParams);*/

        View view1 = new View(this);
        view1.setVisibility(View.VISIBLE);
        view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view1.setBackgroundColor(getResources().getColor(R.color.cube_holo_red_light));

        ImageView imageView = new ImageView(this);
        imageView.setBackground(getResources().getDrawable(R.drawable.loading));

        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(view1, params);

        //setContentView(view1);

        //createFloatView();
    }

    private void createFloatView()
    {
        Button btn_floatView = new Button(getApplicationContext());
        btn_floatView.setText("悬浮窗");

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
         * 即拉下通知栏不可见
         */

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        // 设置悬浮窗的长得宽
        params.width = 100;
        params.height = 100;

        wm.addView(btn_floatView, params);
    }
}
