package com.happynetwork.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.happynetwork.common.R;
import com.happynetwork.common.vo.ImageBean;
import com.happynetwork.common.vo.LocalImageBean;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;


public class ViewPhotoAdapter extends BaseAdapter {
	/**
	 * 用来存储图片的选中情况
	 */
	public List<ImageBean> list;// 此文件夹中的所有图片数据
	protected LayoutInflater mInflater;
	private Context context;
	int currentPosition;
	private ImageLoader imageLoader;
	public ViewPhotoAdapter(Context context,
			LocalImageBean imageBean) {
		this.context = context;
		this.list = imageBean.getLocalImageList();
		mInflater = LayoutInflater.from(context);
		imageLoader = getImLoader();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.xf_common_viewphoto_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.iv_photosingle_checked_adapter_image);
			viewHolder.mPosition = (TextView) convertView
					.findViewById(R.id.tv_photosingle_checked_adapter_position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageBean imageBean = list.get(position);
		viewHolder.mPosition.setText(position+"");
		imageBean.setPosition(position);
		String path = list.get(position).getPath();
		if(path != null && !path.trim().equals("")){
			imageLoader.displayImage("file://" + path, viewHolder.mImageView);
		}
		return convertView;
	}


	public static class ViewHolder {
		public TextView mPosition;
		public ImageView mImageView;
	}
}
