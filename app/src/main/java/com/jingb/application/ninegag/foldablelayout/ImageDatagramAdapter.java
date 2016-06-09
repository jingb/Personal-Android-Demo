package com.jingb.application.ninegag.foldablelayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.jingb.application.App;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.NineGagImageDatagram;
import com.jingb.application.util.ImageCacheManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jingb on 16/3/19.
 */
public class ImageDatagramAdapter extends ArrayAdapter<NineGagImageDatagram> {

    protected int mViewId;

    protected Resources mResource;

    protected static final float MAX_HEIGHT = 200f;

    Drawable mDefaultImageDrawable;
    Drawable mErrorImageDrawable;


    private List<NineGagImageDatagram> mImageDatagrams;

    private GenericDraweeHierarchyBuilder builder;
    GenericDraweeHierarchy hierarchy;
    ImagePipeline imagePipeline;

    public ImageDatagramAdapter(Context context, int resource, List<NineGagImageDatagram> imageDatagrams) {
        super(context, resource, imageDatagrams);
        this.mViewId = resource;
        mResource = getContext().getResources();
        if (mResource == null) {
            mResource = App.getContext().getResources();
        }
        mErrorImageDrawable = mResource.getDrawable(R.drawable.loading_error);
        mImageDatagrams = imageDatagrams;
        ImageCacheManager.init(getContext());

        builder = new GenericDraweeHierarchyBuilder(mResource);
        hierarchy = builder
            .setProgressBarImage(new ProgressBarDrawable())
            .setPlaceholderImage(mResource.getDrawable(R.drawable.loading))
            .setFailureImage(mResource.getDrawable(R.drawable.loading_error))
            .build();
        imagePipeline = Fresco.getImagePipeline();
    }

    public ImageDatagramAdapter(Context context, int resource, NineGagImageDatagram[] imageDatagrams) {
        super(context, resource, imageDatagrams);
        this.mViewId = resource;
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

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mViewId, null);
        }
        if (position + 1 <= mItemCount) {

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
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mViewId, null);
        }
        if (position + 1 <= mItemCount) {
            Holder holder = getHolder(convertView);
            NineGagImageDatagram imageDatagram = getItem(position);
            Uri uri = Uri.parse(imageDatagram.getImages().getSmall());
            //打印图片是否在内存或硬盘取
            Jingb.recordPicSituation(imagePipeline, uri);
            //holder.image.getHierarchy() 此值不会为空，if语句里永远不执行
            if (holder.image.getHierarchy() == null) {
                Log.e(Jingb.TAG, "set hierarchy");
                holder.image.setHierarchy(hierarchy);
            }

            mDefaultImageDrawable = new ColorDrawable(mResource.getColor(
                    Jingb.COLORS[position % Jingb.COLORS.length]));
            holder.image.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            holder.image.getHierarchy().setPlaceholderImage(mDefaultImageDrawable);
            holder.image.getHierarchy().setFailureImage(mErrorImageDrawable);
            holder.image.setImageURI(uri);
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

    /*static class Holder {
        @Bind(R.id.imageItemOfImageLoad)
        ImageView image;

        @Bind(R.id.imageCaptionOfImageLoad)
        TextView caption;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }*/

    static class Holder {
        @Bind(R.id.imageItemOfImageLoad)
        SimpleDraweeView image;

        @Bind(R.id.imageCaptionOfImageLoad)
        TextView caption;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
