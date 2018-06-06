package com.happynetwork.vrestate.localdata.beans.responsevos;

import java.util.List;

/**
 * Created by Tom.yuan on 2017/1/19.
 */

public class AreaResponse {
    private ResponseHead head;
    private List<ProvinceVo> body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<ProvinceVo> getBody() {
        return body;
    }

    public void setBody(List<ProvinceVo> body) {
        this.body = body;
    }

}
