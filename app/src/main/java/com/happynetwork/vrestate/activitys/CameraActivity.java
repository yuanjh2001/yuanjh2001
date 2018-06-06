package com.happynetwork.vrestate.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.happynetwork.common.events.CameraCompleteEvent;
import com.happynetwork.common.events.ClipBackEvent;
import com.happynetwork.common.events.DoUploadImgListener;
import com.happynetwork.common.fragments.FragmentCameraSdk;
import com.happynetwork.common.fragments.FragmentClipPic;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.listeners.UploadFileCallBack;
import com.happynetwork.vrestate.localdata.ApiList;
import com.happynetwork.vrestate.localdata.managers.UserServiceManager;

import java.io.File;

public class CameraActivity extends BaseActivity {
    private int currFragment = 0;
    private RelativeLayout contentLy;
    private FragmentCameraSdk cameraFragment;
    private FragmentClipPic fragmentClipPic;
    private String filePath;
    private boolean isFromCamera = false;
    private int[] sizs;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtils.i("onSaveInstanceState");
        outState.putInt("currFragment", currFragment);
        outState.putString("filePath", filePath);
        outState.putBoolean("isFromCamera",isFromCamera);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LogUtils.i("onRestoreInstanceState");
        currFragment = savedInstanceState.getInt("currFragment");
        filePath = savedInstanceState.getString("filePath");
        isFromCamera = savedInstanceState.getBoolean("isFromCamera");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_camera);
        hideToolBar();
        if (savedInstanceState != null) {
            currFragment = savedInstanceState.getInt("currFragment");
            filePath = savedInstanceState.getString("filePath");
            isFromCamera = savedInstanceState.getBoolean("isFromCamera");
            LogUtils.i(" currFragment "+currFragment +" filePath " +filePath);
            //清除fragment状态
            String FRAGMENTS_TAG = "Android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        contentLy = (RelativeLayout) findViewById(R.id.content_id);
        contentLy.setVisibility(View.GONE);
        sizs = getSenceSize();
        checkCameraPermission();
    }

    @Override
    protected void showCamera() {
        super.showCamera();
        checkStoragePermission();
    }

    @Override
    protected void showStorageAlert() {
        super.showStorageAlert();
        contentLy.setVisibility(View.VISIBLE);
        if (currFragment == 0) {
            toCamera();
        }else if(currFragment == 1){
            toClipPic(isFromCamera,filePath);
        }
    }

    public int[] getSenceSize() {
        int[] res = new int[2];
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        res[0] = metric.widthPixels;
        res[1] = metric.heightPixels;
        LogUtils.i("SenceSize: " + res[0] + " " + res[1] + " dpi: " + metric.densityDpi);
        return res;
    }

    public String getFilePath(){
        if(filePath == null || filePath.trim().equals("")){
            String dir = getCacheDir().getAbsolutePath();
            filePath = dir + System.currentTimeMillis()+".jpg";
            /*if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"xftemp"+File.separator;
                File dirfile = new File(dir);
                if (!dirfile.exists()) {
                    dirfile.mkdirs();
                }
                filePath = dir + System.currentTimeMillis()+".jpg";
            }else {
                ToastUtils.createToast(CameraActivity.this,"sd卡不可用！");
            }*/
        }
        return filePath;
    }

    public void toCamera() {
        cameraFragment = new FragmentCameraSdk();
        if(filePath == null)getFilePath();
        cameraFragment.setFilePath(filePath);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id,cameraFragment).commitAllowingStateLoss();
        cameraFragment.setSizs(sizs);
        cameraFragment.setCameraCompleteEvent(new CameraCompleteEvent() {
            @Override
            public void completeCamera(String path) {
                toClipPic(true,path);
            }
        });
        currFragment = 0;
    }

    public void toClipPic(boolean fromWhere,String path){
        filePath = path;
        isFromCamera = fromWhere;
        fragmentClipPic = new FragmentClipPic();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id, fragmentClipPic).commitAllowingStateLoss();
        fragmentClipPic.setSizs(sizs);
        fragmentClipPic.setFilePath(filePath,isFromCamera);
        fragmentClipPic.setClipBackEvent(new ClipBackEvent() {
            @Override
            public void doBack() {
                toCamera();
            }
        });
        fragmentClipPic.setDoUploadImgListener(new DoUploadImgListener() {
            @Override
            public void doUpload(String filePath) {
                UserServiceManager.getInstance().setContext(CameraActivity.this).setShowLoading(true).uploadFile(filePath, new UploadFileCallBack() {
                    @Override
                    public void uploadSuc(String url) {
                        Intent intent = new Intent("user_img_change");
                        Bundle b = new Bundle();
                        b.putString("imgurl",url);
                        intent.putExtras(b);
                        LocalBroadcastManager.getInstance(CameraActivity.this).sendBroadcast(intent);
                        CameraActivity.this.finish();
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
        currFragment = 1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && contentLy.getVisibility() == View.VISIBLE) {
            LogUtils.i("back=====");
            Intent intent = new Intent("back_message_received");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
