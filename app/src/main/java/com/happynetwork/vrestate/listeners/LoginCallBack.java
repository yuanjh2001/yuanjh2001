package com.happynetwork.vrestate.listeners;

/**
 * Created by Tom.yuan on 2017/1/3.
 */
public interface LoginCallBack {
    /**
     * 登录回调
     * @param token 服务器返回的用户token
     */
    public abstract void loginSuc(String token);

    /**
     * 登录失败
     * @param code 失败代码
     */
    public abstract void loginFail(String code);
}
