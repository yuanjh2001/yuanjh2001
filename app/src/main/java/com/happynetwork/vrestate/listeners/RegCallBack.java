package com.happynetwork.vrestate.listeners;

/**
 * 注册回调
 * Created by Tom.yuan on 2016/10/12.
 */
public interface RegCallBack {
    public abstract void regSuc(String token);
    public abstract void regFail();
}
