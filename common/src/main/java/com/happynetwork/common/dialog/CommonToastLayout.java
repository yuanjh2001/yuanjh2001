package com.happynetwork.common.dialog;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.common.R;

/**
 * 提示框
 */
public class CommonToastLayout extends WindowManagerBaseLayout {
    private ImageView imageView;
    private TextView textView;
    private long toastTime = 0l;
    private int showTime = 2200;

    /**
     * @param context
     */
    public CommonToastLayout(Activity context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CommonToastLayout(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToastLayout(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initView() {
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.gravity = Gravity.CENTER;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.xf_common_mytoast_floatwin, this);
        setClickable(true);
        wManager.addView(this, wmParams);
        invalidate();
        textView = (TextView) mContentView.findViewById(R.id.tvForToast);
        imageView = (ImageView)mContentView.findViewById(R.id.im_id);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("onClick");
                remove();
            }
        });
    }

    public void showToast(String text,boolean showIcon){
        textView.setText(text);
        toastTime = System.currentTimeMillis();
        if(showIcon){
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }
        handler.sendMessageDelayed(new Message(),showTime);
        invalidate();
    }

    public void showToast(String str){
        showToast(str,false);
    }

    public void setShowTime(int showTime){
        this.showTime = showTime;
        if(showTime == 0){
            remove();
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(System.currentTimeMillis() - toastTime >= showTime){
                ToastUtils.cancelToast();
            }else {
                handler.sendMessageDelayed(new Message(),showTime);
            }
        }
    };

    @Override
    public void doBack(KeyEvent event) {
        super.doBack(event);
        remove();
    }
}
