package com.happynetwork.vrestate.localdata.managers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.happynetwork.common.nohttp.CallServer;
import com.happynetwork.common.nohttp.HttpListener;
import com.happynetwork.common.nohttp.SSLContextUtil;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.common.utils.Tools;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.listeners.LoginCallBack;
import com.happynetwork.vrestate.localdata.ApiList;
import com.happynetwork.vrestate.localdata.beans.UserInfo;
import com.happynetwork.vrestate.localdata.beans.requestvos.RequestVo;
import com.happynetwork.vrestate.localdata.beans.responsevos.LoginResponse;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ParseError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.net.ProtocolException;

import javax.net.ssl.SSLContext;

/**
 * Created by Tom.yuan on 2017/1/19.
 */
public class BaseManager {

    public BaseManager(){

    }

    public void showToast(Activity mContext,Exception exception){
        if (exception instanceof NetworkError) {// 网络不好
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_please_check_network));
        } else if (exception instanceof TimeoutError) {// 请求超时
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_timeout));
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_not_found_server));
        } else if (exception instanceof URLError) {//
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_url_error));
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_not_found_cache));
        } else if (exception instanceof ProtocolException) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_system_unsupport_method));
        } else if (exception instanceof ParseError) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_parse_data_error));
        } else {
            ToastUtils.createToast(mContext,mContext.getString(R.string.nohttp_error_unknow));
        }
    }

    protected RequestVo getRequestVo(Activity mContext){
        RequestVo requestVo = new RequestVo();
        UserInfo userInfo = UserServiceManager.getInstance().getUserInfo(mContext);
        requestVo.setToken(userInfo.getToken());
        return requestVo;
    }

    protected RequestVo getRequestVo(Application mContext){
        RequestVo requestVo = new RequestVo();
        UserInfo userInfo = UserServiceManager.getInstance().getUserInfo(mContext);
        requestVo.setToken(userInfo.getToken());
        return requestVo;
    }

    protected Request<String> initRequest(String url,boolean isHttps){
        Request<String> httpsRequest = NoHttp.createStringRequest(url, RequestMethod.POST);
        if(isHttps){
            SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
            if (sslContext != null){
                httpsRequest.setSSLSocketFactory(sslContext.getSocketFactory());
            }
            httpsRequest.setHostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);
        }
        httpsRequest.setConnectTimeout(ApiList.TIMEOUT);
        httpsRequest.setReadTimeout(ApiList.TIMEOUT);
        return httpsRequest;
    }

    protected Request<String> initRequest(String url){
        return initRequest(url,false);
    }

    protected Request<String> initHttpsRequest(String url){
        return initRequest(url,true);
    }

    /**
     * 处理服务器错误码，这里主要处理token过期
     * @param errCode
     */
    protected void doErrorCode(String errCode){

    }


}
