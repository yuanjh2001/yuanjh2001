package com.happynetwork.vrestate.mywidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Tom.yuan on 2017/1/16.
 */

public class DLVideoView extends VideoView {

    public DLVideoView(Context context)
    {
        super(context);
    }

    public DLVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
