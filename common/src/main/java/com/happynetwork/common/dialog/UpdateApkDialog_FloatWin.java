package com.happynetwork.common.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.happynetwork.common.CommonSdk;
import com.happynetwork.common.events.Float_UpdateApkDialogClickEvent;
import com.happynetwork.common.R;

/**
 * 自动更新弹框，系统弹窗方式
 */
public class UpdateApkDialog_FloatWin extends WindowManagerBaseLayout {
    private DialogInterface.OnCancelListener listener;
    private Float_UpdateApkDialogClickEvent float_updateApkDialogClickEvent;
    private boolean cancelable = false;
    private Button installBtn,cancelBtn;
    private TextView update_tip;
    private boolean isCompulsoryUpdate = false;

    public void setCompulsoryUpdate(boolean compulsoryUpdate) {
        isCompulsoryUpdate = compulsoryUpdate;
    }

    public void setFloat_updateApkDialogClickEvent(Float_UpdateApkDialogClickEvent float_updateApkDialogClickEvent) {
        this.float_updateApkDialogClickEvent = float_updateApkDialogClickEvent;
    }

    public Float_UpdateApkDialogClickEvent getCallBack(){
        return new Float_UpdateApkDialogClickEvent() {
            @Override
            public void updateApk(boolean closeWin) {

            }

            @Override
            public void closeWin() {

            }

            @Override
            public void updateState(int i) {
                if(update_tip != null ){
                    if(i != 1000){
                        update_tip.setText("下载"+i+"%");
                    }else {
                        hide();
                    }
                }
            }
        };
    }

    /**
     * @param context
     */
    public UpdateApkDialog_FloatWin(Activity context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public UpdateApkDialog_FloatWin(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpdateApkDialog_FloatWin(Activity context, AttributeSet attrs, int defStyle) {
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
        wmParams.gravity = Gravity.CENTER;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.xf_common_updateapk_floatwin, this);
        wmParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wmParams.dimAmount = 0.5f;
        wManager.addView(this, wmParams);
        installBtn = (Button)mContentView.findViewById(R.id.install_id);
        cancelBtn = (Button)mContentView.findViewById(R.id.close_id);
        update_tip = (TextView)mContentView.findViewById(R.id.update_tip_id);
        installBtn.setOnClickListener(new MyOnClickListener());
        cancelBtn.setOnClickListener(new MyOnClickListener());
        if(isCompulsoryUpdate){
            cancelBtn.setVisibility(GONE);
            cancelable = false;
            update_tip.setText("请更新版本，否则无法继续！");
            invalidate();
        }
        show();
    }

    class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.install_id) {
                if(isCompulsoryUpdate){
                    if (float_updateApkDialogClickEvent != null)float_updateApkDialogClickEvent.updateApk(false);
                    update_tip.setText("下载中,请耐心等待……！");
                }else {
                    if (float_updateApkDialogClickEvent != null)float_updateApkDialogClickEvent.updateApk(true);
                    remove();
                }
            }else if(id == R.id.close_id){
                if (float_updateApkDialogClickEvent != null)float_updateApkDialogClickEvent.closeWin();
                remove();
            }
        }
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
    public void hide() {
        super.hide();
        doFinish();
    }

    public void doFinish(){
        if(isCompulsoryUpdate && CommonSdk.xf_common_exitApp != null){
            if (float_updateApkDialogClickEvent != null)float_updateApkDialogClickEvent.closeWin();
            remove();
            exitHandler.sendEmptyMessage(0);
        }
    }

    private Handler exitHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(CommonSdk.xf_common_exitApp != null)CommonSdk.xf_common_exitApp.exitUpdate();
        }
    };

    @Override
    public void doBack(KeyEvent event) {
        super.doBack(event);
        if(isCompulsoryUpdate){
            doFinish();
        }else {
            onBackPressed();
        }
    }
}
