package com.jingb.application.ninegag.imageload;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jingb.application.R;
import com.jingb.application.ninegag.NineGagDatagram;
import com.jingb.application.ninegag.NineGagImageDatagram;
import com.jingb.application.ninegag.foldablelayout.ImageDatagramAdapter;
import com.jingb.application.util.GsonRequest;

public class ImageLoadMainActivity extends Activity {

    String mUrl = "http://infinigag.k3min.eu/hot";

    NineGagImageDatagram[] mImagesArray;
    ListView mListView;
    ImageDatagramAdapter mAdapter;

    RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageload_main);

        mListView = (ListView) findViewById(R.id.listview_of_imageload_main);
        getImages();

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(ImageLoadMainActivity.this,
                            "touch the bottom", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

    }

    public void getImages() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        GsonRequest<NineGagDatagram> request = new GsonRequest<NineGagDatagram>(mUrl,
                NineGagDatagram.class, new Response.Listener<NineGagDatagram>() {
            @Override
            public void onResponse(NineGagDatagram response) {
                Toast.makeText(ImageLoadMainActivity.this, "load data finish", Toast.LENGTH_SHORT).show();
                mImagesArray = response.getData();
                setAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ImageLoadMainActivity.this, "fail", Toast.LENGTH_SHORT).show();
                Log.e("error", "get data fail");
            }
        }, mRetryPolicy);
        requestQueue.add(request);
    }

    private void setAdapter() {
        mAdapter = new ImageDatagramAdapter(this, R.layout.imageload_listview_item, mImagesArray);
        mListView.setAdapter(mAdapter);
    }

}
