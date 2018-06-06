package com.happynetwork.vrestate.activitys;

import android.os.Bundle;

import com.happynetwork.vrestate.R;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentViewBelowTitleBar(R.layout.activity_setting);
        setTitleName(getString(R.string.setting_tv));
        showBack();
    }
}
