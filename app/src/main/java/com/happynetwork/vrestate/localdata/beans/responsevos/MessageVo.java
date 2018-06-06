package com.happynetwork.vrestate.localdata.beans.responsevos;

/**
 * Created by Tom.yuan on 2017/1/3.
 */

public class MessageVo {
    private String smsCode;
    private String userMobile;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
