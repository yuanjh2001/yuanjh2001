package com.happynetwork.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.happynetwork.common.events.HomeWatcher;
import com.happynetwork.common.services.BaseService;
import com.happynetwork.common.utils.LogUtils;

public class WindowManagerBaseLayout extends FrameLayout implements HomeWatcher.OnHomePressedListener{
    public WindowManager wManager;
    public WindowManager.LayoutParams wmParams;
    public View mContentView;
    public Activity mContext;
    public boolean isShow = false;
    private boolean isRemov = false;

    @Override
    public void onHomePressed() {
        LogUtils.i("点HOME键");
        hide();
    }

    @Override
    public void onHomeLongPressed() {
        LogUtils.i("长按HOME键");
        hide();
    }

    @Override
    public void onScreenOn() {
        LogUtils.i("点亮");
        if(mContext != null){
            Intent intent1 = new Intent(BaseService.APPOPEN_MESSAGE_RECEIVED_ACTION);
            mContext.sendBroadcast(intent1);
        }
    }

    @Override
    public void onScreenOff() {
        LogUtils.i("锁屏");
        hide();
    }

    @Override
    public void onUserPresent() {
        LogUtils.i("解锁");
        if(mContext != null){
            Intent intent1 = new Intent(BaseService.APPOPEN_MESSAGE_RECEIVED_ACTION);
            mContext.sendBroadcast(intent1);
        }
    }

    /**
     * @param context
     */
    public WindowManagerBaseLayout(Activity context) {
        this(context, null);
        mContext = context;
    }

    /**
     * @param context
     * @param attrs
     */
    public WindowManagerBaseLayout(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public WindowManagerBaseLayout(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public boolean isShowing(){
        return isShow;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public void setwManager(WindowManager windowManager) {
        if(windowManager != null)this.wManager = windowManager;
        if (this.wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            //设置图片格式，效果为背景透明
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            wmParams.gravity = Gravity.CENTER | Gravity.CENTER;
            wmParams.token = mContext.getWindow().getDecorView().findViewById(android.R.id.content).getWindowToken();
            // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
            wmParams.x = 0;
            wmParams.y = 0;
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        if (this.wManager == null) {
            wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        initView();
    }

    public void initView() {
        wManager.addView(this, wmParams);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
        isShow = true;
        invalidate();
    }

    public void hide() {
        this.setVisibility(View.GONE);
        isShow = false;
        invalidate();
    }


    public void remove() {
        if (!isRemov && this != null && wManager != null) {
            this.removeAllViews();
            wManager.removeView(this);
            isRemov = true;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                LogUtils.i("---doback---");
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
