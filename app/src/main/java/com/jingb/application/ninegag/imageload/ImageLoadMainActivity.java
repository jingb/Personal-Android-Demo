package com.jingb.application.ninegag.imageload;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.NineGagDatagram;
import com.jingb.application.ninegag.NineGagImageDatagram;
import com.jingb.application.ninegag.foldablelayout.ImageDatagramAdapter;
import com.jingb.application.util.GsonRequest;

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

public class ImageLoadMainActivity extends Activity {

    String mUrl = "http://infinigag.k3min.eu/hot";

    @Bind(R.id.listview_of_imageload_main)
    ListView mListView;

    ImageDatagramAdapter mAdapter;
    PtrClassicFrameLayout mPtrFrame;
    List<NineGagImageDatagram> mDatas;
    String mNextPage = "";
    static boolean mFirstLoad = true;
    RequestQueue mRequestQueue;

    RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageload_main);

        ButterKnife.bind(ImageLoadMainActivity.this);

        Log.i(Jingb.TAG, "max memory: " + Runtime.getRuntime().maxMemory());

        mAdapter = new ImageDatagramAdapter(this, R.layout.imageload_listview_item, mDatas = new ArrayList<>());
        mListView.setAdapter(mAdapter);

        mRequestQueue = Volley.newRequestQueue(this);
        asynGetData(mUrl, mRequestQueue);

        /**
         * 处理下拉刷新
         */
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mDatas.clear();
                asynGetData(mUrl, mRequestQueue);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                /***
                 * 判断是否可以进行刷新操作,当且仅当第一行的内容在视图里(到了顶部)或者当前视图没有内容的时候,
                 * 才触发onRefreshBegin函数,否则会出现随便什么时候拖动屏幕下拉都触发该函数
                 */
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
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                asynGetData(mUrl + "/" + mNextPage, mRequestQueue);
            }
        });
    }

    public void asynGetData(String url, RequestQueue requestQueue) {
        Log.i(Jingb.TAG, "request url: " + url);
        GsonRequest<NineGagDatagram> request = new GsonRequest<NineGagDatagram>(url,
                NineGagDatagram.class, new Response.Listener<NineGagDatagram>() {
            @Override
            public void onResponse(NineGagDatagram response) {
                mDatas.addAll(Arrays.asList(response.getData()));
                mAdapter.notifyDataSetChanged();
                mNextPage = response.getPaging().getNext();
                Log.i(Jingb.TAG, "next page: " + mNextPage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ImageLoadMainActivity.this, "fail", Toast.LENGTH_SHORT).show();
                Log.e(Jingb.TAG, "get data fail");
            }
        }, mRetryPolicy);
        requestQueue.add(request);
    }

}
