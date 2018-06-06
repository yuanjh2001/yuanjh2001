package com.happynetwork.common.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.happynetwork.common.utils.LogUtils;

/**
 * Created by Tom.yuan on 2016/9/22.
 */
public class HomeWatcher {
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier;

    /**
     * 回调接口
     */
    public interface OnHomePressedListener {
        /**
         * 短按home
         */
        public void onHomePressed();

        /**
         * 长按home
         */
        public void onHomeLongPressed();

        /**
         * 屏幕点亮
         */
        public void onScreenOn();

        /**
         * 锁屏
         */
        public void onScreenOff();

        /**
         * 解锁
         */
        public void onUserPresent();
    }

    public HomeWatcher(Context context) {
        mContext = context;
        mRecevier = new InnerRecevier();
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mFilter.addAction(Intent.ACTION_SCREEN_ON);
        mFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mFilter.addAction(Intent.ACTION_USER_PRESENT);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, mFilter);
            getScreenState();
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch() {
        if (mRecevier != null) {
            try {
                mContext.unregisterReceiver(mRecevier);
            }catch (Exception e){
                LogUtils.w(e.toString());
            }
        }
    }

    /**
     * 广播接收者
     */
    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_LOCK = "lock";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    LogUtils.i("action:" + action + ",reason:" + reason);
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            // 短按home键
                            mListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 长按home键
                            mListener.onHomeLongPressed();
                        }else if (reason.equals(SYSTEM_DIALOG_REASON_LOCK)) {
                            mListener.onScreenOff();
                        }
                    }
                }
            }else if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                mListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                mListener.onScreenOff();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                mListener.onUserPresent();
            }
        }
    }

    /**
     * 获取screen状态
     */
    private void getScreenState() {
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            if (mListener != null) {
                mListener.onScreenOn();
            }
        } else {
            if (mListener != null) {
                mListener.onScreenOff();
            }
        }
    }
}
