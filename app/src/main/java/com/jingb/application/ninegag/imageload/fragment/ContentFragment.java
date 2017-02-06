package com.jingb.application.ninegag.imageload.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
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
import com.jingb.application.util.NetworkUtils;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.orhanobut.logger.Logger;

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

    private GagDatagrmAdapter mAdapter;
    private AnimationAdapter mAnimAdapter;

    private PtrClassicFrameLayout mPtrFrame;
//    private PtrFrameLayout mPtrFrame;

    private String mNextPage = "";

    public static final String CATEGORY = "category";
    private String mCategoryStr;
    private Category mCategory;

    public static ContentFragment newInstance(Category category) {
        Logger.i(category.getDisplayName() + " fragment init!");
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY, category);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_frag, container, false);

        ButterKnife.bind(this, view);

        mCategory = (Category) getArguments().getSerializable(CATEGORY);
        mCategoryStr = mCategory.getDisplayName();

        mDataHelper = new GagDatagramHelper(App.getContext(), Category.getCategory(mCategoryStr));

        mAdapter = new GagDatagrmAdapter(getActivity(), mListView);
        mAnimAdapter = new SwingBottomInAnimationAdapter(new SwingRightInAnimationAdapter(mAdapter));
        mAnimAdapter.setAbsListView(mListView);
        mListView.setAdapter(mAnimAdapter);
        mListView.setOnItemClickListener(this);

        /**
         * initLoader
         */
        getLoaderManager().initLoader(Category.getCategory(mCategoryStr).ordinal(), null, this);

        handleDropDownListRefresh(view);
        handleListViewDropUpLoadMore(view);

        /**
         * autoload the first fragment
         * @see
         *     still have a bug when cooperate with JingbPagerAdapter
         */
        if (mCategory.ordinal() == 0 && Jingb.appStart) {
            mPtrFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPtrFrame.autoRefresh(true);
                    Jingb.appStart = false;
                }
            }, 100);
        }
        /*MaterialHeader materialHeader = new MaterialHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        materialHeader.setColorSchemeColors(colors);
        materialHeader.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        materialHeader.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        materialHeader.setPtrFrameLayout(mPtrFrame);

        mPtrFrame.setHeaderView(materialHeader);
        mPtrFrame.addPtrUIHandler(materialHeader);*/
        return view;
    }

    @Override
    public String getName() {
        return mCategoryStr;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        if (data != null && data.getCount() == 0) {
            NetworkUtils.gsonRequest(String.format(NetApi.URL, mCategoryStr, mNextPage),
                GagDatagram.GagDatagramRequestData.class,
                new Response.Listener<GagDatagram.GagDatagramRequestData>() {
                    @Override
                    public void onResponse(GagDatagram.GagDatagramRequestData response) {
                        storeData(response);
                    }
                }, NetworkUtils.getDefaultErrorListener(Jingb.DEFAULT_NET_REQUEST_ERROR_MSG));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }


    /****
     * handle drop up loading more
     * 处理listview上拉加载更多
     */
    private void handleListViewDropUpLoadMore(View view) {
        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();
        // binding view and data
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(final LoadMoreContainer loadMoreContainer) {
                NetworkUtils.gsonRequest(String.format(NetApi.URL, mCategoryStr, mNextPage),
                        GagDatagram.GagDatagramRequestData.class,
                        new Response.Listener<GagDatagram.GagDatagramRequestData>() {
                            @Override
                            public void onResponse(GagDatagram.GagDatagramRequestData response) {
                                storeData(response);
                                loadMoreContainer.loadMoreFinish(false, true);
                            }
                        },
                        NetworkUtils.getDefaultErrorListener(Jingb.DEFAULT_NET_REQUEST_ERROR_MSG));
            }
        });
    }

    /**
     * handle drop down refersh the content
     * 处理listview的下拉刷新
     */
    private void handleDropDownListRefresh(View view) {
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.load_more_list_view_ptr_frame);
//        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mDataHelper.deleteAll();
                NetworkUtils.gsonRequest(String.format(NetApi.URL, mCategoryStr, ""),
                    GagDatagram.GagDatagramRequestData.class,
                    new Response.Listener<GagDatagram.GagDatagramRequestData>() {
                        @Override
                        public void onResponse(GagDatagram.GagDatagramRequestData response) {
                            storeData(response);
                            mPtrFrame.refreshComplete();
                        }
                    }, NetworkUtils.getDefaultErrorListener(Jingb.DEFAULT_NET_REQUEST_ERROR_MSG));

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


    public void storeData(GagDatagram.GagDatagramRequestData response) {
        mNextPage = response.getPage();
        ArrayList<GagDatagram> gagDatagrams = response.data;
        mDataHelper.bulkInsert(gagDatagrams);
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
            case ListViewAppearenceStyle.SCALE:
                if (!(mAnimAdapter instanceof ScaleInAnimationAdapter)) {
                    mAnimAdapter = new ScaleInAnimationAdapter(mAdapter);
                    changed = true;
                }
                break;
        }
        if (changed) {
            mAnimAdapter.setAbsListView(mListView);
            if (mListView != null) {
                mListView.setAdapter(mAnimAdapter);
            }
            mAnimAdapter.notifyDataSetChanged();
        }
    }

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
