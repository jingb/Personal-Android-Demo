package com.jingb.application;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jingb.application.ninegag.foldablelayout.FoldableMainActivity;
import com.jingb.application.newslistdemo.NewsListMainActivity;
import com.jingb.application.ninegag.imageload.ImageLoadMainActivity;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @Bind(R.id.showTheDpiOfYourScreen)
    Button showTheDpiOfYourScreen;

    @Bind(R.id.newsListFragmentDemo)
    Button newsListFragmentDemo;

    @Bind(R.id.foldableLayoutDemo)
    Button foldableLayoutDemo;

    @Bind(R.id.imageLoadDemo)
    Button imageLoadDemo;

    String lineSeperator = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //showAllSystemProperties();

        ButterKnife.bind(this);

        showTheDpiOfYourScreen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTheDpiOfYourScreen();
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
    }

    public void jumpToActivity(Class clazz) {
        Intent intent = new Intent(MainActivity.this, clazz);
        startActivity(intent);
    }

    private void showTheDpiOfYourScreen() {
        Resources res = getResources();
        String info = "dpi 等于widthPixels/widthInch " + lineSeperator
                + "widthInch是手机屏幕的物理宽度,英寸为单位" + lineSeperator;
        int widthPixels = res.getDisplayMetrics().widthPixels;
        int heightPixels = res.getDisplayMetrics().heightPixels;
        float xdpi = res.getDisplayMetrics().xdpi;
        float ydpi = res.getDisplayMetrics().ydpi;
        double widthInch = widthPixels / xdpi;
        double heighInch = heightPixels / ydpi;
        info += "the widthPixels is " + widthPixels + lineSeperator
                + "the heightPixels is " + heightPixels + lineSeperator
                + "xdpi is " + xdpi + lineSeperator
                + " ydpi is " + ydpi + lineSeperator
                + " 屏幕物理宽度为: " + widthInch + "英寸" + lineSeperator
                + " 屏幕物理高度为: " + heighInch + "英寸";

        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
    }


    private void clickEmail() {
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName("com.android.email",
                "com.android.email.activity.Welcome");
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.MAIN");
        startActivity(mIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    public void showAllSystemProperties() {
        Properties properties = System.getProperties();
        String tag = "System.out";
        Set<Map.Entry<Object, Object>> set = properties.entrySet();
        for (Map.Entry<Object, Object> entry : set) {
            Log.i(tag, entry.getKey() + ": " + entry.getValue());
        }
    }
}
