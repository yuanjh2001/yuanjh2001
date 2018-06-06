package com.happynetwork.common.services;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.happynetwork.common.activitys.UpdateApkActivity;
import com.happynetwork.common.events.Float_UpdateApkDialogClickEvent;
import com.happynetwork.common.nohttp.CallServer;
import com.happynetwork.common.nohttp.HttpListener;
import com.happynetwork.common.nohttp.SSLContextUtil;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.common.utils.Tools;
import com.happynetwork.common.vo.UpdateApkResponseVo;
import com.happynetwork.common.R;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * 自动更新服务
 */
public class UpdateServiceForGameSdk extends BaseService{
    private Application mContext;
    private String apkUrl = "";
    private int fileSize;
    int downLoadFileSize;
    private NotificationManager mNotificationManager = null;
    private Notification mNotification;
    private int updateVersion;
    private String apkPath = "";
    private String apkName = "";
    private DownloadRequest mDownloadRequest;
    private String apiUrl = "";
    private String appsecret = "";
    private String appChanel = "";
    private String gameId = "";
    private String serverId = "";
    private boolean isCompulsoryUpdate = false;
    private Float_UpdateApkDialogClickEvent myUpdateApkDialogEvent;
    public SimpleBinder sBinder;

    public class SimpleBinder extends Binder {
        public UpdateServiceForGameSdk getService() {
            return UpdateServiceForGameSdk.this;
        }
    }

    public void setMyUpdateApkDialogEvent(Float_UpdateApkDialogClickEvent event){
        myUpdateApkDialogEvent = event;
    }

    private void showUpdateApkDialog(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), UpdateApkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCompulsoryUpdate",isCompulsoryUpdate);
        bundle.putString("type","gamesdk");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 是否忽略版本更新
     */
    class IgnoringVo{
        private int version;
        private boolean isIgnoring;

        public boolean isIgnoring() {
            return isIgnoring;
        }

        public void setIgnoring(boolean ignoring) {
            isIgnoring = ignoring;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }

    /**
     * @return 是否忽略
     */
    private boolean isIgnoring() {
        SharedPreferences preferences = getSharedPreferences("data",0);
        String json = preferences.getString("IgnoringVo", "");
        IgnoringVo vo = new Gson().fromJson(json,IgnoringVo.class);
        if(vo != null && vo.getVersion() == updateVersion && vo.isIgnoring()){
            return true;
        }
        return false;
    }

    private void setIgnoring(){
        IgnoringVo vo = new IgnoringVo();
        vo.setIgnoring(true);
        vo.setVersion(updateVersion);
        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
        sharedata.putString("IgnoringVo", new Gson().toJson(vo)).commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("onCreate");
        mContext = getApplication();
        sBinder = new SimpleBinder();
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            apkPath = File.separator + "sdcard" + File.separator;
            apkName = (info.packageName + "_" + info.versionName).replace(".", "_") + ".apk";
            LogUtils.i(apkPath+apkName);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(e.toString());
        }

    }

    private Map<String, String> getParams(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", "100000");
        map.put("timespan", (System.currentTimeMillis() / 1000) + "");
        map.put("clientip", Tools.getLocalIpAddress());
        map.put("restype", "json");
        map.put("zparam", "");
        return map;
    }

    private void httpsCheckUpdate() {
        Request<String> httpsRequest = NoHttp.createStringRequest(apiUrl, RequestMethod.POST);
        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
        if (sslContext != null){
            httpsRequest.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        httpsRequest.setHostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", "100001");
        map.put("timespan", (System.currentTimeMillis() / 1000) + "");
        map.put("clientip", Tools.getLocalIpAddress());
        map.put("appchannel", appChanel);//打包渠道识别串
        map.put("gameid", gameId);//游戏厂商申请的id
        map.put("restype","json");
        map.put("serverid", serverId);//游戏厂商提供的服务器id
        try {
            map.put("zparam", Base64.encodeToString("".getBytes("UTF-8"), Base64.NO_WRAP));//应用自定义参数，原样返回
        } catch (UnsupportedEncodingException e) {
            LogUtils.e(e.toString());
        }
        LogUtils.i(map.get("zparam"));
        String sign = Tools.signMd5(map, appsecret);
        map.put("sign",sign);
        LogUtils.i(sign);
        map = Tools.urlEncode(map);
        httpsRequest.add(map);
        LogUtils.i(map.toString());
        LogUtils.i(httpsRequest.url());
        CallServer.getRequestInstance().add(mContext, 0, httpsRequest, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if (response.getHeaders().getResponseCode() != 200) {
                    LogUtils.w("ResponseCode " + response.getHeaders().getResponseCode());
                    ToastUtils.createToast(mContext, "服务器错误!");
                    return;
                }
                LogUtils.i(response.toString());
                UpdateApkResponseVo vo;
                try {
                    vo = new Gson().fromJson(response.get(), UpdateApkResponseVo.class);
                }catch (Exception e){
                    LogUtils.w(e.toString());
                    vo = new UpdateApkResponseVo();
                }
                if (vo != null && vo.getResult() != null) {
                    LogUtils.i(vo.getResult().getRet());
                    if ("0".equals(vo.getResult().getRet())) {
                        try {
                            PackageManager manager = getPackageManager();
                            PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
                            int systemCode = info.versionCode;

                            updateVersion = vo.getAppversion();
                            LogUtils.i("systemCode " + systemCode + " version " + vo.getVersion() + " appversion " + updateVersion + " appdownloadurl " + vo.getAppdownloadurl());
                            apkUrl = vo.getAppdownloadurl();
                            isCompulsoryUpdate = vo.isCompulsoryUpdate();

                            if (false) {
                                apkUrl = "http://dlcdn.87870.com/apk/87870_android_613.apk";
                                systemCode = 0;
                                isCompulsoryUpdate = true;
                            }

                            if (updateVersion > systemCode && (!isIgnoring() || isCompulsoryUpdate)) {
                                showUpdateApkDialog();
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            LogUtils.w(e.toString());
                        }
                    }else {
                        //ToastUtils.createToast(mContext, vo.getResult().getMsg());
                        if (false) {
                            apkUrl = "http://dlcdn.87870.com/apk/87870_android_613.apk";
                            isCompulsoryUpdate = true;
                            showUpdateApkDialog();
                        }
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                LogUtils.w(responseCode + " networkMillis " + networkMillis + " exception" + exception.toString());
            }
        }, false, false);
    }

    public void downFile() {
        if (mDownloadRequest == null || mDownloadRequest.isFinished()){
            notificationInit();
            // url下载地址,fileFolder保存的文件夹,fileName文件名,isRange是否断点续传下载,isDeleteOld 如果发现存在同名文件，是否删除后重新下载，如果不删除，则直接下载成功。
            mDownloadRequest = NoHttp.createDownloadRequest(apkUrl, apkPath, apkName, true, true);
            CallServer.getDownloadInstance().add(0, mDownloadRequest, downloadListener);
        }else {
            LogUtils.i("正在下载");
            ToastUtils.createToast(mContext, "正在下载安装包，请稍后!");
        }
    }

    /**
     * 下载监听
     */
    private DownloadListener downloadListener = new DownloadListener() {

        @Override
        public void onDownloadError(int what, Exception exception) {
            ToastUtils.createToast(mContext, "下载安装包失败！");
            LogUtils.w(exception.toString());
        }

        @Override
        public void onStart(int what, boolean isResume, long beforeLength, Headers headers, long allCount) {
            int progress = 0;
            if (allCount != 0) {
                fileSize = 100;
                progress = (int) (beforeLength * 100 / allCount);
                downLoadFileSize = progress;
                sendMsg(progress);
            }
        }

        @Override
        public void onProgress(int what, int progress, long fileCount) {
            downLoadFileSize = progress;
            sendMsg(progress);
        }

        @Override
        public void onFinish(int what, String filePath) {
            downLoadFileSize = 100;
            sendMsg(100);
            ToastUtils.createToast(mContext, "下载成功！",true);
            update();
        }

        @Override
        public void onCancel(int what) {
            ToastUtils.createToast(mContext, "取消下载！");
        }
    };

    @Override
    protected void timerUpdate() {

    }

    /**
     * 更新
     */
    void update() {
        if(myUpdateApkDialogEvent != null){
            myUpdateApkDialogEvent.updateState(1000);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(apkPath+apkName)), "application/vnd.android.package-archive");
        startActivity(intent);
        stopSelf();
    }

    public void notificationInit() {
        //通知栏内显示下载进度条
        Intent intent = new Intent();//点击进度条，进入程序
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.xf_common_btn_download_ic_download;
        mNotification.tickerText = "下载...";
        mNotification.contentView = new RemoteViews(getPackageName(), R.layout.xf_common_updateapk_notification);//通知栏中进度布局
        mNotification.contentIntent = pIntent;
    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
        if(myUpdateApkDialogEvent != null){
            myUpdateApkDialogEvent.updateState(flag);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mNotification.contentView.setTextViewText(R.id.content_view_text1, msg.what + "%");
            mNotification.contentView.setProgressBar(R.id.content_view_progress, fileSize, downLoadFileSize, false);
            mNotificationManager.notify(0, mNotification);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("onStartCommand");
        if(intent != null){
            apiUrl = intent.getExtras().getString("url");
            appsecret = intent.getExtras().getString("appsecret");
            appChanel = intent.getExtras().getString("appChanel");
            gameId = intent.getExtras().getString("gameId");
            serverId = intent.getExtras().getString("serverId");
            LogUtils.i(apiUrl);
            LogUtils.i(appsecret);
            LogUtils.i(appChanel);
            LogUtils.i(gameId);
            LogUtils.i(serverId);
            httpsCheckUpdate();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtils.i("onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }
}
