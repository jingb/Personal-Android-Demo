package com.jingb.application.ninegag.foldablelayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jingb.application.R;

/**
 * Created by jingb on 16/3/19.
 */
public class PaintingAdapter extends ArrayAdapter<Painting> {

    private int viewId;

    public PaintingAdapter(Context context, int resource, Painting[] paintings) {
        super(context, resource, paintings);
        this.viewId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Painting painting = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(viewId, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageItem);
        imageView.setImageResource(painting.getImageId());

        /*NetworkImageView networkImageView = (NetworkImageView) view.findViewById(R.id.networkImageViewItem);
        networkImageView.setImageResource(painting.getImageId());*/

        return view;
    }

}
