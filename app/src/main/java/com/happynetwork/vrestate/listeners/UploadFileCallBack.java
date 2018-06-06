package com.happynetwork.vrestate.listeners;

/**
 * 上传文件回调
 * Created by Tom.yuan on 2017/1/4.
 */
public interface UploadFileCallBack {
    public abstract void uploadSuc(String url);
    public abstract void fail();
}
