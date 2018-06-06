package com.happynetwork.vrestate.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happynetwork.common.services.TimerService;
import com.happynetwork.common.utils.LogUtils;

/**
 * Created by Tom.yuan on 2016/12/27.
 */
public class BaseFragment extends Fragment {
    protected Context mContext;
    public View rootView;
    private MyMessageReceiver mMessageReceiver;
    public boolean hasPaused = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        regist();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    /**
     * 打开activity * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(mContext, ActivityClass);
        startActivity(intent);
    }

    /**
     * 打开activity * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(mContext, ActivityClass);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void regist() {
        mMessageReceiver = new MyMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(TimerService.TIMER_MESSAGE_RECEIVED_ACTION);
        getContext().registerReceiver(mMessageReceiver, filter);
    }

    class MyMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(TimerService.TIMER_MESSAGE_RECEIVED_ACTION.equals(action)){
                timerUpdate();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hasPaused = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        hasPaused = true;
    }

    protected void timerUpdate(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("onActivityResult====================");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mMessageReceiver != null){
            getContext().unregisterReceiver(mMessageReceiver);
            mMessageReceiver = null;
        }
    }
}
