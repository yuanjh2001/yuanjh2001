package com.happynetwork.vrestate.localdata.beans.responsevos;

/**
 * Created by Tom.yuan on 2017/1/19.
 */

public class UploadImgResponse {
    private ResponseHead head;
    private ImgVo body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public ImgVo getBody() {
        return body;
    }

    public void setBody(ImgVo body) {
        this.body = body;
    }

}
