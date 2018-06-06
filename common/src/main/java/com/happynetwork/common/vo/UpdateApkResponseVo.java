package com.happynetwork.common.vo;

import android.util.Base64;

import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.Tools;

import java.io.UnsupportedEncodingException;

/**
 * Created by Tom.yuan on 2016/9/8.
 */
public class UpdateApkResponseVo {
    private ResponseCommonResult result;
    private String ZParam;
    private String version;
    private int appversion;
    private String appdownloadurl;

    private boolean isCompulsoryUpdate;

    /**
     * 是否强制更新
     * @return
     */
    public boolean isCompulsoryUpdate() {
        return isCompulsoryUpdate;
    }

    public void setCompulsoryUpdate(boolean compulsoryUpdate) {
        isCompulsoryUpdate = compulsoryUpdate;
    }

    public ResponseCommonResult getResult() {
        return result;
    }

    public void setResult(ResponseCommonResult result) {
        this.result = result;
    }

    public String getAppdownloadurl() {
        return appdownloadurl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAppdownloadurl(String appdownloadurl) {
        this.appdownloadurl = appdownloadurl;
    }

    public int getAppversion() {
        return appversion;
    }

    public void setAppversion(int appversion) {
        this.appversion = appversion;
    }

    public String getZParam() {
        try {
            LogUtils.i(ZParam);
            ZParam = Tools.urlDecode(Tools.urlDecode(ZParam));
            LogUtils.i(ZParam);
            return new String(Base64.decode(ZParam, Base64.NO_WRAP),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.e(e.toString());
        }
        return "";
    }

    public void setZParam(String ZParam) {
        this.ZParam = ZParam;
    }
}
