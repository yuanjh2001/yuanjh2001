package com.happynetwork.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.happynetwork.common.nohttp.CallServer;
import com.happynetwork.common.nohttp.HttpListener;
import com.happynetwork.common.nohttp.SSLContextUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * 异常处理程序
 *
 * @author Tom.yuan
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private String logUrl = "api/app/addapplog.ashx";
    private String baseUrl = "http://172.16.20.10:5001/";
    private static final boolean DEBUG = true;
    private static CrashHandler sInstance = new CrashHandler();
    // 系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private Application mContext;
    private String logContext;

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    private String getLogUrl(){
        return baseUrl+logUrl;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Application context) {
        mContext = context;
        // 获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void init(Application context,String baseUrl) {
        this.baseUrl = baseUrl;
        mContext = context;
        // 获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(new BufferedWriter(sw));
        try {
            // 导出手机信息
            dumpPhoneInfo(pw);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        pw.println();
        // 导出异常的调用栈信息
        ex.printStackTrace(pw);
        pw.close();
        logContext = sw.getBuffer().toString();
        sendExceptionToServer();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        }
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", "100000");
        map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map.put("clientip", Tools.getLocalIpAddress());
        map.put("restype", "json");
        map.put("zparam", "");
        map.put("content", logContext);
        return map;
    }

    private void sendExceptionToServer() {
        Request<String> httpsRequest = NoHttp.createStringRequest(getLogUrl(), RequestMethod.POST);
        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
        if (sslContext != null) {
            httpsRequest.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        httpsRequest.setHostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);
        Map<String, String> map = getParams();
        Map<String, String> singMap = getParams();
        singMap.put("appsecret","bb99ca735e5c70f5443cb598d3ea5de2");
        String sign = Tools.signValueMd5(singMap);
        map = Tools.urlEncode(map);
        LogUtils.i(sign);
        map.put("sign", sign);
        httpsRequest.add(map);
        LogUtils.i(map.toString());
        LogUtils.i(httpsRequest.url());

        CallServer.getRequestInstance().add(mContext, 0, httpsRequest, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if (response.getHeaders().getResponseCode() != 200) {
                    LogUtils.w("ResponseCode " + response.getHeaders().getResponseCode());
                    return;
                }
                LogUtils.i(response.toString());
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(responseCode + " networkMillis " + networkMillis + " exception" + exception.toString());
                LogUtils.w(url);
            }
        }, false,false);
    }

    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version:");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

}
