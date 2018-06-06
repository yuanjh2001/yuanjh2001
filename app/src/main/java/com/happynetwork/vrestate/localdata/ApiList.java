package com.happynetwork.vrestate.localdata;

import android.os.Environment;

import com.happynetwork.common.utils.LogUtils;

import java.io.File;

/**
 * 服务器接口
 * Created by Tom.yuan on 2017/1/3.
 */
public class ApiList {
    private static ApiList instance;
    private static final String TEMP_DIR = "xf_estate_temp" + File.separator;
    public static final int TIMEOUT = 20000;
    public static final String SUCESS = "1";
    private String dataDir;
    private String baseUrl = "http://120.132.71.98/";
    private String sendSms = "user/index/sendsms";
    private String regUrl = "user/index/register";
    private String loginUrl = "user/index/login";
    private String logoutUrl = "user/index/logout";
    private String imgUploadUrl = "sns/index/upload";
    private String imgUploadViewUr = "http://120.132.71.98/";
    private String areaUrl = "sns/index/area";
    private String cateTypeUrl = "sns/shop/cate";
    private String serviceTypeUrl = "sns/service/cate";
    private String getUserInfoUrl = "user/user/info";
    private String industryUrl = "user/index/industry";
    private String saveUserUrl = "user/user/save";

    private ApiList() {

    }

    public String getDataDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataDir = (dataDir == null || dataDir.equals("")) ? Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + TEMP_DIR : dataDir;
        }
        return dataDir;
    }

    public static ApiList getInstance() {
        if (instance == null) {
            instance = new ApiList();
        }
        return instance;
    }

    public void setBaseUrl(String url) {
        baseUrl = url;
        LogUtils.i(baseUrl);
    }

    /**
     * @return 注册请求url
     */
    public String getRegUrl() {
        return baseUrl + regUrl;
    }

    /**
     * @return 登录请求url
     */
    public String getLoginUrl() {
        return baseUrl + loginUrl;
    }

    /**
     * @return 获取用户信息URL
     */
    public String getGetUserInfoUrl(){
        return baseUrl + getUserInfoUrl;
    }

    /**
     * @return 登出请求url
     */
    public String getLogoutUrl() {
        return baseUrl + logoutUrl;
    }

    /**
     * @return 短信验证码请求url
     */
    public String getSendSms() {
        return baseUrl + sendSms;
    }

    /**
     * @return 地区列表请求url
     */
    public String getAreaUrl(){
        return baseUrl + areaUrl;
    }

    /**
     * @return 商户分类请求url
     */
    public String getCateTypeUrl(){
        return baseUrl + cateTypeUrl;
    }

    /**
     * @return 服务分类请求url
     */
    public String getServiceTypeUrl(){
        return baseUrl + serviceTypeUrl;
    }

    /**
     * @return 图像上传url
     */
    public String getImgUploadUrl(){
        return baseUrl+imgUploadUrl;
    }

    /**
     * @return 图像浏览url
     */
    public String getImgUploadViewUr(){
        return imgUploadViewUr;
    }

    /**
     * @return 行业分类
     */
    public String getIndustryUrl(){
        return baseUrl + industryUrl;
    }

    /**
     * @return 保存用户信息URL
     */
    public String getSaveUserUrl(){
        return baseUrl + saveUserUrl;
    }
}
