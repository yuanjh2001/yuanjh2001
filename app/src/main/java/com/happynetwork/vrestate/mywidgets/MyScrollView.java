package com.happynetwork.vrestate.mywidgets;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.happynetwork.common.utils.LogUtils;

/**
 * Created by Tom.yuan on 2017/1/17.
 */

public class MyScrollView extends ScrollView {
    //下拉
    //用于记录下拉位置
    private float y = 0f;
    //zoomView原本的宽高
    private int zoomViewWidth = 0;
    private int zoomViewHeight = 0;
    //是否正在放大
    private boolean mScaling = false;
    //放大的view，默认为第一个子view
    private View zoomView;
    //滑动放大系数，系数越大，滑动时放大程度越大
    private float mScaleRatio = 0.4f;
    //最大的放大倍数
    private float mScaleTimes = 2f;
    //回弹时间系数，系数越小，回弹越快
    private float mReplyRatio = 0.5f;
    private OnScrollListener onScrollListener;

    //上拉
    //拖动的距离 size = 4 的意思 只允许拖动屏幕的1/4
    private static final int size = 4;
    private View inner;
    private float y1;
    private float nowY;
    //是否是上拉状态
    private boolean mPull = false;
    private Rect normal = new Rect();

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setZoomView(View zoomView) {
        this.zoomView = zoomView;
    }

    public void setmScaleRatio(float mScaleRatio) {
        this.mScaleRatio = mScaleRatio;
    }

    public void setmScaleTimes(int mScaleTimes) {
        this.mScaleTimes = mScaleTimes;
    }

    public void setmReplyRatio(float mReplyRatio) {
        this.mReplyRatio = mReplyRatio;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //不可过度滚动，否则上移后下拉会出现部分空白的情况
        setOverScrollMode(OVER_SCROLL_NEVER);
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        //获得默认第一个view
        if (inner != null && inner instanceof ViewGroup && zoomView == null) {
            ViewGroup vg = (ViewGroup) inner;
            if (vg.getChildCount() > 0) {
                zoomView = vg.getChildAt(0);
            }
        }
        //LogUtils.i(zoomView.toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            zoomViewWidth = zoomView.getMeasuredWidth();
            zoomViewHeight = zoomView.getMeasuredHeight();
        }
        if (zoomView == null || zoomViewWidth <= 0 || zoomViewHeight <= 0 || inner == null) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        y = ev.getY();//滑动到顶部时，记录位置
                        normal.setEmpty();
                    } else {
                        //上拉
                        nowY = ev.getY();
                        int deltaY = (int) (y1 - nowY) / size;
                        y1 = nowY;
                        // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                        if (isNeedMove()) {
                            mPull = true;
                            if (normal.isEmpty()) {
                                // 保存正常的布局位置
                                normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                            }
                        }
                        if(mPull){
                            int yy = inner.getTop() - deltaY;
                            if(yy >= normal.top){
                                mPull = false;
                                inner.layout(normal.left, normal.top, normal.right, normal.bottom);
                                normal.setEmpty();
                                break;
                            }
                            // 移动布局
                            inner.layout(inner.getLeft(), yy, inner.getRight(), inner.getBottom() - deltaY);
                        }
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - y) * mScaleRatio);
                if (distance < 0) {
                    break;
                }
                mScaling = true;
                setZoom(distance);
                return true;
            case MotionEvent.ACTION_UP:
                mScaling = false;
                mPull = false;
                replyView();
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                y1 = ev.getY();
                mScaling = false;
                mPull = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 放大view
     */
    private void setZoom(float s) {
        float scaleTimes = (float) ((zoomViewWidth + s) / (zoomViewWidth * 1.0));
        //如超过最大放大倍数，直接返回
        if (scaleTimes > mScaleTimes) return;

        ViewGroup.LayoutParams layoutParams = zoomView.getLayoutParams();
        layoutParams.width = (int) (zoomViewWidth + s);
        layoutParams.height = (int) (zoomViewHeight * ((zoomViewWidth + s) / zoomViewWidth));
        //设置控件水平居中
        //((MarginLayoutParams) layoutParams).setMargins(-(layoutParams.width - zoomViewWidth) / 2, 0, 0, 0);
        zoomView.setLayoutParams(layoutParams);
    }

    /**
     * 下拉回弹
     */
    private void replyView() {
        final float distance = zoomView.getMeasuredWidth() - zoomViewWidth;
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(distance, 0.0F).setDuration((long) (distance * mReplyRatio));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoom((Float) animation.getAnimatedValue());
            }
        });
        anim.start();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) onScrollListener.onScroll(l, t, oldl, oldt);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 滑动监听
     */
    public interface OnScrollListener {
        void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }


    // 上拉开启动画移动
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(), normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        //LogUtils.i(getHeight() +" | "+inner.getMeasuredHeight()+" | "+scrollY);
        if (scrollY == offset) {
            return true;
        }
        return false;
    }
}
