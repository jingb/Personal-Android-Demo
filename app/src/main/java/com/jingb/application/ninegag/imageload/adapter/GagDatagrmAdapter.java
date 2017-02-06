package com.jingb.application.ninegag.imageload.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.imageload.model.GagDatagram;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jingb on 16/7/13.
 */
public class GagDatagrmAdapter extends CursorAdapter {

    ListView mListView;
    private LayoutInflater mLayoutInflater;
    private Resources mResource;
    private int mViewId;

    Drawable mDefaultImageDrawable;
    Drawable mErrorImageDrawable;

    private GenericDraweeHierarchyBuilder builder;
    GenericDraweeHierarchy hierarchy;
    ImagePipeline imagePipeline;


    public GagDatagrmAdapter(Context context, ListView listView) {
        super(context, null, false);
        mListView = listView;
        mResource = context.getResources();
        mLayoutInflater = LayoutInflater.from(context);
        mViewId = R.layout.imageload_listview_item;

        mErrorImageDrawable = mResource.getDrawable(R.drawable.loading_error);
        builder = new GenericDraweeHierarchyBuilder(mResource);
        hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable())
                .setPlaceholderImage(mResource.getDrawable(R.drawable.loading))
                .setFailureImage(mResource.getDrawable(R.drawable.loading_error))
                .build();
        imagePipeline = Fresco.getImagePipeline();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(mViewId, null);
    }

    @Override
    public GagDatagram getItem(int position) {
        getCursor().moveToPosition(position);
        return GagDatagram.fromCursor(getCursor());
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int position = cursor.getPosition();

        Holder holder = getHolder(view);

        GagDatagram gagDatagram = GagDatagram.fromCursor(cursor);
        Uri uri = Uri.parse(gagDatagram.images.normal);
        //打印图片是否在内存或硬盘取
        //Jingb.recordPicSituation(imagePipeline, uri);
        //holder.image.getHierarchy() 此值不会为空，if语句里永远不执行
        if (holder.image.getHierarchy() == null) {
            Logger.e("set hierarchy");
            holder.image.setHierarchy(hierarchy);
        }
        mDefaultImageDrawable = new ColorDrawable(mResource.getColor(
                Jingb.COLORS[position % Jingb.COLORS.length]));
        holder.image.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        holder.image.getHierarchy().setPlaceholderImage(mDefaultImageDrawable);
        holder.image.getHierarchy().setFailureImage(mErrorImageDrawable);
        holder.image.setImageURI(uri);
        holder.caption.setText(gagDatagram.caption);

    }

    static class Holder {
        @Bind(R.id.imageItemOfImageLoad)
        SimpleDraweeView image;

        @Bind(R.id.imageCaptionOfImageLoad)
        TextView caption;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }
}
