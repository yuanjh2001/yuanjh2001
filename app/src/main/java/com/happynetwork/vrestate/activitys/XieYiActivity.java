package com.happynetwork.vrestate.activitys;

import android.os.Bundle;

import com.happynetwork.vrestate.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户协议页
 */
public class XieYiActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentViewBelowTitleBar(R.layout.activity_xieyi);
        setTitleName("87870网络服务协议");
        showBack();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }
}
