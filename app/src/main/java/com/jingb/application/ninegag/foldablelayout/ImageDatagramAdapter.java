package com.jingb.application.ninegag.foldablelayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jingb.application.R;
import com.jingb.application.ninegag.NineGagImageDatagram;
import com.jingb.application.util.DensityUtils;
import com.jingb.application.util.ImageCacheManager;

/**
 * Created by jingb on 16/3/19.
 */
public class ImageDatagramAdapter extends ArrayAdapter<NineGagImageDatagram> {

    protected int viewId;

    RequestQueue mQueue;

    protected Resources mResource;

    protected static final float MAX_HEIGHT = 200f;

    public ImageDatagramAdapter(Context context, int resource, NineGagImageDatagram[] imageDatagrams) {
        super(context, resource, imageDatagrams);
        this.viewId = resource;
        mQueue = Volley.newRequestQueue(getContext());
        mResource = getContext().getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NineGagImageDatagram imageDatagram = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(viewId, null);

//        ImageView imageView = (ImageView) view.findViewById(R.id.imageItem);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageItemOfImageLoad);

        final Drawable defaultImageDrawable = new ColorDrawable(mResource.getColor(R.color.holo_red_light));
        final Drawable errorImageDrawable = new ColorDrawable(mResource.getColor(R.color.holo_green_light));

        ImageCacheManager.init(getContext());
        ImageLoader.ImageListener listener = ImageCacheManager.getImageListener(
                imageView, defaultImageDrawable, errorImageDrawable);
        ImageLoader.ImageContainer image = ImageCacheManager.loadImage(imageDatagram.getImages().getLarge(), listener,
                0, DensityUtils.dip2px(getContext(), MAX_HEIGHT), ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(image.getBitmap());

//        TextView textView = (TextView) view.findViewById(R.id.imageCaption);
        TextView textView = (TextView) view.findViewById(R.id.imageCaptionOfImageLoad);
        textView.setText(imageDatagram.getCaption());

        return view;
    }

}
