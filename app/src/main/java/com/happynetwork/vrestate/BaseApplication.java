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
package com.happynetwork.vrestate;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.easemob.EMCallBack;
import com.happynetwork.common.nohttp.AuthImageDownloader;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.vrestate.beans.User;
import com.happynetwork.vrestate.widget.DemoHXSDKHelper;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.cache.DBCacheStore;

import java.io.File;
import java.util.Map;

/**
 * @author Tom.yuan
 */
public class BaseApplication extends Application {
    public static BaseApplication _instance;
    private ImageLoader imageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private String dataDir;
    public static boolean isQuitApp = false;
    public static boolean isMainActivity = false;
    private static int _useServer = 2;        // 1 内网; 2 外网测试; 3 外网正式

    public static Context applicationContext;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;

        /**
         * this function will initialize the HuanXin SDK
         *
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         *
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         *
         * for example:
         * 例子：
         *
         * public class DemoHXSDKHelper extends HXSDKHelper
         *
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
            LogUtils.i("======================");
        hxSDKHelper.onInit(_instance);
        LogUtils.i("======================00000");
        setUseServer();
        switch (_useServer) {
            case 1:
                Logger.setDebug(true);
                Logger.setTag("--nohttp--vr-->");
                break;
            case 2:
                Logger.setDebug(true);
                Logger.setTag("--nohttp--vr-->");
                break;
            case 3:

                break;
        }

        NoHttp.initialize(this, new NoHttp.Config().setConnectTimeout(20000).setReadTimeout(20000).setCacheStore(new DBCacheStore(this).setEnable(true)));

        initSharePF();
        MobclickAgent.openActivityDurationTrack(false);
    }

    public void setUseServer() {
        ApplicationInfo appInfo;
        try {
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            LogUtils.i("UMENG_APPKEY = " + appInfo.metaData.getString("UMENG_APPKEY"));
            LogUtils.i("UMENG_CHANNEL = " + appInfo.metaData.getString("UMENG_CHANNEL"));
            _useServer = appInfo.metaData.getInt("USER_SERVER");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.i("_useServer = " + _useServer);
    }

    private void initSharePF() {
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wx9fedc7dcca5daf19",
                "7ad285d19ab2573b374dc329e82ab6fd");
        PlatformConfig.setSinaWeibo("2268707844",
                "a7ddc9f94f8676bbe599e9b60b1b3b7c");
        PlatformConfig.setQQZone("101289184", "4737469a0876b8764f48d31a6d185f38");
        Config.REDIRECT_URL = "http://account.87870.com/sina/app.ashx";
    }

    public static BaseApplication getInstance() {
        return _instance;
    }

    public DisplayImageOptions getImgOps() {
        if (mDisplayImageOptions == null) {
            mDisplayImageOptions = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .build();
        }
        return mDisplayImageOptions;
    }

    public ImageLoader getImLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                    .threadPoolSize(1)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .memoryCache(new WeakMemoryCache())
                    .imageDownloader(new AuthImageDownloader(_instance))
                    .build();
            imageLoader.init(config);
        }
        return imageLoader;
    }


    /**
     * @return 屏幕尺寸
     */
    public int[] getSenceSize() {
        int[] res = new int[2];
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        res[0] = metric.widthPixels;
        res[1] = metric.heightPixels;
        LogUtils.i("SenceSize: " + res[0] + " " + res[1] + " dpi: " + metric.densityDpi);
        return res;
    }

    public String getAppDataDir() {
        dataDir = (dataDir == null || dataDir.equals("")) ? Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName() + File.separator : dataDir;
        File f = new File(dataDir);
        if (!f.exists()) {
            LogUtils.i(" 创建目录 " + dataDir);
            f.mkdirs();
        }
        return dataDir;
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     * @param username
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final boolean isGCM,final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(isGCM,emCallBack);
    }

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        return hxSDKHelper.getContactList();
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);
    }
}
