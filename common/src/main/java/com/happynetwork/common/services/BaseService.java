package com.happynetwork.common.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;

import com.happynetwork.common.CommonSdk;
import com.happynetwork.common.events.HomeWatcher;
import com.happynetwork.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseService extends Service implements HomeWatcher.OnHomePressedListener{
    public final static String APPOPEN_MESSAGE_RECEIVED_ACTION = "appopen_message_received";
    private MyMessageReceiver mMessageReceiver;
    private HomeWatcher mHomeWatcher;

    @Override
    public void onHomePressed() {

    }

    @Override
    public void onHomeLongPressed() {

    }

    @Override
    public void onScreenOn() {

    }

    @Override
    public void onScreenOff() {

    }

    @Override
    public void onUserPresent() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("=============onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        regist();
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(this);
        mHomeWatcher.startWatch();
        return super.onStartCommand(intent, flags, startId);
    }

    private void regist() {
        mMessageReceiver = new MyMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(CommonSdk.Exit_MESSAGE_RECEIVED_ACTION);
        filter.addAction(APPOPEN_MESSAGE_RECEIVED_ACTION);
        filter.addAction(TimerService.TIMER_MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    class MyMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(CommonSdk.Exit_MESSAGE_RECEIVED_ACTION.equals(action)){
                exit();
            }else if(TimerService.TIMER_MESSAGE_RECEIVED_ACTION.equals(action)){
                timerUpdate();
            }else if(APPOPEN_MESSAGE_RECEIVED_ACTION.equals(action)){
                appToForegroundRun();
            }
        }
    }

    protected void exit(){
        stopSelf();
    }

    protected void timerUpdate(){

    }

    protected void appToForegroundRun(){

    }

    /**
     * 获取桌面
     *
     * @param context
     * @return
     */
    private List<String> getLaunchers(Context context) {
        List<String> packageNames = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null) {
                packageNames.add(resolveInfo.activityInfo.processName);
                packageNames.add(resolveInfo.activityInfo.packageName);
            }
        }
        return packageNames;
    }

    /**
     * 是否是桌面
     *
     * @param context
     * @return
     */
    public boolean isLauncherForeground(Context context) {
        boolean isLauncherForeground = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<String> lanuchers = getLaunchers(context);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (lanuchers.contains(activeProcess)) {
                            isLauncherForeground = true;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (lanuchers.contains(componentInfo.getPackageName())) {
                isLauncherForeground = true;
            }
        }

        return isLauncherForeground;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        mHomeWatcher.setOnHomePressedListener(null);
        mHomeWatcher.stopWatch();
        super.onDestroy();
        LogUtils.i("----------------onDestroy------------");
    }
}
