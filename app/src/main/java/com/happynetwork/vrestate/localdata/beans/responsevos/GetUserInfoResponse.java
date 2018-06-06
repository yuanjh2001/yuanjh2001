package com.happynetwork.vrestate.localdata.beans.responsevos;

import com.happynetwork.vrestate.localdata.beans.UserInfo;

/**
 * Created by Tom.yuan on 2017/1/3.
 */

public class GetUserInfoResponse {
    private ResponseHead head;
    private UserInfo body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public UserInfo getBody() {
        return body;
    }

    public void setBody(UserInfo body) {
        this.body = body;
    }

}
