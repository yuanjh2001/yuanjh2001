package com.happynetwork.vrestate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happynetwork.vrestate.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Tom.yuan on 2016/12/27.
 * 分享
 */
public class Share_Fragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_share, null);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Share_Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Share_Fragment");
    }


}
