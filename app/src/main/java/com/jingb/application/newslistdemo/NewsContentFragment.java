package com.jingb.application.newslistdemo;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingb.application.R;

public class NewsContentFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_content_frag, container, false);
        return view;
    }

    public void inflateNews(String title, String content) {
        View visibilityLayout = view.findViewById(R.id.visibilityLayout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView titleView = (TextView) view.findViewById(R.id.news_title);
        TextView newsContentView = (TextView) view.findViewById(R.id.news_content);
        titleView.setText(title);
        newsContentView.setText(content);
    }

}
