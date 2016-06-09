package com.jingb.application.ninegag.pager_sliding_tabstrip;


import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingb.application.Jingb;
import com.jingb.application.R;

/**
 * Created by jingb on 16/6/9.
 */
public class BasicFragment extends Fragment {

    TextView textView;

    private static final String[] CONTENTS = new String[] {"hot", "trending", "fresh"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_frag, container, false);

        Integer position = (Integer) getArguments().get("position");
        textView = (TextView) view.findViewById(R.id.tv_in_base_frag);
        textView.setText(CONTENTS[position]);

        Drawable bgDrawable = new ColorDrawable(getResources().getColor(
                Jingb.COLORS[position % Jingb.COLORS.length]));
        //view.setBackground(bgDrawable);
        return view;
    }
}
