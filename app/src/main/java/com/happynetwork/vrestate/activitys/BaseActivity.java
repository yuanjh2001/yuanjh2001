/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.happynetwork.vrestate.activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.happynetwork.common.activitys.CommonCheckPermissionsGrantActivity;
import com.happynetwork.common.nohttp.CallServer;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.vrestate.BaseApplication;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.widget.HXSDKHelper;
import com.happynetwork.vrestate.localdata.beans.UserInfo;
import com.happynetwork.vrestate.localdata.managers.UserServiceManager;

/**
 * @author Tom.yuan.
 */
public abstract class BaseActivity extends CommonCheckPermissionsGrantActivity{
    public boolean isLanscape = false;
    protected View contentView;
    private ImageView rl_back;
    private ImageView rl_commomsearch;
    private TextView tv_commontitle;
    private TextView rl_save;
    private LinearLayout ll_content,ll_content1;
    private RelativeLayout mToolbar;
    private MyMessageReceiver mMessageReceiver;

    private void regist() {
        mMessageReceiver = new MyMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        //filter.addAction(BaseApplication.SHOW_GAMESED_LOGIN);
        registerReceiver(mMessageReceiver, filter);
    }

    class MyMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*if (BaseApplication.SHOW_GAMESED_LOGIN.equals(action)) {
                LogUtils.i("-------show---login--------");

            }*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //不可横屏
        if (!isLanscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        regist();
    }

    protected void initToolBar() {
        rl_back = (ImageView) findViewById(R.id.rl_back);
        tv_commontitle = (TextView) findViewById(R.id.tv_commontitle);
        rl_commomsearch = (ImageView) findViewById(R.id.rl_commomsearch);
        rl_save = (TextView)findViewById(R.id.rl_save);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_content1 = (LinearLayout) findViewById(R.id.ll_content1);
        rl_back.setOnClickListener(new MyOnClickListener());
        rl_back.setVisibility(View.GONE);
        rl_commomsearch.setOnClickListener(new MyOnClickListener());
        rl_commomsearch.setVisibility(View.GONE);
        rl_save.setOnClickListener(new MyOnClickListener());
        rl_save.setVisibility(View.GONE);
        mToolbar = (RelativeLayout) findViewById(R.id.toolbar_ly_id);
    }

    protected void hideToolBar() {
        if (mToolbar != null) mToolbar.setVisibility(View.GONE);
    }

    protected void showToolBar(){
        if (mToolbar != null) mToolbar.setVisibility(View.VISIBLE);
    }

    /**
     * 显示返回按钮
     */
    public void showBack() {
        rl_back.setVisibility(View.VISIBLE);
    }

    /**
     * 显示保存按钮
     */
    public void showSave() {
        rl_save.setVisibility(View.VISIBLE);
    }

    /**
     * 显示搜索按钮
     */
    public void showSeach() {
        rl_commomsearch.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题栏名称
     *
     * @param title
     */
    public void setTitleName(String title) {
        tv_commontitle.setText(title);
    }

    /**
     * 加入页面内容布局
     *
     * @param layoutId
     */
    protected void addContentView(int layoutId) {
        initToolBar();
        ll_content1.setVisibility(View.GONE);
        ll_content.setVisibility(View.VISIBLE);
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (ll_content.getChildCount() > 0) {
            ll_content.removeAllViews();
        }
        if (contentView != null) {
            ll_content.addView(contentView);
        }
    }

    /**
     * 加入页面内容布局于标题栏下面
     *
     * @param layoutId
     */
    protected void addContentViewBelowTitleBar(int layoutId) {
        initToolBar();
        ll_content.setVisibility(View.GONE);
        ll_content1.setVisibility(View.VISIBLE);
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (ll_content1.getChildCount() > 0) {
            ll_content1.removeAllViews();
        }
        if (contentView != null) {
            ll_content1.addView(contentView);
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_back:
                    doBack();
                    break;
                case R.id.rl_save:
                    doSave();
                    break;
                case R.id.rl_commomsearch:
                    break;
            }
        }
    }

    protected void doBack(){
        finish();
    }

    protected void doSave(){

    }

    public void exitApp() {
        LogUtils.i("exitApp");
        BaseApplication.getInstance().isQuitApp = true;
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setClass(getApplicationContext(), WelcomeActivity.class);
//        startActivity(intent);
    }

    /**
     * 打开activity * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(this, ActivityClass);
        startActivity(intent);
    }

    protected void playVrVideo(){
        Intent it = new Intent();
        it.setClass(this,SimpleVrVideoActivity.class);
        it.setAction(Intent.ACTION_VIEW);
        //it.setData(Uri.parse("file:///sdcard/MIVR/video/437.mp4"));
        it.setData(Uri.parse("http://player-87870.oss-cn-hangzhou.aliyuncs.com/Act-m3u8-segment/16593212bbbf440abffb9f2b6648e6a7/001.m3u8"));
        Bundle b = new Bundle();
        b.putInt("inputFormat",2);
        //b.putInt("inputType",2);
        it.putExtras(b);
        startActivity(it);
    }

    /**
     * 打开activity * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(this, ActivityClass);
        intent.putExtras(b);
        startActivity(intent);
    }

    public boolean hasNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if ((mobNetInfo != null && mobNetInfo.isConnected())
                || (wifiNetInfo != null && wifiNetInfo.isConnected())
                || (activeInfo != null && activeInfo.isAvailable())) {
            LogUtils.i("has net");
            return true;
        } else {
            LogUtils.i("no net");
            return false;
        }
    }

    /**
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        LogUtils.d("Status height:" + height);
        return height;
    }

    /**
     * @return 底部状态栏高度
     */
    public int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        LogUtils.d("Navi height:" + height);
        return height;
    }

    @Override
    public void onBackPressed() {
        ToastUtils.cancelToast();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestInstance().cancelBySign(this);
        if (mMessageReceiver != null) {
            unregisterReceiver(mMessageReceiver);
            mMessageReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HXSDKHelper.getInstance().getNotifier().reset();
    }
}
