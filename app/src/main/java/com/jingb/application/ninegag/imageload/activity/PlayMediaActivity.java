package com.jingb.application.ninegag.imageload.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jingb.application.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jingb on 16/7/19.
 */
public class PlayMediaActivity extends Activity {

    @Bind(R.id.wv_media)
    WebView mWebView;

    public static final String MEDIAURL = "mediaUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmedia_activity);
        ButterKnife.bind(PlayMediaActivity.this);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        mWebView.loadUrl(getIntent().getStringExtra(MEDIAURL));

    }

}
