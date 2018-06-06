package com.happynetwork.vrestate.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.happynetwork.vrestate.R;

/**
 * 发布邀约
 */
public class InviteActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout inviteScopeLayout,inviteAddressLayout;
    private RelativeLayout toolbar;

    private TextView tvCommonTitle;
    private ImageView rlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_invite);
        inviteScopeLayout = (RelativeLayout) findViewById(R.id.invite_scope_layout);
        inviteScopeLayout.setOnClickListener(this);
        inviteAddressLayout = (RelativeLayout) findViewById(R.id.invite_address_layout);
        inviteAddressLayout.setOnClickListener(this);
        setTitleName(getString(R.string.release_invite_info));
        showToolBar();
        showBack();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.invite_scope_layout:
                Intent intentScope = new Intent();
                intentScope.setClass(this, InviteScopeActivity.class);
                startActivity(intentScope);
                break;
            case R.id.invite_address_layout:
                Intent intentAddress = new Intent();
                intentAddress.setClass(this,InviteAddressListActivity.class);
                startActivity(intentAddress);
                break;
            default:
                break;
        }

    }
}
