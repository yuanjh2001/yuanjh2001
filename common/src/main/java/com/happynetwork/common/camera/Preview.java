package com.happynetwork.common.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.happynetwork.common.activitys.PersionCenterActivity;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;


import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Tom.yuan
 * @create: 2016-8-31
 **/
public class Preview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    public Camera camera;
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
    private int cameraCount = 0;
    private Camera.CameraInfo cameraInfo;
    public boolean isCreated = false;
    private boolean isLandscape = true;
    private Context mContext;
    private int[] sizs;

    public Preview(Context context) {
        this(context, false);
    }

    public Preview(Context context, boolean isLandscape) {
        this(context, isLandscape,new int[]{800,600});
    }

    public Preview(Context context, boolean isLandscape,int [] s) {
        super(context);
        this.mContext = context;
        this.isLandscape = isLandscape;
        sizs = s;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cameraCount = 0;
        cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
    }

    public int getCameraPosition() {
        return cameraPosition;
    }

    //开始拍照时调用
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.i("+---------------+");
        try {
            camera = Camera.open();
        } catch (Exception e) {
            LogUtils.w(e.toString());

            isCreated = false;
            if (mContext != null && mContext instanceof Activity) {
                ToastUtils.createToast((Activity) mContext, "打开相机失败！");
                ((Activity) mContext).finish();
            } else {
                Toast.makeText(getContext(), "打开相机失败！", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (!isLandscape) camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            //关闭相机
            camera.release();
            camera = null;
            e.printStackTrace();
        }
        isCreated = true;
    }

    public void changeCamera() {
        if (!isCreated) {
            return;
        }
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    if (!isLandscape) camera.setDisplayOrientation(90);
                    try {
                        camera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setCameraPs();
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    if (!isLandscape) camera.setDisplayOrientation(90);
                    try {
                        camera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setCameraPs();
                    cameraPosition = 1;
                    break;
                }
            }
        }

    }

    /**
     * 拍照结束时调用
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.i("surfaceDestroyed");
        if (camera == null) {
            return;
        }
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    /**
     * 拍照状态改变时调用
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        LogUtils.i("+++++++++++++++++++++++++++++++" + getResources().getConfiguration().orientation);
        setCameraPs();
    }

    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera c) {
            if (success) {
                LogUtils.i("---------自动聚焦-------");
                //if(camera != null)camera.setOneShotPreviewCallback(null);
            }
        }
    };

    private void setCameraPs() {
        if (camera == null) {
            return;
        }
        Parameters parameters = camera.getParameters();
        //设置图片尺寸
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        if (list != null && !list.isEmpty()) {
            int i = 0;
            Camera.Size maxSize = list.get(i);
            for (Camera.Size s : list) {
                if (s.width > maxSize.width && s.height > maxSize.height) {
                    maxSize = s;
                    i++;
                }
            }
            LogUtils.i(" width=" + maxSize.width + " height=" + maxSize.height + " i=" + i);
            parameters.setPreviewSize(maxSize.width, maxSize.height);
        }

        parameters.setPictureSize(800, 600);
        List<Camera.Size> piclist = parameters.getSupportedPictureSizes();
        if (piclist != null && !piclist.isEmpty()) {

            Collections.sort(piclist, new Comparator<Camera.Size>() {

                @Override
                public int compare(Camera.Size o1, Camera.Size o2) {
                    int i = o1.width - o2.width;
                    if (i == 0) {
                        return o1.height - o2.height;
                    }
                    return i;
                }
            });
            int i = 0;
            Camera.Size maxSecSize = piclist.get(i);
            Camera.Size maxSize = piclist.get(i);
            if (sizs[0] > 0) {
                for (Camera.Size s : piclist) {
                    LogUtils.i(s.width +" | " + s.height);
                    if (s.width >= sizs[0] && s.height >= sizs[1]) {
                        maxSecSize = s;
                        LogUtils.i(maxSecSize.width + " * " + maxSecSize.height + " i = " + i);
                        break;
                    }
                    i++;
                }
            } else {
                for (Camera.Size s : piclist) {
                    if (s.width > maxSize.width && s.height > maxSize.height) {
                        maxSize = s;
                        i++;
                    }
                }
                LogUtils.i(" width=" + maxSize.width + " height=" + maxSize.height + " i=" + i);
                i = 0;
                if (maxSecSize.width == maxSize.width && maxSecSize.height == maxSize.height) {
                    maxSecSize = piclist.get(piclist.size() - 1);
                }
                for (Camera.Size s : piclist) {
                    if (s.width > maxSecSize.width && s.height > maxSecSize.height && s.width != maxSize.width && s.height != maxSize.height) {
                        maxSecSize = s;
                        i++;
                    }
                }
            }
            LogUtils.i(" width=" + maxSecSize.width + " height=" + maxSecSize.height + " i=" + i);
            parameters.setPictureSize(maxSecSize.width, maxSecSize.height);
        }

        //设置图片格式
        parameters.setPictureFormat(PixelFormat.JPEG);
        //设置图片预览的帧数
        parameters.setPreviewFrameRate(24);

        camera.setParameters(parameters);//模拟器测试需要注释掉此行，否则报错
        camera.startPreview();
        camera.autoFocus(mAutoFocusCallback);
    }
}
