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
import com.happynetwork.vrestate.beans.FocusModel;
import com.happynetwork.vrestate.utils.BitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @author Tom.yuan
 * 关注数据适配器
 * 2016-7-28
 * 下午2:48:21
 */
public class FocusAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FocusModel> focusModels;
    private ImageLoader imageLoader;  
    private DisplayImageOptions options;
    private final static String TOFOCUS = "0";//粉丝，即对方已经关注我
    private final static String MULT_FOCUS = "1";//双方均已经关注
    private final static String FOCUSED = "2";//被关注
    public FocusAdapter(Context context, ArrayList<FocusModel> focusModels, ImageLoader imageLoader, DisplayImageOptions options){
        this.context = context;
        this.focusModels = focusModels;
        this.imageLoader = imageLoader;
        this.options = options;
    }
    @Override
    public int getCount() {
        return focusModels.size();
    }

    @Override
    public Object getItem(int position) {
        return focusModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HoldView holdView;
        if(convertView==null){
            holdView = new HoldView();
            View view = LayoutInflater.from(context).inflate(R.layout.common_item, null);
            convertView = view;
            holdView.headImage = (ImageView) convertView.findViewById(R.id.head_img);
            holdView.name = (TextView) convertView.findViewById(R.id.name);
            holdView.range = (TextView) convertView.findViewById(R.id.range_value);
            holdView.focusStatus = (ImageView) convertView.findViewById(R.id.focus_status_img);
            convertView.setTag(holdView);
        }else{
            holdView = (HoldView) convertView.getTag();
        }
        
        FocusModel focusModel = focusModels.get(position);
        String headImageUrl = focusModel.headPicUrl;
        String name = focusModel.name;
        String range = focusModel.range;
        String focusStatus = focusModel.focusStatus;
        holdView.name.setText(name);
        holdView.range.setText(range);
        final ImageView headImageView = holdView.headImage;
        
        if("0".equals(focusStatus)){
            holdView.focusStatus.setImageResource(R.mipmap.mine_tofocus);
        }else if("1".equals(focusStatus)){
            holdView.focusStatus.setImageResource(R.mipmap.mine_focused);
            
        }else if("3".equals(focusStatus)){
            holdView.focusStatus.setVisibility(View.GONE);
        }
        else{
            holdView.focusStatus.setImageResource(R.mipmap.mine_multfocus);
        }
        imageLoader.displayImage(headImageUrl, headImageView, options, new ImageLoadingListener() {
            
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }
            
            @Override
            public void onLoadingFailed(String imageUri, View view,
                    FailReason failReason) {
            }
            
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                headImageView.setImageBitmap(BitmapUtil.toRoundBitmap(loadedImage));
            }
            
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
        
        
        return convertView;
    }

    class HoldView{
        ImageView headImage;
        ImageView focusStatus;
        TextView name;
        TextView range;
    }
}
