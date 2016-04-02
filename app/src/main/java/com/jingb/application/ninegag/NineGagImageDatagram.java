package com.jingb.application.ninegag;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jingb.application.util.GsonRequest;

/**
 * Created by jingb on 16/3/19.
 */
public class NineGagImageDatagram {

    String id;
    String caption;
    NineGagImageUrls images;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NineGagImageUrls getImages() {
        return images;
    }

    public void setImages(NineGagImageUrls images) {
        this.images = images;
    }

}
