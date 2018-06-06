package com.happynetwork.vrestate.listeners;

import com.happynetwork.vrestate.localdata.beans.responsevos.ProvinceVo;

import java.util.List;

/**
 * Created by Tom.yuan on 2017/1/19.
 */

public interface GetAreaCallBack {
    public abstract void suc(List<ProvinceVo> provinceVos);
    public abstract void fail(String code);
}
