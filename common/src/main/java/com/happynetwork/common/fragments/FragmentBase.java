package com.happynetwork.common.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.happynetwork.common.nohttp.AuthImageDownloader;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yolanda.nohttp.error.NetworkError;

import java.lang.reflect.Field;

/**
 * 返回按键监听
 */
public class FragmentBase extends Fragment {
    private MessageReceiver mMessageReceiver;
    private ImageLoader imageLoader;
    public boolean isBacked = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("back_message_received");
        mMessageReceiver = new MessageReceiver();
        broadcastManager.registerReceiver(mMessageReceiver, filter);
    }

    public ImageLoader getImLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                    .threadPoolSize(1)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .memoryCache(new WeakMemoryCache())
                    .imageDownloader(new AuthImageDownloader(getActivity()))
                    .build();
            imageLoader.init(config);
        }
        return imageLoader;
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("back_message_received".equals(intent.getAction())) {
                LogUtils.i("back");
                doBack();
            }
        }
    }

    public void showToast(String s){
        ToastUtils.createToast(getActivity().getApplication(),s);
    }

    public void showToast(Exception exception){
        if (exception instanceof NetworkError) {// 网络不好
            ToastUtils.createToast(getActivity().getApplication(), "网络请求错误");
        }
    }

    public void doBack(){
        isBacked = true;
        ToastUtils.cancelToast();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i("************************************************** onDetach");
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.i("onDestroy");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
