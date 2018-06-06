package com.happynetwork.common.dialog;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.happynetwork.common.R;
import com.happynetwork.common.adapters.ViewPhotoAdapter;
import com.happynetwork.common.events.ViewPhotoClickEvent;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.vo.ImageBean;
import com.happynetwork.common.vo.LocalImageBean;

/**
 * 照片列表
 */
public class ViewPhotoLayout extends BaseWindowManagerLayout {
    private ImageButton backBtn;
    private GridView mGridView;

    public LocalImageBean getImageBean() {
        return imageBean;
    }

    private LocalImageBean imageBean;//文件夹图片总数据
    private ViewPhotoAdapter adapter;//本地图片展示Adapter
    private ViewPhotoClickEvent viewPhotoClickEvent;

    public void setImageBean(LocalImageBean bean){
        this.imageBean = bean;
        getImages();
    }

    public void setViewPhotoClickEvent(ViewPhotoClickEvent viewPhotoClickEvent){
        this.viewPhotoClickEvent = viewPhotoClickEvent;
    }

    /**
     * @param context
     */
    public ViewPhotoLayout(Activity context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ViewPhotoLayout(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPhotoLayout(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initView() {
        wmParams.gravity = Gravity.CENTER | Gravity.CENTER;
        wmParamsParent.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParamsParent.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.xf_common_viewphoto_floatwin, this);
        try {
            wManager.addView(this, wmParamsParent);
        }catch (Exception e){
            LogUtils.w(e.toString());
        }
        this.setClickable(true);
        invalidate();

        backBtn = (ImageButton)mContentView.findViewById(R.id.back_id);
        backBtn.setOnClickListener(new MyOnClickListener());

    }

    private void getImages() {
        mGridView = (GridView) findViewById(R.id.gv_photosingle_checked_childgrid);
        // 点击选择图片
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                ImageBean bean = (ImageBean) parent.getItemAtPosition(position);
                viewPhotoClickEvent.submitSelect(bean);
            }
        });
        adapter = new ViewPhotoAdapter(mContext, imageBean);
        mGridView.setAdapter(adapter);
    }

    class MyOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.back_id) {
                viewPhotoClickEvent.backClick();
            }
        }
    }

    @Override
    public void doBack(KeyEvent event) {
        super.doBack(event);
        int action = event.getAction();
        if(action == KeyEvent.ACTION_DOWN){
            viewPhotoClickEvent.backClick();
        }
    }
}
