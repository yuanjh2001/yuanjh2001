package com.happynetwork.vrestate.localdata.managers;

import android.app.Activity;
import android.app.Application;

import com.google.gson.Gson;
import com.happynetwork.common.nohttp.CallServer;
import com.happynetwork.common.nohttp.HttpListener;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.listeners.GetAreaCallBack;
import com.happynetwork.vrestate.listeners.GetIndustryCallBack;
import com.happynetwork.vrestate.localdata.ApiList;
import com.happynetwork.vrestate.localdata.beans.requestvos.RequestVo;
import com.happynetwork.vrestate.localdata.beans.responsevos.AreaResponse;
import com.happynetwork.vrestate.localdata.beans.responsevos.IndustryResponse;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * 字典接口</br>
 * Created by Tom.yuan on 2017/1/19.
 */
public class DictionaryServiceManager extends BaseManager {
    private static DictionaryServiceManager instance;
    private Activity mContext;
    private Application application;
    private boolean showLoading = true;
    private GetAreaCallBack getAreaCallBack;
    private GetIndustryCallBack getIndustryCallBack;

    private DictionaryServiceManager(){

    }

    public static DictionaryServiceManager getInstance(){
        if(instance == null){
            instance = new DictionaryServiceManager();
        }
        return instance;
    }

    public DictionaryServiceManager setContext(Activity context){
        mContext = context;
        application = mContext.getApplication();
        return getInstance();
    }

    public DictionaryServiceManager setApplicationContext(Application context){
        application = context;
        return getInstance();
    }

    public DictionaryServiceManager setShowLoading(boolean showLoading){
        this.showLoading = showLoading;
        return getInstance();
    }
    /**
     * 获取行政区域接口
     * @param callBack 成功回调
     */
    public void getArea(GetAreaCallBack callBack){
        this.getAreaCallBack = callBack;
        Request<String> request = initRequest(ApiList.getInstance().getAreaUrl());
        RequestVo requestVo = getRequestVo(mContext);
        String json = new Gson().toJson(requestVo);
        request.setDefineRequestBodyForJson(json);
        LogUtils.i(json);
        CallServer.getRequestInstance().add(mContext, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.getHeaders().getResponseCode() != 200){
                    LogUtils.w("ResponseCode "+response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                    getAreaCallBack.fail("responsecode_"+response.getHeaders().getResponseCode());
                    return;
                }
                LogUtils.i(response.get());
                AreaResponse vo = null;
                try {
                    vo = new Gson().fromJson(response.get(), AreaResponse.class);
                }catch (Exception e){
                    ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                    getAreaCallBack.fail("responsecode_jsonerror");
                }
                if(vo != null && vo.getHead() != null){
                    LogUtils.i(vo.getHead().getCode());
                    if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                        getAreaCallBack.suc(vo.getBody());
                    }else {
                        if(vo.getHead().getMessage().equals(""))ToastUtils.createToast(mContext,vo.getHead().getMessage());
                        doErrorCode(vo.getHead().getCode());
                        getAreaCallBack.fail(vo.getHead().getCode());
                    }
                }else {
                    LogUtils.w("vo is null");
                    getAreaCallBack.fail("responsecode_jsonerror1");
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(url);
                showToast(mContext,exception);
                getAreaCallBack.fail("responsecode_"+responseCode);
            }
        }, false, showLoading);
    }

    /**
     * 获取行业分类
     * @param callBack 成功回调
     */
    public void getIndustry(GetIndustryCallBack callBack){
        this.getIndustryCallBack = callBack;
        Request<String> request = initRequest(ApiList.getInstance().getIndustryUrl());
        RequestVo requestVo = getRequestVo(mContext);
        String json = new Gson().toJson(requestVo);
        request.setDefineRequestBodyForJson(json);
        LogUtils.i(json);
        CallServer.getRequestInstance().add(mContext, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.getHeaders().getResponseCode() != 200){
                    LogUtils.w("ResponseCode "+response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                    getIndustryCallBack.fail("responsecode_"+response.getHeaders().getResponseCode());
                    return;
                }
                LogUtils.i(response.get());
                IndustryResponse vo = null;
                try {
                    vo = new Gson().fromJson(response.get(), IndustryResponse.class);
                }catch (Exception e){
                    ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                    getIndustryCallBack.fail("responsecode_jsonerror");
                }
                if(vo != null && vo.getHead() != null){
                    LogUtils.i(vo.getHead().getCode());
                    if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                        getIndustryCallBack.suc(vo.getBody());
                    }else {
                        if(vo.getHead().getMessage().equals(""))ToastUtils.createToast(mContext,vo.getHead().getMessage());
                        doErrorCode(vo.getHead().getCode());
                        getIndustryCallBack.fail(vo.getHead().getCode());
                    }
                }else {
                    LogUtils.w("vo is null");
                    getIndustryCallBack.fail("responsecode_jsonerror1");
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(url);
                showToast(mContext,exception);
                getIndustryCallBack.fail("responsecode_"+responseCode);
            }
        }, false, showLoading);
    }


    /**
     * 处理服务器错误码，这里主要处理token过期
     * @param errCode
     */
    @Override
    protected void doErrorCode(String errCode){

    }


}
