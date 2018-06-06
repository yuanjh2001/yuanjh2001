package com.happynetwork.vrestate.localdata.beans.requestvos;

/**
 * Created by Tom.yuan on 2017/1/3.
 * 请求参数
 */

public class RequestVo {
    private RequestHead head;
    private Object body;
    private String token;

    public RequestVo(){
        head = new RequestHead();
        head.setCallTime(System.currentTimeMillis()/1000+"");
        head.setCallSource("android");
    }

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getToken() {
        token = token == null?"":token;
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
