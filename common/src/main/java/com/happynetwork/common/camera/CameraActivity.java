package com.happynetwork.common.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 *@author: Tom.yuan
 *@create: 2016-8-31
 *@describe: 拍照
 * 
 **/
public class CameraActivity extends Activity {
	Preview preview;
	private String filePath;
    private ImageButton cameraBtn,otherCameraBtn;
    private Matrix matrix = new Matrix();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xf_common_camera);
		Bundle bundle = getIntent().getExtras();
		filePath = bundle.getString("filePath");
		otherCameraBtn = (ImageButton)findViewById(R.id.cameraother_btn);
        otherCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preview != null)preview.changeCamera();
            }
        });
        otherCameraBtn.setVisibility(View.VISIBLE);
        cameraBtn = (ImageButton)findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preview != null)preview.camera.takePicture(shutterCallback, rawCallback,jpegCallback);
            }
        });
		preview = new Preview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		LogUtils.d("====>>onCreate");
		
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			LogUtils.d("====>>onShutter");
		}
	};
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			LogUtils.d("====>>onPictureTaken");
		}
	};
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Parameters ps = camera.getParameters();
			if(ps.getPictureFormat() == PixelFormat.JPEG){
				//停止拍照
			    camera.stopPreview();
			    //旋转处理
			    Bitmap cameraBitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
                if(cameraBitmap != null && matrix != null && preview != null){
                    matrix.postRotate(90,cameraBitmap.getWidth()/2,cameraBitmap.getHeight()/2);
                    if(preview.getCameraPosition() == 0){
                        matrix.postScale(1,-1,cameraBitmap.getWidth()/2,cameraBitmap.getHeight()/2);
                    }
                    Bitmap resizedBitmap = Bitmap.createBitmap(cameraBitmap,0,0,cameraBitmap.getWidth(),cameraBitmap.getHeight(),matrix,true);
                    //存储拍照获得的图片
                    bmpsavejpg(resizedBitmap);
                    Intent resultData = new Intent();
                    setResult(RESULT_OK, resultData);
                    finish();
                }
			}
		}
	};
	
	 /**
	  * 保存图片至sd卡<br />把bmp保存为jpg
	  * @param cameraBitmap 需要保存的位图
	  * @return 图片路径
	  */
	 private String bmpsavejpg(Bitmap cameraBitmap){
		 LogUtils.i(filePath);
	    	try {	    		
	    		File file = new File(filePath);
	    		if(!file.exists())file.createNewFile(); 	//创建文件
	    		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
	    		cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	    		fos.flush();
	    		fos.close();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		return null;
	    	}
	    	return filePath;
	 }
}