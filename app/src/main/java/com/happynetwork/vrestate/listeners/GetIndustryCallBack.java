package com.happynetwork.vrestate.listeners;

import com.happynetwork.vrestate.localdata.beans.responsevos.Industry;

import java.util.List;

/**
 * Created by Tom.yuan on 2017/1/20.
 */

public interface GetIndustryCallBack {
    public abstract void suc(List<Industry> industrys);
    public abstract void fail(String code);
}
