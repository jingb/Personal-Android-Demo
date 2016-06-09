package com.jingb.application;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by jingb on 16/6/9.
 */
public class BaseActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
    }
}
