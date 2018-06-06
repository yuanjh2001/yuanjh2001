package com.happynetwork.common.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.happynetwork.common.CommonSdk;
import com.happynetwork.common.dialog.UpdateApkDialog_FloatWin;
import com.happynetwork.common.events.Float_UpdateApkDialogClickEvent;
import com.happynetwork.common.services.UpdateService;
import com.happynetwork.common.services.UpdateServiceForGameSdk;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.R;

public class UpdateApkActivity extends FragmentActivity {
    private static UpdateApkDialog_FloatWin updateApkDialog_floatWin;
    private Float_UpdateApkDialogClickEvent myUpdateApkDialogEvent;
    private UpdateService.SimpleBinder sBinder;

    private UpdateServiceForGameSdk.SimpleBinder sBinder_sdk;

    private boolean isCompulsoryUpdate = false;
    private String winType = "app";


    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            if (getWindow().getDecorView().findViewById(android.R.id.content).getWindowToken() == null) {
                handler.postDelayed(r, 30);
            } else {
                showUpdateApkDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xf_common_update_apk_activity);
        if (getIntent() != null && getIntent().getExtras() != null) {
            isCompulsoryUpdate = getIntent().getExtras().getBoolean("isCompulsoryUpdate");
            winType = getIntent().getExtras().getString("type");
        }
        if (savedInstanceState != null) {
            isCompulsoryUpdate = savedInstanceState.getBoolean("isCompulsoryUpdate");
            winType = savedInstanceState.getString("type");
        }

        handler.postDelayed(r, 30);
    }

    public void showUpdateApkDialog() {
        updateApkDialog_floatWin = new UpdateApkDialog_FloatWin(this);
        updateApkDialog_floatWin.setCompulsoryUpdate(isCompulsoryUpdate);
        updateApkDialog_floatWin.setwManager(null);
        updateApkDialog_floatWin.setFloat_updateApkDialogClickEvent(new MyUpdateApkDialogEvent());
        myUpdateApkDialogEvent = updateApkDialog_floatWin.getCallBack();
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sBinder = (UpdateService.SimpleBinder) service;
            sBinder.getService().setMyUpdateApkDialogEvent(myUpdateApkDialogEvent);
            sBinder.getService().downFile();
            unbindService(serviceConnection);
            LogUtils.i(" downfile--- " + sBinder.getService().toString());
        }
    };

    ServiceConnection serviceConnection_sdk = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sBinder_sdk = (UpdateServiceForGameSdk.SimpleBinder) service;
            sBinder_sdk.getService().setMyUpdateApkDialogEvent(myUpdateApkDialogEvent);
            sBinder_sdk.getService().downFile();
            unbindService(serviceConnection_sdk);
            LogUtils.i(" downfile--sdk- " + sBinder_sdk.getService().toString());
        }
    };

    class MyUpdateApkDialogEvent implements Float_UpdateApkDialogClickEvent {

        @Override
        public void updateApk(boolean closeWin) {
            if ("app".equals(winType)) {
                bindService(new Intent(UpdateApkActivity.this, UpdateService.class), serviceConnection, Context.BIND_AUTO_CREATE);
            } else if ("gamesdk".equals(winType)) {
                bindService(new Intent(UpdateApkActivity.this, UpdateServiceForGameSdk.class), serviceConnection_sdk, Context.BIND_AUTO_CREATE);
            }
            if (CommonSdk.umengEvent != null) CommonSdk.umengEvent.updateApk();
            if (updateApkDialog_floatWin != null && closeWin) {
                updateApkDialog_floatWin = null;
                finish();
            }
        }

        @Override
        public void closeWin() {
            if (updateApkDialog_floatWin != null) {
                updateApkDialog_floatWin = null;
            }

            if ("app".equals(winType)) {
                Intent intent = new Intent();
                intent.setClass(UpdateApkActivity.this, UpdateService.class);
                stopService(intent);
            } else if ("gamesdk".equals(winType)) {
                Intent intent = new Intent();
                intent.setClass(UpdateApkActivity.this, UpdateServiceForGameSdk.class);
                stopService(intent);
            }
            finish();
        }

        @Override
        public void updateState(int i) {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isCompulsoryUpdate",isCompulsoryUpdate);
        outState.putString("type",winType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateApkDialog_floatWin != null) {
            updateApkDialog_floatWin.remove();
            updateApkDialog_floatWin = null;
        }
    }
}
