package com.happynetwork.vrestate.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Tom.yuan on 2016/12/26.
 * pager 适配器
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> list;

    public ViewPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(list.get(position));
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position));
    }

}
