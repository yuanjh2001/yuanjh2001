package com.happynetwork.common.dialog;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.happynetwork.common.R;
import com.happynetwork.common.adapters.ViewPhotoGroupAdapter;
import com.happynetwork.common.events.ViewPhotoGroupClickEvent;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.common.vo.ImageBean;
import com.happynetwork.common.vo.LocalImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 相册分组列表
 */
public class ViewPhotoGroupLayout extends BaseWindowManagerLayout {
    private ImageButton backBtn;
    private ViewPhotoGroupClickEvent viewPhotoGroupClickEvent;

    private final static int SCAN_OK = 1;// 扫描完成
    private ViewPhotoGroupAdapter adapter;// 适配器
    private ListView mGroupGridView;// 展示扫描图片
    private List<LocalImageBean> localImageAllList;//总数据

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 扫描结束
                case SCAN_OK:
                    // 将扫描的所有图片放入适配器中
                    if (localImageAllList.size() != 0) {
                        adapter = new ViewPhotoGroupAdapter(mContext, localImageAllList, mGroupGridView);
                        mGroupGridView.setAdapter(adapter);
                    } else {
                        ToastUtils.createToast(mContext, "亲，您的相册没有图片哦");
                    }
                    break;
            }
        }

    };

    public void setViewPhotoGroupClickEvent(ViewPhotoGroupClickEvent viewPhotoGroupClickEvent) {
        this.viewPhotoGroupClickEvent = viewPhotoGroupClickEvent;
    }

    /**
     * @param context
     */
    public ViewPhotoGroupLayout(Activity context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ViewPhotoGroupLayout(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPhotoGroupLayout(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initView() {
        wmParams.gravity = Gravity.CENTER | Gravity.CENTER;
        wmParamsParent.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParamsParent.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.xf_common_viewphotogroup_floatwin, this);
        try {
            wManager.addView(this, wmParamsParent);
        }catch (Exception e){
            LogUtils.w(e.toString());
        }
        this.setClickable(true);
        invalidate();

        backBtn = (ImageButton) mContentView.findViewById(R.id.back_id);
        backBtn.setOnClickListener(new MyOnClickListener());
        localImageAllList = new ArrayList<LocalImageBean>();
        mGroupGridView = (ListView) mContentView.findViewById(R.id.gv_circlefriends_scannerimage_gridview);
        getImages();

        mGroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                LocalImageBean bean = (LocalImageBean) parent.getItemAtPosition(position);
                LogUtils.i(bean.toString());
                viewPhotoGroupClickEvent.submitSelect(bean);
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.back_id) {
                viewPhotoGroupClickEvent.backClick();
            }
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.createToast(mContext, "暂无外部存储");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mContext.getContentResolver();

                // 查询的字段
                String[] projection = {MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, projection,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                List<ImageBean> imagesList = null;
                LocalImageBean localImageBean = null;
                HashMap<String, LocalImageBean> localImageMap = new HashMap<String, LocalImageBean>();
                ImageBean imageBean;
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    // 获取该图片的父路径名
                    String parentName = new File(path).getParentFile()
                            .getName();
                    if (!localImageMap.containsKey(parentName)) {
                        //不是一个文件夹数据
                        localImageBean = new LocalImageBean();
                        localImageBean.setGroupName(parentName);
                        imagesList = new ArrayList<ImageBean>();
                        imageBean = new ImageBean();
                        imageBean.setPath(path);
                        imagesList.add(imageBean);
                        localImageBean.setLocalImageList(imagesList);
                        localImageMap.put(parentName, localImageBean);
                    } else {
                        //是同一个文件夹
                        localImageBean = localImageMap.get(parentName);
                        imagesList = localImageBean.getLocalImageList();
                        imageBean = new ImageBean();
                        imageBean.setPath(path);
                        imagesList.add(imageBean);
                    }
                }

                mCursor.close();
                Iterator iter = localImageMap.keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    LocalImageBean value = localImageMap.get(key);
                    localImageAllList.add(value);
                }
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_OK);
            }
        }).start();
    }

    @Override
    public void doBack(KeyEvent event) {
        super.doBack(event);
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            viewPhotoGroupClickEvent.backClick();
        }
    }
}
