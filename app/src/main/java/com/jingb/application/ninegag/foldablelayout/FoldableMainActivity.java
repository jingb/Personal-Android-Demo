package com.jingb.application.ninegag.foldablelayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jingb.application.App;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.NineGagDatagram;
import com.jingb.application.ninegag.NineGagImageDatagram;
import com.jingb.application.util.GsonRequest;

public class FoldableMainActivity extends Activity {

    String mUrl = "http://infinigag.k3min.eu/hot";

    NineGagImageDatagram[] mImagesArray;
    FoldableListLayout foldableListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foldablelayout_main);

        foldableListLayout = (FoldableListLayout) findViewById(R.id.foldable_list);

        getImages();
    }

    public void setAdapter() {
        foldableListLayout.setAdapter(new ImageDatagramAdapterOfFoldableLayoutDemo(FoldableMainActivity.this,
                R.layout.foldable_item, mImagesArray));
    }

    public void getImages() {
        RequestQueue requestQueue = App.getRequestQueue();
        Log.i(Jingb.TAG, "request url: " + mUrl);
        GsonRequest<NineGagDatagram> request = new GsonRequest<>(mUrl,
                NineGagDatagram.class, new Response.Listener<NineGagDatagram>() {
            @Override
            public void onResponse(NineGagDatagram response) {
                Toast.makeText(FoldableMainActivity.this, "load data finish", Toast.LENGTH_SHORT).show();
                mImagesArray = response.getData();
                setAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoldableMainActivity.this, "req data fail and req url: " + mUrl, Toast.LENGTH_SHORT).show();
                Log.e(Jingb.TAG, "get data fail");
            }
        });
        requestQueue.add(request);
    }

}
