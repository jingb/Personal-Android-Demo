package com.jingb.application.ninegag.imageload.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jingb.application.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by jingb on 16/8/29.
 */
public class JingbListView extends FrameLayout {

    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptr_frame;

    @Bind(R.id.view_container)
    LoadMoreListViewContainer view_container;

    @Bind(R.id.jingb_listview)
    ListView jingb_listview;

    OnLoadMoreHandler mOnLoadMoreHandler;

    public interface OnLoadMoreHandler {
        void onLoadMore();
    }


    public JingbListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.jingb_listview, this);
        ButterKnife.bind(this);

        view_container.useDefaultHeader();
        view_container.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(final LoadMoreContainer loadMoreContainer) {
                mOnLoadMoreHandler.onLoadMore();
                loadMoreContainer.loadMoreFinish(false, true);
            }
        });
        ptr_frame.setLastUpdateTimeRelateObject(this);
        /*ptr_frame.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mDataHelper.deleteAll();
                asynGetData(String.format(NetApi.URL, mCategory, ""), App.getRequestQueue(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.arg1) {
                            case Jingb.SUCCESS:
                                ptr_frame.refreshComplete();
                                break;
                            default:
                                ptr_frame.refreshComplete();
                                break;
                        }
                    }
                });

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, jingb_listview, header);
            }
        });*/
    }


}
