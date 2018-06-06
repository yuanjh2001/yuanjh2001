package com.happynetwork.common.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.happynetwork.common.R;
import com.happynetwork.common.vo.ImageBean;
import com.happynetwork.common.vo.LocalImageBean;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;

public class ViewPhotoGroupAdapter extends BaseAdapter {
    private List<LocalImageBean> list;
    private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
    private ListView mGridView;
    protected LayoutInflater mInflater;
    private Context context;
    private ImageLoader imageLoader;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public ImageLoader getImLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPoolSize(1)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .memoryCache(new WeakMemoryCache())
                    .build();
            imageLoader.init(config);
        }
        return imageLoader;
    }

    public ViewPhotoGroupAdapter(Context context, List<LocalImageBean> localImageAllList, ListView mGroupGridView) {
        this.list = localImageAllList;
        this.mGridView = mGroupGridView;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        imageLoader = getImLoader();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LocalImageBean mImageBean = list.get(position);
        String path;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.xf_common_viewphoto_grid_group_item, null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.gv_circlefriends_scanimagegroup_image);
            viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.tv_circlefriends_scanimagegroup_title);
            viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.tv_circlefriends_scanimagegroup_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mImageView.setImageResource(R.drawable.xf_common_friends_sends_pictures_no);
        }
        List<ImageBean> list = mImageBean.getLocalImageList();
        viewHolder.mTextViewTitle.setText(mImageBean.getGroupName());
        viewHolder.mTextViewCounts.setText("（"+Integer.toString(mImageBean.getLocalImageList().size()) + "）");
        if (position < list.size()) {
            path = list.get(position).getPath();
            if(path != null && !path.trim().equals("")){
                imageLoader.displayImage("file://" + path, viewHolder.mImageView);
            }
        } else {
            viewHolder.mImageView.setImageResource(R.drawable.xf_common_friends_sends_pictures_no);
        }

        return convertView;
    }

    public static class ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewCounts;
    }

}
