package com.jingb.application.ninegag.imageload;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.jingb.application.App;
import com.jingb.application.BaseActivity;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.NineGagDatagram;
import com.jingb.application.ninegag.NineGagImageDatagram;
import com.jingb.application.ninegag.foldablelayout.ImageDatagramAdapter;
import com.jingb.application.util.GsonRequest;
import com.jingb.application.util.NetworkUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ImageLoadMainActivity extends BaseActivity {

    String mUrl = "http://infinigag.k3min.eu/hot";

    @Bind(R.id.listview_of_imageload_main)
    ListView mListView;

    ImageDatagramAdapter mAdapter;
    PtrClassicFrameLayout mPtrFrame;
    List<NineGagImageDatagram> mDatas;
    String mNextPage = "";

    RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Fresco初始化 **/
        Fresco.initialize(this);
        setContentView(R.layout.imageload_main);
        /** ButterKnife初始化 ***/
        ButterKnife.bind(ImageLoadMainActivity.this);

        mAdapter = new ImageDatagramAdapter(this, R.layout.imageload_listview_item, mDatas = new ArrayList<>());
        mListView.setAdapter(mAdapter);

        asynGetData(mUrl, App.getRequestQueue(), null);

        /**
         * 处理下拉刷新
         */
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mDatas.clear();
                asynGetData(mUrl, App.getRequestQueue(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.arg1) {
                            case Jingb.SUCCESS:
                                mPtrFrame.refreshComplete();
                                break;
                            default:
                                mPtrFrame.refreshComplete();
                                break;
                        }
                    }
                });

            }
            /***
             * 判断是否可以进行刷新操作,当且仅当第一行的内容在视图里(到了顶部)或者当前视图没有内容的时候,
             * 才触发onRefreshBegin函数,否则会出现随便什么时候拖动屏幕下拉都触发该函数
             */
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });


        /****
         * 处理加载更多
         */
        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();
        // binding view and data
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(final LoadMoreContainer loadMoreContainer) {
                asynGetData(mUrl + "/" + mNextPage, App.getRequestQueue(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.arg1) {
                            case Jingb.SUCCESS:
                                loadMoreContainer.loadMoreFinish(false, true);
                                break;
                            default:
                                loadMoreContainer.loadMoreFinish(false, true);
                                break;
                        }
                    }
                });
            }
        });
    }

    public void asynGetData(String url, RequestQueue requestQueue, final Handler handler) {
        final Message msg = handler != null ? handler.obtainMessage() : null;

        GsonRequest<NineGagDatagram> request = new GsonRequest<>(url,
                NineGagDatagram.class, new Response.Listener<NineGagDatagram>() {
            @Override
            public void onResponse(NineGagDatagram response) {
                mDatas.addAll(Arrays.asList(response.getData()));
                mAdapter.notifyDataSetChanged();
                mNextPage = response.getPaging().getNext();

                if (handler != null) {
                    msg.arg1 = Jingb.SUCCESS;
                    handler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ImageLoadMainActivity.this, "get data fail", Toast.LENGTH_SHORT).show();
                if (handler != null) {
                    msg.arg1 = Jingb.FAIL;
                    handler.sendMessage(msg);
                }
            }
        }, mRetryPolicy);

        if (NetworkUtils.isNetworkAvailable(this)) {
            requestQueue.add(request);
        } else {
            url = "0:" + url;
            byte[] bytes = requestQueue.getCache().get(url) != null ?
                    requestQueue.getCache().get(url).data : null;
            if (bytes != null) {
                try {
                    String str = new String(bytes, "utf-8");
                    NineGagDatagram cacheData = new Gson().fromJson(str, NineGagDatagram.class);
                    mDatas.addAll(Arrays.asList(cacheData.getData()));
                    mAdapter.notifyDataSetChanged();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e(Jingb.TAG, e.getMessage());
                }
            } else {
                Log.e(Jingb.TAG, "could not get data from cache");
            }
        }
    }

}
