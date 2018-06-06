package com.happynetwork.vrestate.localdata.beans.responsevos;

/**
 * Created by Tom.yuan on 2017/1/3.
 */

public class ResponseHead {
    private String code;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        code = code == null?"":code;
        return code.trim();
    }

    public void setCode(String code) {
        this.code = code;
    }
}
