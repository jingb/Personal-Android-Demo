package com.jingb.application;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jingb.application.ninegag.imageload.BaseFragment;

/**
 * Created by jingb on 16/6/9.
 */
public class BaseActivity extends FragmentActivity {

    protected void replaceFragment(int viewId, BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(viewId, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alpha:
                Toast.makeText(this, "alpha", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.bottom_right:
                Toast.makeText(this, "bottom_right", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.scale:
                Toast.makeText(this, "scale", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.cards:
                Toast.makeText(this, "cards", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
