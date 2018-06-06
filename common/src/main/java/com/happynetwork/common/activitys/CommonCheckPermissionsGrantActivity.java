/**
 *
 */
package com.happynetwork.common.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.happynetwork.common.utils.CheckPermisionUtil;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 */
public class CommonCheckPermissionsGrantActivity extends FragmentActivity {
    private String[] ps = new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.READ_CONTACTS,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE};
    private static final int GETPERMISSION_SUCCESS = 1;//获取权限成功
    private static final int GETPERMISSION_FAILER = 2;//获取权限失败
    private MyHandler myHandler = new MyHandler();

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            LogUtils.w("Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LogUtils.w("Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    public void requestAllPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(CommonCheckPermissionsGrantActivity.this,
               ps, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                LogUtils.i("onGranted");
                myHandler.sendEmptyMessage(GETPERMISSION_SUCCESS);
            }

            @Override
            public void onDenied(String permission) {
                LogUtils.i("onDenied");
                myHandler.sendEmptyMessage(GETPERMISSION_FAILER);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETPERMISSION_SUCCESS:
                    LogUtils.i("获取全部权限成功");
                    break;
                case GETPERMISSION_FAILER:
                    Toast.makeText(CommonCheckPermissionsGrantActivity.this, "应用没有获取权限，请重新打开应用，或者到设置页面添加权限以后再从新打开。", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    protected void checkSystemAlertPermission() {
        boolean flag = true;
        LogUtils.i("申请系统弹窗权限 " + Build.MANUFACTURER);
        if ("Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            String miuiVer = getSystemProperty("ro.miui.ui.version.name");
            LogUtils.i(miuiVer);
            if (("V8".equals(miuiVer) || "V7".equals(miuiVer))&& !CheckPermisionUtil.isFloatWindowPermisionGranted(this)) {
                LogUtils.i("miui !!! "+miuiVer);
                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", getPackageName());
                try{
                    startActivityForResult(intent, 36);
                }catch (Exception e){
                    LogUtils.w(e.toString());
                    showMissingPermissionDialog();
                    return;
                }
                flag = false;
                Toast.makeText(this, "请允许显示悬浮窗！", Toast.LENGTH_LONG).show();
            }else if(("V8".equals(miuiVer) || "V7".equals(miuiVer)) && CheckPermisionUtil.isFloatWindowPermisionGranted(this)){
                showSysAlert();
                return;
            }
        }

        if (flag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 判断是否有SYSTEM_ALERT_WINDOW权限
                if (Settings.canDrawOverlays(this)) {
                    LogUtils.d("==== canDrawOverlays tttt =====");
                    showSysAlert();
                }else {
                    LogUtils.d("--requestPermissionsIfNecessaryForResult--");
                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                            new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionsResultAction() {

                                @Override
                                public void onGranted() {
                                    LogUtils.i("granted ");
                                    showSysAlert();
                                }

                                @Override
                                public void onDenied(String permission) {
                                    LogUtils.i("onDenied");
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:" + getPackageName()));
                                    // REQUEST_CODE2是本次申请的请求码
                                    LogUtils.i("=======3636=====");
                                    try{
                                        startActivityForResult(intent, 3636);
                                    }catch (Exception e){
                                        LogUtils.w(e.toString());
                                        showMissingPermissionDialog();
                                    }
                                }
                            }
                    );
                }
            }else {
                LogUtils.d("==== <23 =====");
                showSysAlert();
            }
        }
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限提示");
        builder.setMessage("当前应用缺少必要权限。\n" +
                "请在设置里或权限管理工具里打开悬浮窗或系统弹窗权限。然后重新打开");

        builder.setNegativeButton("退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doExit();
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    protected void doExit(){
        finish();
    }

    protected void checkStoragePermission() {
        LogUtils.i("申请SD卡读写权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        showStorageAlert();
                    }

                    @Override
                    public void onDenied(String permission) {
                        onStorageDenied();
                    }
                }
        );
    }
    protected void checkReadStoragePermission() {
        LogUtils.i("申请SD卡读写权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        showStorageAlert();
                    }

                    @Override
                    public void onDenied(String permission) {
                        onStorageDenied();
                    }
                }
        );
    }

    protected void checkRecordAudioPermission() {
        LogUtils.i("申请录音权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        showRecordAudioAlert();
                    }

                    @Override
                    public void onDenied(String permission) {
                        showRecordAudioDenied();
                    }
                }
        );
    }

//Manifest.permission.CALL_PRIVILEGED,Manifest.permission.CALL_PHONE
protected void checkCallPrivilegedPermission() {
    LogUtils.i("申请系统专属权限");
    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
            new String[]{Manifest.permission.CALL_PRIVILEGED}, new PermissionsResultAction() {

                @Override
                public void onGranted() {
                    showRecordAudioAlert();
                }

                @Override
                public void onDenied(String permission) {
                    showRecordAudioDenied();
                }
            }
    );
}
    //ACCESS_COARSE_LOCATION

    protected void checkAccessCoarseLocationPermission() {
        LogUtils.i("申请系统访问位置权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        showRecordAudioAlert();
                    }

                    @Override
                    public void onDenied(String permission) {
                        showRecordAudioDenied();
                    }
                }
        );
    }

    protected void checkCallPrhonePermission() {
        LogUtils.i("申请callphone权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.CALL_PHONE}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        showRecordAudioAlert();
                    }

                    @Override
                    public void onDenied(String permission) {
                        showRecordAudioDenied();
                    }
                }
        );
    }

    protected void checkPhoneStatePermission() {
        LogUtils.i("申请读取手机状态权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        showPhoneState();
                    }

                    @Override
                    public void onDenied(String permission) {
                        onPhoneStateDenied();
                    }
                }
        );
    }

    protected void checkCameraPermission() {
        LogUtils.i("申请调用相机权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.CAMERA}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        if(CheckPermisionUtil.isCameraPermisionGranted(CommonCheckPermissionsGrantActivity.this)){
                            showCamera();
                        }else {
                            onCameraDenied();
                        }
                    }

                    @Override
                    public void onDenied(String permission) {
                        onCameraDenied();
                    }
                }
        );
    }

    protected void checkContactsPermission() {
        LogUtils.i("申请读写通讯录权限");
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.READ_CONTACTS}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        showContacts();
                    }

                    @Override
                    public void onDenied(String permission) {
                        onContactsDenied();
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
        LogUtils.i("onRequestPermissionsResult");
    }

    protected void showStorageAlert() {
        LogUtils.i("已获取SD卡读写权限");
    }
    protected void showRecordAudioAlert() {
        LogUtils.i("已获取录音权限");
    }
    protected void showRecordAudioDenied() {
        LogUtils.i("无法继续，需要获取录音权限");
    }

    protected void showSysAlert() {
        LogUtils.i("已获取系统弹窗权限");
    }

    protected void showCamera() {
        LogUtils.i("已获取调用相机权限");
    }

    protected void showPhoneState() {
        LogUtils.i("已获取读取手机状态权限");
    }

    protected void showContacts() {
        LogUtils.i("已获取通讯录读写权限");
    }

    protected void onCameraDenied() {
        doDenied("无法继续,需要调用相机");
    }

    protected void onPhoneStateDenied() {
        doDenied("无法继续,需要读取手机状态权限");
    }

    protected void onSysAlertDenied() {
        LogUtils.i("拒绝悬浮框");
        doDenied("无法继续,需要打开悬浮框");
    }

    protected void onStorageDenied() {
        doDenied("无法继续,需要读写sd卡权限");
    }

    void onContactsDenied() {
        doDenied("此功能需要读取通讯录权限，否则无法继续！");
    }

    public void doDenied(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(CheckPermisionUtil.isFloatWindowPermisionGranted(this) + "requestCode: " + requestCode);
        if (requestCode == 36) {
            if (CheckPermisionUtil.isFloatWindowPermisionGranted(this)) {
                if ("Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
                    String miuiVer = getSystemProperty("ro.miui.ui.version.name");
                    LogUtils.i(miuiVer);
                    if ("V8".equals(miuiVer)) {
                        LogUtils.i("miui v8 !!!!!!!");
                        showSysAlert();
                    }else if("V7".equals(miuiVer)){
                        LogUtils.i("miui v7 !!!!!!!");
                        showSysAlert();
                    }else {
                        LogUtils.i("=== ooo ===");
                        showSysAlert();
                    }
                }else {
                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                            new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionsResultAction() {

                                @Override
                                public void onGranted() {
                                    showSysAlert();
                                }

                                @Override
                                public void onDenied(String permission) {
                                    onSysAlertDenied();
                                }
                            }
                    );
                }

            } else {
                doDenied("无法继续,需要打开悬浮框");
            }
        }else if (requestCode == 3636) {
            LogUtils.i("=======result == 3636=====");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 判断是否有SYSTEM_ALERT_WINDOW权限
                if (Settings.canDrawOverlays(this)) {
                    showSysAlert();
                }else {
                    onSysAlertDenied();
                }
            }
        }
    }
}
