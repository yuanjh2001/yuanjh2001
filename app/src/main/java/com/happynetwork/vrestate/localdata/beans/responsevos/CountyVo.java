package com.happynetwork.vrestate.localdata.beans.responsevos;

/**
 * 县
 * Created by Tom.yuan on 2017/1/19.
 */

public class CountyVo {
    private String id;
    private String text;
    private String parentid;

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
