package com.happynetwork.vrestate.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.happynetwork.vrestate.R;


/**
 * 
 * @author Tom.yuan 2016-9-26 下午4:20:47 联系管家弹出框
 */
public class ContactManagerActivity extends Activity implements OnClickListener{
    private ImageView close_img;
    private LinearLayout contact_manager_layout;
    public static final String ACTION_CALL_PRIVILEGED = "android.intent.action.CALL_PRIVILEGED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_manager);
        close_img = (ImageView) findViewById(R.id.close_img);
        close_img.setOnClickListener(this);
        contact_manager_layout = (LinearLayout) findViewById(R.id.contact_manager_layout);
        contact_manager_layout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.close_img:
            this.finish();
            break;
        case R.id.contact_manager_layout:
            Intent intent = new Intent();
            intent.setAction("android.intent.action.CALL");
            intent.setData(Uri.parse("tel:13126733579"));//蓝色固定
            startActivity(intent);
            break;
        default:
            break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
