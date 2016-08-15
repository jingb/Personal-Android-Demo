package com.jingb.application.ninegag.imageload.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jingb.application.Jingb;
import com.jingb.application.R;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SlidePageFragment extends BaseFragment {

    private static final String DATA = "slidepagefragment.picurl";

    private static boolean mPicFromNetWork;

    public static SlidePageFragment newInstance(boolean picFromNetWork, @NonNull final String data) {
        mPicFromNetWork = picFromNetWork;

        Bundle arguments = new Bundle();
        arguments.putString(DATA, data);

        SlidePageFragment fragment = new SlidePageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Bind(R.id.tv_introduction)
    TextView tv_introduction;

    String mData;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_slide_page, container, false);
        ButterKnife.bind(this, rootView);

        Bundle arguments = getArguments();

        if (arguments != null) {
            mData = arguments.getString(DATA);
        }
        if (!mPicFromNetWork) {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                Logger.e(e, e.getMessage());
            }
            doAnimation();
            tv_introduction.setText(mData);
        } else {
            /*SimpleDraweeView view = (SimpleDraweeView) rootView.findViewById(R.id.pic);
            if (mData != null) {
                view.setImageURI(Uri.parse(mData));
            }*/
        }
        return rootView;
    }

    public void doAnimation() {
        YoYo.with(Techniques.FadeInRight).
                duration(Jingb.COMMON_ANIMATION_DURATION).
                playOn(tv_introduction);
    }

}
