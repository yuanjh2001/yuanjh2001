package com.happynetwork.common.utils;

import android.app.Activity;
import android.app.Application;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happynetwork.common.R;

/**
 * @author Tom.yuan
 */
public final class ToastUtils {
    private static View toastLayout;
    private static ImageView imageView;
    private static TextView textView;
    private static Toast toast;

    public static void createToast(Application c, String str, boolean showIm,int gravity){
        if(str == null || str.trim().equals("") || !Tools.isForeground(c)){
            if(!Tools.isForeground(c)){
                LogUtils.w("str is background");
            }else {
                LogUtils.w("str is null");
            }
            return;
        }
        if(toastLayout == null){
            initToast(c);
        }
        try {
            textView.setText(str);
            if(showIm){
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(gravity,0,0);
            toast.show();
        }catch (Exception e){
            LogUtils.w(e.toString());
        }
    }

    public static void createToast(Application c, String str, boolean showIm){
        createToast(c,str,showIm,Gravity.CENTER);
    }

    public static void createToast(Activity c, String str, boolean showIm){
        if(c != null){
            createToast(c.getApplication(),str,showIm,Gravity.CENTER);
        }
    }

    public static void createToast(Application c, String str){
        createToast(c,str,false);
    }

    public static void createToast(Activity c, String str){
        if(c != null){
            createToast(c.getApplication(),str,false);
        }
    }

    private static void initToast(Application c){
        try {
            toastLayout = LayoutInflater.from(c).inflate(R.layout.xf_common_mytoast_floatwin, null);
            textView = (TextView) toastLayout.findViewById(R.id.tvForToast);
            imageView = (ImageView)toastLayout.findViewById(R.id.im_id);
            toast = Toast.makeText(c,"",Toast.LENGTH_LONG);
            toast.setView(toastLayout);
        }catch (Exception e){
            LogUtils.w(e.toString());
        }
    }

    public static void cancelToast() {
        if (toastLayout != null) {
            try {
                toast.cancel();
            }catch (Exception e){
                LogUtils.w(e.toString());
            }
            toastLayout = null;
            toast = null;
        }
    }
}
