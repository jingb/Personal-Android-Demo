package com.jingb.application.ninegag.foldablelayout;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.jingb.application.R;

/**
 * Created by jingb on 16/3/19.
 */
public class Painting {

    int imageId;

    public Painting(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static Painting[] getAllPaintings(Resources res) {
        TypedArray images = res.obtainTypedArray(R.array.paintings_images);

        int size = 5;
        Painting[] paintings = new Painting[size];

        for (int i = 0; i < size; i++) {
            final int imageId = images.getResourceId(i, -1);
            paintings[i] = new Painting(imageId);
        }

        images.recycle();

        return paintings;
    }

}
