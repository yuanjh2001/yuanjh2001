package com.happynetwork.common.services;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.happynetwork.common.dialog.WaitDialog;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.common.utils.Tools;

/**
 * 全局timer
 */
public final class TimerService extends BaseService {
    public final static String TIMER_MESSAGE_RECEIVED_ACTION = "timer_message_received";
    private Application mContext;
    private int timer_interval = 1000;
    public boolean pressHome = false;

    @Override
    public void onHomePressed() {
        pressHome = true;
        ToastUtils.cancelToast();
        WaitDialog.hide();
    }

    @Override
    public void onHomeLongPressed() {
        pressHome = true;
        ToastUtils.cancelToast();
        WaitDialog.hide();
    }

    Handler updateHandler = new Handler(Looper.getMainLooper());

    Runnable updateRunable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(TIMER_MESSAGE_RECEIVED_ACTION);
            if (mContext == null) mContext = getApplication();
            mContext.sendBroadcast(intent);
            if (pressHome && Tools.isForeground(getApplication())) {
                Intent intent1 = new Intent(APPOPEN_MESSAGE_RECEIVED_ACTION);
                mContext.sendBroadcast(intent1);
                pressHome = false;
                WaitDialog.show();
            }
            updateHandler.postDelayed(updateRunable, timer_interval);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("=============onCreate");
        updateHandler.postDelayed(updateRunable, timer_interval);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("=============onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void timerUpdate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
