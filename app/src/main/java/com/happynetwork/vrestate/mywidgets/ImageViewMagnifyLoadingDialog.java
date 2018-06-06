package com.happynetwork.vrestate.mywidgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.happynetwork.vrestate.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 点击查看图片全屏放大，再次点击还原
 * Created by Tom.yuan on 2017/1/19.
 */
public class ImageViewMagnifyLoadingDialog extends Dialog{

    private ImageView imageView;// 显示文字

    private RelativeLayout layout;

    private String imagePath;// 图片路径

    public ImageViewMagnifyLoadingDialog(Context context, String imagePath) {
        super(context, R.style.magnifyDialogStyle);
        this.imagePath = imagePath;
    }

    public ImageViewMagnifyLoadingDialog(Context context, int themeResId, String imagePath) {
        super(context, themeResId);
        this.imagePath = imagePath;
    }

    protected ImageViewMagnifyLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener, String imagePath) {
        super(context, cancelable, cancelListener);
        this.imagePath = imagePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bigpic);
        imageView = (ImageView)findViewById(R.id.iv_magnify_imageview);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LayoutParams laParams=(LayoutParams)imageView.getLayoutParams();
        laParams.height=width;
        laParams.width=width;
        layout = (RelativeLayout)findViewById(R.id.rl_dialog_magnify);
        layout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        if(null != imagePath && !"".equals(imagePath))
        {
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).bitmapConfig(Config.RGB_565).build();
            String imageUrl = "" + imagePath;
            ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
//            imageView.setImageBitmap(ImageUtils.getDiskBitmap(imagePath));
        }
        else
        {
            imageView.setBackgroundResource(R.drawable.def_icon);
        }
    }
}
