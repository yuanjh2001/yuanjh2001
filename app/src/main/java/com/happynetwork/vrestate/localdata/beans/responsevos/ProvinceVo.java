package com.happynetwork.vrestate.localdata.beans.responsevos;

import java.util.List;

/**
 * 省
 * Created by Tom.yuan on 2017/1/19.
 */

public class ProvinceVo {
    private String id;
    private String text;
    private String parentid;
    private List<CityVo> children;

    public List<CityVo> getChildren() {
        return children;
    }

    public void setChildren(List<CityVo> children) {
        this.children = children;
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

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
