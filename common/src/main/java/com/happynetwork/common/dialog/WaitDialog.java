package com.happynetwork.common.dialog;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.R;


/**
 * @author Tom.yuan
 */
public class WaitDialog{
    private static Dialog loadingDialog;

    public static void WaitDialog(Activity context) {
        if(loadingDialog == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.xf_common_customprogressdialog, null);// 得到加载view
            AnimationDrawable drawable = (AnimationDrawable)((ImageView)v.findViewById(R.id.loadingImageView)).getDrawable();
            drawable.start();
            loadingDialog = new Dialog(context, R.style.Xf_common_NetDialog);// 创建自定义样式dialog
            loadingDialog.setContentView(v);// 设置布局
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void show(){
        LogUtils.i(" show "+loadingDialog);
        if(loadingDialog != null){
            try {
                loadingDialog.show();
            }catch (Exception e){
                LogUtils.w(e.toString());
            }
        }
    }

    public static void dismiss(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            try {
                loadingDialog.dismiss();
            }catch (Exception e){
                LogUtils.w(e.toString());
            }
            loadingDialog = null;
        }
    }

    public static void hide(){
        if(loadingDialog != null){
            loadingDialog.hide();
        }
    }

    public static boolean isShowing(){
        if(loadingDialog != null){
            return loadingDialog.isShowing();
        }
        return false;
    }

    public static void setCancelable(boolean isCancel){
        if(loadingDialog != null){
            loadingDialog.setCancelable(isCancel);
        }
    }

    public static void setOnCancelListener(DialogInterface.OnCancelListener l){
        if(loadingDialog != null){
            loadingDialog.setOnCancelListener(l);
        }
    }
}
