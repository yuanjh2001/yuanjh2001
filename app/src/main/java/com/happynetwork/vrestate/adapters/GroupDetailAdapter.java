package com.happynetwork.vrestate.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.beans.GroupDetailModel;
import com.happynetwork.vrestate.utils.BitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class GroupDetailAdapter extends BaseAdapter {
    
    private Context context;
    private ArrayList<GroupDetailModel> groupDetailModels;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public GroupDetailAdapter(Context context, ArrayList<GroupDetailModel> groupDetailModels, ImageLoader imageLoader, DisplayImageOptions options){
        this.context = context;
        this.groupDetailModels = groupDetailModels;
        this.imageLoader = imageLoader;
        this.options = options;
    }
    @Override
    public int getCount() {
        return groupDetailModels.size();
    }

    @Override
    public Object getItem(int position) {
        return groupDetailModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        HoldView holdView = null;
        if(convertView==null){
            holdView = new HoldView();
            View view = LayoutInflater.from(context).inflate(R.layout.group_detail_item, null);
            convertView = view;
            holdView.groupNumberHeadPic = (ImageView) convertView.findViewById(R.id.group_num_head);
            holdView.groupNumberName = (TextView) convertView.findViewById(R.id.nickname);
            convertView.setTag(holdView);
        }else{
            holdView = (HoldView) convertView.getTag();
        }

        GroupDetailModel groupDetailModel = groupDetailModels.get(position);
        String groupNumberName = groupDetailModel.groupNumberName;
        String headUrl = groupDetailModel.headUrl;
        holdView.groupNumberName.setText(groupNumberName);
        final ImageView headPic = holdView.groupNumberHeadPic;
        imageLoader.displayImage(headUrl, headPic, options, new ImageLoadingListener() {
            
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }
            
            @Override
            public void onLoadingFailed(String imageUri, View view,
                    FailReason failReason) {
            }
            
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                headPic.setImageBitmap(BitmapUtil.toRoundBitmap(loadedImage));
            }
            
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
        
        return convertView;
    }

    class HoldView{
        ImageView groupNumberHeadPic;
        TextView groupNumberName;
    }
}
