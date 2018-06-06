package com.happynetwork.vrestate.localdata.beans.requestvos;

/**
 * Created by Tom.yuan on 2017/1/3.
 * 请求通用参数
 */

public class RequestHead {
    private String callTime;
    private String callSource;

    public String getCallSource() {
        return callSource;
    }

    public void setCallSource(String callSource) {
        this.callSource = callSource;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }
}
