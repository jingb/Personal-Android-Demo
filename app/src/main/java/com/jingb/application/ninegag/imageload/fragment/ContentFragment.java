package com.jingb.application.ninegag.imageload.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.jingb.application.App;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.jingb.application.ninegag.imageload.activity.PhotoViewActivity;
import com.jingb.application.ninegag.imageload.adapter.GagDatagrmAdapter;
import com.jingb.application.ninegag.imageload.dao.GagDatagramHelper;
import com.jingb.application.ninegag.imageload.model.Category;
import com.jingb.application.ninegag.imageload.model.GagDatagram;
import com.jingb.application.ninegag.imageload.model.ListViewAppearenceStyle;
import com.jingb.application.ninegag.imageload.model.NetApi;
import com.jingb.application.util.GsonRequest;
import com.jingb.application.util.NetworkUtils;
import com.jingb.application.util.ToastUtils;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by jingb on 16/6/9.
 */
public class ContentFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    @Bind(R.id.listview_of_imageload_main)
    ListView mListView;

    private GagDatagramHelper mDataHelper;

    GagDatagrmAdapter mAdapter;
    //动画相关的adapter
    private AnimationAdapter mAnimAdapter;

    PtrClassicFrameLayout mPtrFrame;
    String mNextPage = "";

    RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    String mCategory;

    public static final int HOT = 1;
    public static final int TRENDING = 2;
    public static final int FRESH = 3;

    public static ContentFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putString(Jingb.CATEGORY, category.getDisplayName());
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_frag, container, false);

        ButterKnife.bind(this, view);

        mCategory = getArguments().getString(Jingb.CATEGORY);
        Log.e(Jingb.SECOND_TAG, "fragment onCreateView: " + mCategory);
        mDataHelper = new GagDatagramHelper(App.getContext(), Category.getCategory(mCategory));

        mAdapter = new GagDatagrmAdapter(getActivity(), mListView);
        //包一层动画adapter，默认是从右下角滑进屏幕
        mAnimAdapter = new SwingBottomInAnimationAdapter(new SwingRightInAnimationAdapter(mAdapter));
        mAnimAdapter.setAbsListView(mListView);
        mListView.setAdapter(mAnimAdapter);
        //响应点击单个item调到到大图
        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(Category.getCategory(mCategory).ordinal(), null, this);

        asynGetData(String.format(NetApi.URL, mCategory, mNextPage),
                App.getRequestQueue(), null);

        handleDropDownListRefresh(view);
        handleListViewDropUpLoadMore(view);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        if (data != null && data.getCount() == 0) {
            asynGetData(String.format(NetApi.URL, mCategory, mNextPage),
                    App.getRequestQueue(), null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }


    /****
     * 处理listview上拉加载更多
     */
    private void handleListViewDropUpLoadMore(View view) {
        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();
        // binding view and mData
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(final LoadMoreContainer loadMoreContainer) {
                asynGetData(String.format(NetApi.URL, mCategory, mNextPage), App.getRequestQueue(), new Handler() {
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

    /**
     * 处理listview的下拉刷新
     */
    private void handleDropDownListRefresh(View view) {
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                mDatas.clear();
                mDataHelper.deleteAll();
                asynGetData(String.format(NetApi.URL, mCategory, ""), App.getRequestQueue(), new Handler() {
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
    }


    public void asynGetData(String url, RequestQueue requestQueue, final Handler handler) {
        //Logger.i("asynGetData url: " + url);
        final Message msg = handler != null ? handler.obtainMessage() : null;

        GsonRequest<GagDatagram.GagDatagramRequestData> request = new GsonRequest<>(url,
                GagDatagram.GagDatagramRequestData.class, new Response.Listener<GagDatagram.GagDatagramRequestData>() {

            final boolean isRefreshFromTop = ("".equals(mNextPage.trim()));
            @Override
            public void onResponse(GagDatagram.GagDatagramRequestData response) {
                mNextPage = response.getPage();
                ArrayList<GagDatagram> gagDatagrams = response.data;
                if (isRefreshFromTop) {
                    mDataHelper.deleteAll();
                }
                mDataHelper.bulkInsert(gagDatagrams);

                if (handler != null) {
                    msg.arg1 = Jingb.SUCCESS;
                    handler.sendMessage(msg);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showShort("get data fail");
                if (handler != null) {
                    msg.arg1 = Jingb.FAIL;
                    handler.sendMessage(msg);
                }
            }
        }, mRetryPolicy);

        if (NetworkUtils.isNetworkAvailable(App.getContext())) {
            requestQueue.add(request);
        } else {
            /*url = "0:" + url;
            byte[] bytes = requestQueue.getCache().get(url) != null ?
                    requestQueue.getCache().get(url).mData : null;
            if (bytes != null) {
                try {
                    String str = new String(bytes, "utf-8");
                    NineGagDatagram cacheData = new Gson().fromJson(str, NineGagDatagram.class);
                    //mDatas.addPics(Arrays.asList(cacheData.getData()));
                    mAdapter.notifyDataSetChanged();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e(Jingb.TAG, e.getMessage());
                }
            } else {
                Log.e(Jingb.TAG, "could not get mData from cache");
            }*/
        }
    }

    public void setListviewApparenceStyle(String style) {
        if (StringUtils.isEmpty(style)) {
            return ;
        }
        boolean changed = false;
        switch (style) {
            case ListViewAppearenceStyle.ALPHA:
                if (!(mAnimAdapter instanceof AlphaInAnimationAdapter)) {
                    mAnimAdapter = new AlphaInAnimationAdapter(mAdapter);
                    changed = true;
                }
                break;
            case ListViewAppearenceStyle.BOTTOM_RIGHT:
                if (!(mAnimAdapter instanceof SwingBottomInAnimationAdapter)) {
                    mAnimAdapter = new SwingBottomInAnimationAdapter(new SwingRightInAnimationAdapter(mAdapter));
                    changed = true;
                }
                break;
            case ListViewAppearenceStyle.CARDS:
                ToastUtils.showShort("not implemented!");
                break;
            case ListViewAppearenceStyle.SCALE:
                if (!(mAnimAdapter instanceof ScaleInAnimationAdapter)) {
                    mAnimAdapter = new ScaleInAnimationAdapter(mAdapter);
                    changed = true;
                }
                break;
        }
        if (changed) {
            mAnimAdapter.setAbsListView(mListView);
            mListView.setAdapter(mAnimAdapter);
            mAnimAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 响应跳到大图界面
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GagDatagram item = mAdapter.getItem(position);
        String imageUrl = item.images.large;
        Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
        intent.putExtra(PhotoViewActivity.IMAGE_URL, imageUrl);
        intent.putExtra(PhotoViewActivity.PHOTODESC, item.caption);
        intent.putExtra(PhotoViewActivity.MEDIA, item.media);
        startActivity(intent);
    }
}
