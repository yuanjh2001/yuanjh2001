package com.happynetwork.vrestate.localdata.beans.responsevos;

/**
 * Created by Tom.yuan on 2017/1/3.
 */

public class LoginResponse {
    private ResponseHead head;
    private RegVo body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public RegVo getBody() {
        return body;
    }

    public void setBody(RegVo body) {
        this.body = body;
    }

}
