package com.happynetwork.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.happynetwork.common.utils.LogUtils;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 用户图像悬浮框
 */
public class BaseWindowManagerLayout extends FrameLayout {
    public WindowManager wManager;
    public WindowManager.LayoutParams wmParams;
    public WindowManager.LayoutParams wmParamsParent;
    public View mContentView;
    public Activity mContext;
    private ImageLoader imageLoader;

    /**
     * @param context
     */
    public BaseWindowManagerLayout(Activity context) {
        this(context, null);
        mContext = context;
    }

    /**
     * @param context
     * @param attrs
     */
    public BaseWindowManagerLayout(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public BaseWindowManagerLayout(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public void setwManager(WindowManager windowManager,int t){
        this.wManager = windowManager;
        if (this.wmParamsParent == null) {
            wmParamsParent = new WindowManager.LayoutParams();
            wmParamsParent.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            wmParamsParent.token = mContext.getWindow().getDecorView().findViewById(android.R.id.content).getWindowToken();
            //设置图片格式，效果为背景透明
            wmParamsParent.format = PixelFormat.RGBA_8888;
            //wmParamsParent.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            wmParamsParent.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParamsParent.gravity = Gravity.LEFT | Gravity.TOP;
            // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
            wmParamsParent.width = WindowManager.LayoutParams.MATCH_PARENT;
            wmParamsParent.height = WindowManager.LayoutParams.MATCH_PARENT;
            wmParamsParent.x = 0;
            wmParamsParent.y = t;
        }
        if (this.wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            wmParams.token = mContext.getWindow().getDecorView().findViewById(android.R.id.content).getWindowToken();
            //设置图片格式，效果为背景透明
            wmParams.format = PixelFormat.RGBA_8888;
            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            wmParams.gravity = Gravity.LEFT | Gravity.CENTER;
            // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
            wmParams.x = 0;
            wmParams.y = t;
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //wmParams.token = GameSdk.ib;
        }
        if (this.wManager == null) {
            wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        initView();
    }

    public void setwManager(WindowManager windowManager) {
        setwManager(windowManager,0);
    }

    public void initView() {
        wManager.addView(this, wmParams);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.setVisibility(View.GONE);
    }

    public synchronized void remove() {
        if (this != null && wManager != null) {
            try {
                wManager.removeView(this);
                this.removeAllViews();
            }catch (Exception e){
                LogUtils.w(e.toString());
            }
        }
    }

    protected void update(Activity context) {

    }

    public ImageLoader getImLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                doBack(event);
                break;
            default:
                doOther(event);
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public void doBack(KeyEvent event) {

    }

    public void doOther(KeyEvent event) {

    }

}
