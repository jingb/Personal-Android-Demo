package com.jingb.application.ninegag.foldablelayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.NineGagImageDatagram;
import com.jingb.application.util.DensityUtils;
import com.jingb.application.util.ImageCacheManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jingb on 16/3/19.
 */
public class ImageDatagramAdapter extends ArrayAdapter<NineGagImageDatagram> {

    protected int viewId;

    protected Resources mResource;

    protected static final float MAX_HEIGHT = 200f;

    Drawable mDefaultImageDrawable;
    Drawable mErrorImageDrawable;

    private List<NineGagImageDatagram> mImageDatagrams;

    public ImageDatagramAdapter(Context context, int resource, List<NineGagImageDatagram> imageDatagrams) {
        super(context, resource, imageDatagrams);
        this.viewId = resource;
        mResource = getContext().getResources();
        mDefaultImageDrawable = mResource.getDrawable(R.drawable.loading);
        mErrorImageDrawable = mResource.getDrawable(R.drawable.loading_error);
        mImageDatagrams = imageDatagrams;
        ImageCacheManager.init(getContext());
    }

    public ImageDatagramAdapter(Context context, int resource, NineGagImageDatagram[] imageDatagrams) {
        super(context, resource, imageDatagrams);
        this.viewId = resource;
        mResource = getContext().getResources();
    }

    int mItemCount = 0;

    @Override
    public int getCount() {
        return mItemCount;
    }

    @Override
    public void notifyDataSetChanged() {
        mItemCount = mImageDatagrams.size();
        super.notifyDataSetChanged();
    }

    int requestCount = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(viewId, null);
        }
        if (position + 1 <= mItemCount) {
//            requestCount++;
//            Log.e(Jingb.TAG, "requestCount: " + requestCount);

            Holder holder = getHolder(convertView);
            NineGagImageDatagram imageDatagram = getItem(position);
            ImageLoader.ImageListener listener = ImageCacheManager.getImageListener(
                    holder.image, mDefaultImageDrawable, mErrorImageDrawable);
            ImageLoader.ImageContainer image = ImageCacheManager.loadImage(imageDatagram.getImages().getLarge(), listener,
                    0, DensityUtils.dip2px(getContext(), MAX_HEIGHT), ImageView.ScaleType.CENTER_CROP);
            holder.image.setImageBitmap(image.getBitmap());
            holder.caption.setText(imageDatagram.getCaption());
        }
        return convertView;
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

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
