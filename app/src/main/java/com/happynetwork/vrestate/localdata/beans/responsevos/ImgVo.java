package com.happynetwork.vrestate.localdata.beans.responsevos;

import com.happynetwork.common.utils.LogUtils;

/**
 * Created by Tom.yuan on 2017/1/20.
 */

public class ImgVo {
    private String extention;
    private String originalImg;
    private String fileId;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    public String getOriginalImg() {
        LogUtils.i(originalImg);
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
