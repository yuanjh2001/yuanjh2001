package com.happynetwork.common;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.happynetwork.common.events.UmengEvent;
import com.happynetwork.common.events.Xf_Common_ExitApp;
import com.happynetwork.common.services.TimerService;
import com.happynetwork.common.services.UpdateService;
import com.happynetwork.common.services.UpdateServiceForGameSdk;
import com.happynetwork.common.utils.LogUtils;

public final class CommonSdk {
    public final static String Exit_MESSAGE_RECEIVED_ACTION = "commonsdk_exit_message_received";
    public static Application application;
    private static boolean isInit = false;
    public static Xf_Common_ExitApp xf_common_exitApp;
    public static UmengEvent umengEvent;

    /**
     * 初始化sdk
     *
     * @param context 应用上下文
     */
    public static void initSdk(Application context) {
        application = context;
        Intent intent = new Intent();
        intent.setClass(application, TimerService.class);
        application.startService(intent);
        isInit = true;
    }

    /**
     * 打开自动更新服务
     * @param c 应用上下文
     * @param url 更新服务url
     * @param appsecret key
     * @param isShowTips 检测版本更新时调用，true将弹出相关提示，并且忽略之前勾选的忽略此版本选项
     */
    public static void openUpdate(Application c,String url,String appsecret,boolean isShowTips,Xf_Common_ExitApp callBack) {
        checkInit();
        xf_common_exitApp = callBack;
        LogUtils.i("openUpdate");
        Intent intent1 = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("appsecret",appsecret);
        bundle.putBoolean("showTips",isShowTips);
        intent1.putExtras(bundle);
        intent1.setClass(c, UpdateService.class);
        c.startService(intent1);
    }

    public static void openUpdate(Application c,String url,String appsecret,Xf_Common_ExitApp callBack) {
        checkInit();
        xf_common_exitApp = callBack;
        LogUtils.i("openUpdate");
        Intent intent1 = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("appsecret",appsecret);
        bundle.putBoolean("showTips",false);
        intent1.putExtras(bundle);
        intent1.setClass(c, UpdateService.class);
        c.startService(intent1);
    }

    /**
     * 打开自动更新服务
     *
     * @param c 应用程序上下文
     */
    public static void openUpdateForGameSDK(Application c,String url,String appsecret,String appChanel,String gameId,String serverId) {
        checkInit();
        LogUtils.i("openUpdate...");
        Intent intent1 = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("appsecret",appsecret);
        bundle.putString("appChanel",appChanel);
        bundle.putString("gameId",gameId);
        bundle.putString("serverId",serverId);
        intent1.putExtras(bundle);
        intent1.setClass(c, UpdateServiceForGameSdk.class);
        c.startService(intent1);
    }

    private static void checkInit() {
        if (!isInit) {
            LogUtils.e("sdk not init");
            throw new ExceptionInInitializerError("common module not init...");
        }
    }

    public static void destroy(Application context) {
        isInit = false;
        Intent msgIntent = new Intent(Exit_MESSAGE_RECEIVED_ACTION);
        context.sendBroadcast(msgIntent);
    }
}
