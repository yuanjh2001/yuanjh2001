package com.happynetwork.vrestate.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.happynetwork.vrestate.R;

/**
 * 发布应邀
 */
public class InvitedActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout inviteScopeLayout;
    private RelativeLayout toolbar;

    private TextView tvCommonTitle;
    private ImageView rlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_invited);

        inviteScopeLayout = (RelativeLayout) findViewById(R.id.invite_scope_layout);
        inviteScopeLayout.setOnClickListener(this);
        setTitleName(getString(R.string.invited));
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
            default:
                break;
        }

    }
}
