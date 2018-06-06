package com.happynetwork.vrestate.listeners;

import com.happynetwork.vrestate.localdata.beans.UserInfo;

/**
 * Created by Tom.yuan on 2017/1/16.
 */

public interface UpdateUserInfoByTokenCallBack {
    public abstract void getUserInfoSuc(UserInfo userInfo);
    public abstract void fail();
}
