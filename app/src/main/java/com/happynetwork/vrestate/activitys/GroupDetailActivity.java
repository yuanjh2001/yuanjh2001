package com.happynetwork.vrestate.activitys;

import java.util.ArrayList;

import com.happynetwork.vrestate.BaseApplication;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.adapters.GroupDetailAdapter;
import com.happynetwork.vrestate.beans.GroupDetailModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Tom.yuan
 * 群详情界面
 * 2016-8-10
 * 下午2:59:27
 */
public class GroupDetailActivity extends Activity implements OnClickListener{
    private GridView gridView;
    private GroupDetailAdapter groupDetailAdapter;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private TextView titleName;
    private ImageView more,back,social_edit_groupinfo;
    private RelativeLayout group_detail_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);
        gridView = (GridView) findViewById(R.id.group_number);
        imageLoader = ImageLoader.getInstance();
        options = BaseApplication.getInstance().getImgOps();
        more = (ImageView) findViewById(R.id.more);
        more.setOnClickListener(this);
        more.setVisibility(View.GONE);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        titleName = (TextView) findViewById(R.id.title_name);
        social_edit_groupinfo = (ImageView) findViewById(R.id.social_edit_groupinfo);
        social_edit_groupinfo.setOnClickListener(this);
        titleName.setText(R.string.default_nick_name);
        groupDetailAdapter = new GroupDetailAdapter(this, getGroupDetailModels(), imageLoader, options);
        gridView.setAdapter(groupDetailAdapter);
        group_detail_layout = (RelativeLayout) findViewById(R.id.group_detail_layout);
        group_detail_layout.setOnClickListener(this);
    }
    
    private ArrayList<GroupDetailModel> getGroupDetailModels() {
        ArrayList<GroupDetailModel> groupDetailModels = new ArrayList<GroupDetailModel>();
        for(int i=0;i<4;i++){
            GroupDetailModel groupDetailModel = new GroupDetailModel();
            groupDetailModel.groupNumberName = getString(R.string.default_nick_name);
            groupDetailModel.headUrl = "http://scimg.jb51.net/allimg/120704/2-120F41056220-L.jpg";
            groupDetailModels.add(groupDetailModel);
        }
        return groupDetailModels;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.more:
            
            break;
        case R.id.back:
            this.finish();
            break;
        case R.id.social_edit_groupinfo:
            Intent intent = new Intent();
            intent.setClass(this, GroupInfoEditActivity.class);
            startActivity(intent);
            break;
        case R.id.group_detail_layout:
            Intent intent_groupmember = new Intent();
            intent_groupmember.setClass(this, GroupMemberActivity.class);
            startActivity(intent_groupmember);
            break;

        default:
            break;
        }
    }
}
