package com.happynetwork.vrestate.localdata.beans.responsevos;

/**
 * Created by Tom.yuan on 2017/1/3.
 */

public class RegVo {
    public String getToken() {
        token = token == null?"":token;
        return token.trim();
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
}
