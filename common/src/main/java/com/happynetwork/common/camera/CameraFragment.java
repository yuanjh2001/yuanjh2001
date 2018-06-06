package com.happynetwork.common.camera;

import android.app.Activity;
import android.support.v4.app.Fragment;
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
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CameraFragment extends Fragment {
	Preview preview;
	private String filePath;
    private ImageButton cameraBtn,otherCameraBtn;
    private Matrix matrix = new Matrix();
	private View rootView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null){
			Bundle bundle = getActivity().getIntent().getExtras();
			filePath = bundle.getString("filePath");
		}
		if(filePath == null || filePath.equals("")){
			getFilePath();
		}

		LogUtils.d(filePath);
	}

	public String getFilePath(){
		if(filePath == null || filePath.trim().equals("")){
			File dirfile = new File("/sdcard/");
			if (!dirfile.exists()) {
				dirfile.mkdirs();
			}
			filePath = "/sdcard/" + System.currentTimeMillis()+".jpg";
		}
		return filePath;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != rootView) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		} else {
			rootView = inflater.inflate(R.layout.xf_common_camera, null);
			otherCameraBtn = (ImageButton)rootView.findViewById(R.id.cameraother_btn);
			otherCameraBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(preview != null)preview.changeCamera();
				}
			});
			otherCameraBtn.setVisibility(View.VISIBLE);
			cameraBtn = (ImageButton)rootView.findViewById(R.id.camera_btn);
			cameraBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(preview != null)preview.camera.takePicture(shutterCallback, rawCallback,jpegCallback);
				}
			});
			preview = new Preview(getActivity());
			((FrameLayout) rootView.findViewById(R.id.preview)).addView(preview);
			LogUtils.d("====>>onCreateView");
		}
		return rootView;
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			LogUtils.d("====>>shutterCallback");
		}
	};
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			LogUtils.d("====>>rawCallback");
		}
	};
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
            LogUtils.d("====>>jpegCallback");
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
                    getActivity().setResult(Activity.RESULT_OK, resultData);
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