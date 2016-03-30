package com.jingb.application.ninegag.foldablelayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jingb on 16/3/19.
 */
public class ImageDatagramAdapter extends ArrayAdapter<NineGagImageDatagram> {

    protected int viewId;

    protected RequestQueue mQueue;

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
        View view;
        Holder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(viewId, null);
            holder = getHolder(view);

            final Drawable defaultImageDrawable = mResource.getDrawable(R.drawable.loading);
            final Drawable errorImageDrawable = mResource.getDrawable(R.drawable.loading_error);

            NineGagImageDatagram imageDatagram = getItem(position);
            ImageCacheManager.init(getContext());
            ImageLoader.ImageListener listener = ImageCacheManager.getImageListener(
                    holder.image, defaultImageDrawable, errorImageDrawable);
            ImageLoader.ImageContainer image = ImageCacheManager.loadImage(imageDatagram.getImages().getLarge(), listener,
                    0, DensityUtils.dip2px(getContext(), MAX_HEIGHT), ImageView.ScaleType.CENTER_CROP);
            holder.image.setImageBitmap(image.getBitmap());
            holder.caption.setText(imageDatagram.getCaption());

        } else {
            view = convertView;
        }

        return view;
    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }

    static class Holder {
        @Bind(R.id.imageItemOfImageLoad)
        ImageView image;

        @Bind(R.id.imageCaptionOfImageLoad)
        TextView caption;

        public ImageLoader.ImageContainer imageRequest;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
