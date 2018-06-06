package com.happynetwork.vrestate.localdata.managers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.happynetwork.common.nohttp.CallServer;
import com.happynetwork.common.nohttp.HttpListener;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.common.utils.Tools;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.listeners.LoginCallBack;
import com.happynetwork.vrestate.listeners.LogoutCallBack;
import com.happynetwork.vrestate.listeners.PhoneCodeCallBack;
import com.happynetwork.vrestate.listeners.RegCallBack;
import com.happynetwork.vrestate.listeners.SaveUserInfoCallBack;
import com.happynetwork.vrestate.listeners.UpdateUserInfoByTokenCallBack;
import com.happynetwork.vrestate.listeners.UploadFileCallBack;
import com.happynetwork.vrestate.localdata.ApiList;
import com.happynetwork.vrestate.localdata.beans.UserInfo;
import com.happynetwork.vrestate.localdata.beans.requestvos.RequestVo;
import com.happynetwork.vrestate.localdata.beans.requestvos.UploadType;
import com.happynetwork.vrestate.localdata.beans.responsevos.GetUserInfoResponse;
import com.happynetwork.vrestate.localdata.beans.responsevos.LoginResponse;
import com.happynetwork.vrestate.localdata.beans.responsevos.RegResponse;
import com.happynetwork.vrestate.localdata.beans.responsevos.SaveUserResponse;
import com.happynetwork.vrestate.localdata.beans.responsevos.SmsResponse;
import com.happynetwork.vrestate.localdata.beans.responsevos.UploadImgResponse;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.File;

/**
 * 用户登录注册接口</br>
 * Created by Tom.yuan on 2016/10/11.
 */
public class UserServiceManager extends BaseManager{
    private static UserServiceManager instance;
    private Activity mContext;
    private Application application;
    private LoginCallBack mLoginCallBack;
    private boolean showLoading = true;
    private RegCallBack regCallBack;
    private PhoneCodeCallBack phoneCodeCallBack;
    private LogoutCallBack logoutCallBack;
    private UploadFileCallBack uploadFileCallBack;
    private UpdateUserInfoByTokenCallBack updateUserInfoByTokenCallBack;
    private SaveUserInfoCallBack saveUserInfoCallBack;

    private UserServiceManager(){

    }

    public static UserServiceManager getInstance(){
        if(instance == null){
            instance = new UserServiceManager();
        }
        return instance;
    }

    public UserServiceManager setContext(Activity context){
        mContext = context;
        application = mContext.getApplication();
        return getInstance();
    }

    public UserServiceManager setApplicationContext(Application context){
        application = context;
        return getInstance();
    }

    public UserServiceManager setShowLoading(boolean showLoading){
        this.showLoading = showLoading;
        return getInstance();
    }

    public void setuserInfo(UserInfo userInfo,Context context) {
        SharedPreferences.Editor sharedata = context.getSharedPreferences("data", 0).edit();
        sharedata.putString("userInfo", new Gson().toJson(userInfo)).commit();
    }

    public UserInfo getUserInfo(Context context){
        SharedPreferences preferences = context.getSharedPreferences("data",0);
        String json = preferences.getString("userInfo", "");
        LogUtils.i(json.toString());
        UserInfo userInfo = new Gson().fromJson(json,UserInfo.class);
        if(userInfo == null){
            userInfo = new UserInfo();
        }
        return  userInfo;
    }

    /**
     * 登录接口
     * @param user 登录参数
     * @param loginCallBack 登录成功回调
     */
    public void doLogin(UserInfo user, LoginCallBack loginCallBack){
        if (user.getUserMobile().equals("")) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.tel_isempty));
            return;
        }
        if (user.getSmsCode().equals("")) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.checkcode_isempty));
            return;
        }
        if (!Tools.checkTelphone(user.getUserMobile())) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.tel_iserror));
            return;
        }
        this.mLoginCallBack = loginCallBack;
        Request<String> request = initRequest(ApiList.getInstance().getLoginUrl());
        RequestVo requestVo = new RequestVo();
        requestVo.setBody(user);
        String json = new Gson().toJson(requestVo);
        request.setDefineRequestBodyForJson(json);
        LogUtils.i(json);
        CallServer.getRequestInstance().add(mContext, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.getHeaders().getResponseCode() != 200){
                    LogUtils.w("ResponseCode "+response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                    mLoginCallBack.loginFail("responsecode_"+response.getHeaders().getResponseCode());
                    return;
                }
                LogUtils.i(response.get());
                LoginResponse vo = null;
                try {
                    vo = new Gson().fromJson(response.get(), LoginResponse.class);
                }catch (Exception e){
                    ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                    mLoginCallBack.loginFail("responsecode_jsonerror");
                }
                if(vo != null && vo.getHead() != null){
                    LogUtils.i(vo.getHead().getCode());
                    if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                        mLoginCallBack.loginSuc(vo.getBody().getToken());
                    }else {
                        if(!vo.getHead().getMessage().equals(""))ToastUtils.createToast(mContext,vo.getHead().getMessage());
                        doErrorCode(vo.getHead().getCode());
                        mLoginCallBack.loginFail(vo.getHead().getCode());
                    }
                }else {
                    LogUtils.w("vo is null");
                    mLoginCallBack.loginFail("responsecode_jsonerror1");
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(url);
                showToast(mContext,exception);
                mLoginCallBack.loginFail("responsecode_"+responseCode);
            }
        }, false, showLoading);
    }


    /**
     * 用户注册接口
     * @param user 用户注册信息
     * @param callBack 回调函数
     */
    public void doReg(UserInfo user,RegCallBack callBack){
        if (user.getUserMobile().equals("")) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.tel_isempty));
            return;
        }
        if (user.getSmsCode().equals("")) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.checkcode_isempty));
            return;
        }
        if (!Tools.checkTelphone(user.getUserMobile())) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.tel_iserror));
            return;
        }
        regCallBack = callBack;
        Request<String> request = initRequest(ApiList.getInstance().getRegUrl());
        RequestVo requestVo = new RequestVo();
        requestVo.setBody(user);
        String json = new Gson().toJson(requestVo);
        request.setDefineRequestBodyForJson(json);
        LogUtils.i(json);
        CallServer.getRequestInstance().add(mContext, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.getHeaders().getResponseCode() != 200){
                    LogUtils.w("ResponseCode "+response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                    regCallBack.regFail();
                    return;
                }
                LogUtils.i(response.get());
                RegResponse vo = null;
                try {
                    vo = new Gson().fromJson(response.get(), RegResponse.class);
                }catch (Exception e){
                    LogUtils.w(e.toString());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                    regCallBack.regFail();
                }

                if(vo != null && vo.getHead() != null){
                    LogUtils.i(vo.getHead().getCode());
                    if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                        regCallBack.regSuc(vo.getBody().getToken());
                    }else {
                        LogUtils.i(vo.getHead().getMessage());
                        if(!vo.getHead().getMessage().equals(""))ToastUtils.createToast(mContext,vo.getHead().getMessage());
                        doErrorCode(vo.getHead().getCode());
                        regCallBack.regFail();
                    }
                }else {
                    LogUtils.w("vo is null");
                    regCallBack.regFail();
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(url);
                showToast(mContext,exception);
                regCallBack.regFail();
            }
        }, false, showLoading);
    }

    /**
     * 获取最新用户信息
     * @param token token必须
     * @param callBack 回调
     */
    public void getUserInfo(final String token, UpdateUserInfoByTokenCallBack callBack){
        this.updateUserInfoByTokenCallBack = callBack;
        Request<String> request = initRequest(ApiList.getInstance().getGetUserInfoUrl());
        RequestVo requestVo = getRequestVo(mContext);
        UserInfo user = new UserInfo();
        user.setToken(token);
        requestVo.setBody(user);
        requestVo.setToken(user.getToken());
        String json = new Gson().toJson(requestVo);
        request.setDefineRequestBodyForJson(json);
        LogUtils.i(json);
        CallServer.getRequestInstance().add(mContext, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.getHeaders().getResponseCode() != 200){
                    LogUtils.w("ResponseCode "+response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                    updateUserInfoByTokenCallBack.fail();
                    return;
                }
                LogUtils.i(response.get());
                GetUserInfoResponse vo = null;
                try {
                    vo = new Gson().fromJson(response.get(), GetUserInfoResponse.class);
                }catch (Exception e){
                    LogUtils.w(e.toString());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                    updateUserInfoByTokenCallBack.fail();
                }

                if(vo != null && vo.getHead() != null){
                    LogUtils.i(vo.getHead().getCode());
                    if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                        UserInfo userInfo = vo.getBody();
                        if(userInfo.getToken().equals(""))userInfo.setToken(token);
                        updateUserInfoByTokenCallBack.getUserInfoSuc(userInfo);
                    }else {
                        LogUtils.i(vo.getHead().getMessage());
                        if(!vo.getHead().getMessage().equals(""))ToastUtils.createToast(mContext,vo.getHead().getMessage());
                        doErrorCode(vo.getHead().getCode());
                        updateUserInfoByTokenCallBack.fail();
                    }
                }else {
                    LogUtils.w("vo is null");
                    updateUserInfoByTokenCallBack.fail();
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(url);
                showToast(mContext,exception);
                updateUserInfoByTokenCallBack.fail();
            }
        }, false, showLoading);
    }

    /**
     * 获取手机验证码
     * @param tel 手机号
     * @param callBack 回调函数
     */
    public void getPhoneCode(String tel,PhoneCodeCallBack callBack) {
        phoneCodeCallBack = callBack;
        if(tel == null || tel.trim().equals("")){
            ToastUtils.createToast(mContext,mContext.getString(R.string.tel_isempty));
            phoneCodeCallBack.phoneCodeCallBack(1);
            return ;
        }else if(!Tools.checkTelphone(tel.trim())){
            ToastUtils.createToast(mContext,mContext.getString(R.string.tel_iserror));
            phoneCodeCallBack.phoneCodeCallBack(2);
            return ;
        }
        Request<String> request = initRequest(ApiList.getInstance().getSendSms());
        RequestVo requestVo = new RequestVo();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserMobile(tel);
        requestVo.setBody(userInfo);
        String json = new Gson().toJson(requestVo);
        request.setDefineRequestBodyForJson(json);
        LogUtils.i(json);
        CallServer.getRequestInstance().add(mContext, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.getHeaders().getResponseCode() != 200){
                    LogUtils.w("ResponseCode "+response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                    phoneCodeCallBack.phoneCodeCallBack(3);
                    return;
                }
                LogUtils.i(response.get());
                SmsResponse vo = null;
                try {
                    vo = new Gson().fromJson(response.get(), SmsResponse.class);
                }catch (Exception e){
                    ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                    phoneCodeCallBack.phoneCodeCallBack(3);
                }
                if(vo != null && vo.getHead() != null){
                    LogUtils.i(vo.getHead().getCode());
                    if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                        phoneCodeCallBack.phoneCodeCallBack(0);
                    }else {
                        LogUtils.i(vo.getHead().getMessage());
                        if(!vo.getHead().getMessage().equals(""))ToastUtils.createToast(mContext,vo.getHead().getMessage());
                        doErrorCode(vo.getHead().getCode());
                        phoneCodeCallBack.phoneCodeCallBack(4);
                    }
                }else {
                    LogUtils.w("vo is null");
                    phoneCodeCallBack.phoneCodeCallBack(3);
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(url);
                showToast(mContext,exception);
                phoneCodeCallBack.phoneCodeCallBack(5);
            }
        }, false, showLoading);
    }

    /**
     * 上传文件
     * @param filePath 文件路径
     */
    public void uploadFile(String filePath,UploadFileCallBack callBack) {
        if (filePath == null || filePath.trim().equals("")) {
            ToastUtils.createToast(mContext,mContext.getString(R.string.file_null));
        } else {
            File file = new File(filePath);
            if (!file.exists()) {
                ToastUtils.createToast(mContext,mContext.getString(R.string.file_null));
            }else {
                uploadFileCallBack = callBack;
                Request<String> httpRequest = NoHttp.createStringRequest(ApiList.getInstance().getImgUploadUrl(), RequestMethod.POST);
                httpRequest.add("upload_file", new FileBinary(file));
                //httpRequest.add("type","avatar");
                RequestVo requestVo = getRequestVo(mContext);
                UploadType uploadType = new UploadType();
                uploadType.setType("avatar");
                requestVo.setBody(uploadType);
                String json = new Gson().toJson(requestVo);
                httpRequest.add("param",json);
                LogUtils.i(json);
                LogUtils.i(httpRequest.url());
                CallServer.getRequestInstance().add(mContext, 0, httpRequest, new HttpListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        if (response.getHeaders().getResponseCode() != 200) {
                            LogUtils.w("ResponseCode " + response.getHeaders().getResponseCode());
                            ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                            uploadFileCallBack.fail();
                            return;
                        }
                        LogUtils.i(response.get());
                        UploadImgResponse vo = null;
                        try {
                            vo = new Gson().fromJson(response.get(),UploadImgResponse.class);
                        }catch (Exception e){
                            ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                            uploadFileCallBack.fail();
                        }

                        if(vo != null && vo.getHead() != null){
                            LogUtils.i(vo.getHead().getCode());
                            if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                                uploadFileCallBack.uploadSuc(vo.getBody().getOriginalImg());
                            }else {
                                LogUtils.i(vo.getHead().getMessage());
                                ToastUtils.createToast(mContext,vo.getHead().getMessage());
                                doErrorCode(vo.getHead().getCode());
                                uploadFileCallBack.fail();
                            }
                        }else {
                            LogUtils.w("vo is null");
                            uploadFileCallBack.fail();
                        }
                    }

                    @Override
                    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                        LogUtils.w(url);
                        LogUtils.w(responseCode+" "+exception.getCause() + " " + exception);
                        showToast(mContext,exception);
                        exception.printStackTrace();
                        uploadFileCallBack.fail();
                    }
                }, false,showLoading);
            }
        }
    }

    /**
     * 保存用户信息
     * @param userInfo 用户信息
     * @param callBack 回调
     */
    public void saveUserInfo(final UserInfo userInfo, SaveUserInfoCallBack callBack){
        if (userInfo.getUserPic().equals("")) {
            ToastUtils.createToast(mContext, mContext.getString(R.string.user_main_headimgtips));
            return;
        }
        if (userInfo.getUserNick().equals("")) {
            ToastUtils.createToast(mContext, mContext.getString(R.string.user_main_nicktips));
            return;
        }
        if (userInfo.getUserSex().equals("0")) {
            ToastUtils.createToast(mContext, mContext.getString(R.string.user_main_sextips));
            return;
        }
        this.saveUserInfoCallBack = callBack;
        Request<String> request = initRequest(ApiList.getInstance().getSaveUserUrl());
        RequestVo requestVo = getRequestVo(mContext);
        requestVo.setBody(userInfo);
        String json = new Gson().toJson(requestVo);
        request.setDefineRequestBodyForJson(json);
        LogUtils.i(json);
        CallServer.getRequestInstance().add(mContext, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.getHeaders().getResponseCode() != 200){
                    LogUtils.w("ResponseCode "+response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext,mContext.getString(R.string.responsecode_no_200));
                    saveUserInfoCallBack.fail("responsecode_"+response.getHeaders().getResponseCode());
                    return;
                }
                LogUtils.i(response.get());
                SaveUserResponse vo = null;
                try {
                    vo = new Gson().fromJson(response.get(), SaveUserResponse.class);
                }catch (Exception e){
                    ToastUtils.createToast(mContext,mContext.getString(R.string.gson_error));
                    saveUserInfoCallBack.fail("responsecode_jsonerror");
                }
                if(vo != null && vo.getHead() != null){
                    LogUtils.i(vo.getHead().getCode());
                    if(ApiList.SUCESS.equals(vo.getHead().getCode())){
                        saveUserInfoCallBack.saveSuc();
                        setuserInfo(userInfo,mContext);
                        ToastUtils.createToast(mContext,vo.getHead().getMessage());
                    }else {
                        if(!vo.getHead().getMessage().equals(""))ToastUtils.createToast(mContext,vo.getHead().getMessage());
                        doErrorCode(vo.getHead().getCode());
                        saveUserInfoCallBack.fail(vo.getHead().getCode());
                    }
                }else {
                    LogUtils.w("vo is null");
                    saveUserInfoCallBack.fail("responsecode_jsonerror1");
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(url);
                showToast(mContext,exception);
                saveUserInfoCallBack.fail("responsecode_"+responseCode);
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
