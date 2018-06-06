package com.happynetwork.vrestate.localdata.beans;

import java.io.Serializable;

/**
 * Created by Tom.yuan on 2017/1/4.
 */
public class UploadImgVo implements Serializable {
    private String code;
    private String fileid;
    private String thumbfileid;

    public String getThumbfileid() {
        return thumbfileid;
    }

    public void setThumbfileid(String thumbfileid) {
        this.thumbfileid = thumbfileid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }
}
