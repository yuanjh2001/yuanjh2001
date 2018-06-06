package com.happynetwork.vrestate.localdata.beans.responsevos;

import java.util.List;

/**
 * 行业分类
 * Created by Tom.yuan on 2017/1/20.
 */

public class IndustryResponse {
    private ResponseHead head;
    private List<Industry> body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<Industry> getBody() {
        return body;
    }

    public void setBody(List<Industry> body) {
        this.body = body;
    }

}
