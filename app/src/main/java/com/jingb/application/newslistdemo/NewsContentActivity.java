package com.jingb.application.newslistdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.jingb.application.R;

public class NewsContentActivity extends Activity {

    static final String TITLE = "title";
    static final String CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_content);

        String title = getIntent().getStringExtra(TITLE);
        String content = getIntent().getStringExtra(CONTENT);

        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().
                findFragmentById(R.id.news_content_fragment);
        newsContentFragment.inflateNews(title, content);

    }

    public static void actionStart(Context context, String title, String content) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(CONTENT, content);
        context.startActivity(intent);
    }

}
