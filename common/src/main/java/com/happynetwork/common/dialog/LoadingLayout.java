package com.happynetwork.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.happynetwork.common.R;
import com.happynetwork.common.utils.LogUtils;

/**
 * 提示框
 */
public class LoadingLayout extends WindowManagerBaseLayout {
    private DialogInterface.OnCancelListener listener;
    private boolean cancelable = false;

    /**
     * @param context
     */
    public LoadingLayout(Activity context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingLayout(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener l){
        this.listener = l;
    }

    public void setCancelable(boolean cancelable){
        this.cancelable = cancelable;
    }

    public void onBackPressed() {
        if (cancelable) {
            cancel();
        }
    }

    @Override
    public void initView() {
        wmParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wmParams.dimAmount = 0.5f;
        wmParams.gravity = Gravity.CENTER | Gravity.CENTER;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.xf_common_customprogressdialog_floatwin, this);
        wManager.addView(this, wmParams);
        AnimationDrawable drawable = (AnimationDrawable)((ImageView)mContentView.findViewById(R.id.loadingImageView)).getDrawable();
        drawable.start();
        hide();
    }

    public void cancel() {
        if (!cancelable ) {
            cancelable = true;
        }
        if(listener != null){
            listener.onCancel(null);
        }
        remove();
    }

    @Override
    public void doBack(KeyEvent event) {
        super.doBack(event);
        LogUtils.i("doBack");
        onBackPressed();
    }
}
