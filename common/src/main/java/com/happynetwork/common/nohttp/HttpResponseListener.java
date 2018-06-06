/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.happynetwork.common.nohttp;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;

import com.happynetwork.common.dialog.WaitDialog;
import com.happynetwork.common.utils.LogUtils;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * Created in Nov 4, 2015 12:02:55 PM.
 *
 * @author Yan Zhenjie.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private Activity mActivity;

    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<T> callback;

    /**
     * @param c     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Activity c, Request<?> request, HttpListener<T> httpCallback, boolean canCancel, boolean isLoading) {
        this.mActivity = c;
        this.mRequest = request;
        if (c != null && isLoading) {
            LogUtils.i("---------------create dialog----------------------");
            WaitDialog.WaitDialog(c);
            WaitDialog.setCancelable(canCancel);
            WaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }

    public HttpResponseListener(Application c, Request<?> request, HttpListener<T> httpCallback, boolean canCancel, boolean isLoading) {
        this.mRequest = request;
        this.callback = httpCallback;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        if (!WaitDialog.isShowing()){
            LogUtils.i("--start---");
            WaitDialog.show();
        }
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        LogUtils.i("onFinish");
        WaitDialog.dismiss();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();
        if (responseCode > 400 && mActivity != null) {
            if (responseCode == 405) {// 405表示服务器不支持这种请求方法，比如GET、POST、TRACE中的TRACE就很少有服务器支持。
                LogUtils.i("服务器不支持这种请求方法");
            } else {// 但是其它400+的响应码服务器一般会有流输出。
                LogUtils.i(response.toString());
            }
        }
        if (callback != null) {
            callback.onSucceed(what, response);
        }
    }

    @Override
    public void onFailed(int what, Response<T> response) {
        if (callback != null)callback.onFailed(what, response.request().url(), response.getTag(), response.getException(), response.responseCode(), response.getNetworkMillis());
    }
}
