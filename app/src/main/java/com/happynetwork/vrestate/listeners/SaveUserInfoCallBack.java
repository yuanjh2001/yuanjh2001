package com.happynetwork.vrestate.listeners;

/**
 * Created by Tom.yuan on 2017/1/22.
 */

public interface SaveUserInfoCallBack {
    public abstract void saveSuc();
    public abstract void fail(String code);
}
