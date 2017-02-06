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

import butterknife.Bind;
import butterknife.ButterKnife;

public class SlidePageFragment extends BaseFragment {

    private static final String DATA = "slidepagefragment.picurl";
    private static final String POSITION = "position";

    private static boolean mPicFromNetWork;

    private int mPosition;

    private boolean isSelected = false;

    public static SlidePageFragment newInstance(boolean picFromNetWork, @NonNull final String data, int position) {
        mPicFromNetWork = picFromNetWork;

        Bundle arguments = new Bundle();
        arguments.putString(DATA, data);
        arguments.putInt(POSITION, position);

        SlidePageFragment fragment = new SlidePageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Bind(R.id.tv_introduction)
    TextView tv_introduction;

    String mHintMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_slide_page, container, false);
        ButterKnife.bind(this, rootView);

        Bundle arguments = getArguments();

        if (arguments != null) {
            mHintMsg = arguments.getString(DATA);
            mPosition = arguments.getInt(POSITION);
        }
        if (mPicFromNetWork) {
            /*SimpleDraweeView view = (SimpleDraweeView) rootView.findViewById(R.id.pic);
            if (mHintMsg != null) {
                view.setImageURI(Uri.parse(mHintMsg));
            }*/
        } else {
            tv_introduction.setText(mHintMsg);
            if (0 == mPosition) {
                setIsSelected(true);
                doAnimation();
            }
        }
        return rootView;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public void doAnimation() {
        if (isSelected) {
            tv_introduction.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInRight).
                duration(Jingb.COMMON_ANIMATION_DURATION).
                playOn(tv_introduction);
        }
    }

    public void hideHintMsg() {
        tv_introduction.setVisibility(View.GONE);
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
