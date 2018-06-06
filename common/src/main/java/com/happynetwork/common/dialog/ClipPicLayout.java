package com.happynetwork.common.dialog;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.happynetwork.common.R;
import com.happynetwork.common.camera.ClipView;
import com.happynetwork.common.events.ClipPicEvent;
import com.happynetwork.common.events.DoUploadImgListener;
import com.happynetwork.common.utils.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 编辑上传图片
 */
public class ClipPicLayout extends BaseWindowManagerLayout implements View.OnTouchListener {
    private ClipPicEvent clipPicEvent;
    private ImageView srcPic;
    private ClipView clipview;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private Matrix rotateMatrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private Bitmap bitmap;
    private ImageButton xzBtn;
    private FrameLayout contine_c;
    private String filePath;
    private String filePathThumb;
    private DoUploadImgListener doUploadImgListener;

    public String getFilePath() {
        return filePath;
    }

    public void setDoUploadImgListener(DoUploadImgListener listener){
        doUploadImgListener = listener;
    }

    public boolean isCamera() {
        return isCamera;
    }

    private boolean isCamera = false;
    private Button cancelBtn, submitBtn;

    public void setClipPicEvent(ClipPicEvent clipPicEvent) {
        this.clipPicEvent = clipPicEvent;
    }

    public void setFilePath(String filePath, boolean isCamera) {
        this.filePath = filePath;
        this.isCamera = isCamera;
        String dir = getContext().getCacheDir().getAbsolutePath();
        this.filePathThumb = dir +"thumb.jpg";
        /*if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"xftemp"+File.separator;
            File dirfile = new File(dir);
            if (!dirfile.exists()) {
                dirfile.mkdirs();
            }
            this.filePathThumb = dir +"thumb.jpg";
        }else {
            LogUtils.w("sd卡不可用！");
        }*/
        initClipView();
    }

    /**
     * @param context
     */
    public ClipPicLayout(Activity context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ClipPicLayout(Activity context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipPicLayout(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initView() {
        wmParams.gravity = Gravity.CENTER | Gravity.CENTER;
        wmParamsParent.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParamsParent.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.xf_common_photo_clip, this);
        try {
            wManager.addView(this, wmParamsParent);
        }catch (Exception e){
            LogUtils.w(e.toString());
        }
        this.setClickable(true);
        invalidate();

        xzBtn = (ImageButton) mContentView.findViewById(R.id.xuanzuan_id);
        xzBtn.setOnClickListener(new MyOnClickListener());
        contine_c = (FrameLayout) findViewById(R.id.contine_id);
        srcPic = (ImageView) this.findViewById(R.id.src_pic);
        srcPic.setOnTouchListener(this);
        cancelBtn = (Button) mContentView.findViewById(R.id.cancel_id);
        submitBtn = (Button) mContentView.findViewById(R.id.submit_id);

        cancelBtn.setOnClickListener(new MyOnClickListener());
        submitBtn.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.xuanzuan_id) {
                LogUtils.i("旋转90度");
                if (bitmap == null || rotateMatrix == null || srcPic == null) {
                    return;
                }
                rotateMatrix.postRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, true);
                setMatrix(resizedBitmap);
                srcPic.setImageBitmap(resizedBitmap);
            } else if (id == R.id.cancel_id) {
                clipPicEvent.backClick(isCamera);
            } else if (id == R.id.submit_id) {
                getBitmap();
                if(doUploadImgListener != null){
                    doUploadImgListener.doUpload(filePathThumb);
                }
            }
        }
    }

    /**
     * 获取裁剪框内截图
     *
     * @return
     */
    private void getBitmap() {
        srcPic.setDrawingCacheEnabled(true);
        Bitmap resizedBitmap = Bitmap.createBitmap(srcPic.getDrawingCache(), clipview.getClipLeftMargin(), clipview.getClipTopMargin(),
                clipview.getClipWidth(), clipview.getClipHeight(), rotateMatrix, true);
        srcPic.setDrawingCacheEnabled(false);
        bmpsavejpg(resizedBitmap);
    }

    private String bmpsavejpg(Bitmap cameraBitmap) {
        LogUtils.i(filePathThumb);
        try {
            File file = new File(filePathThumb);
            if (!file.exists()) file.createNewFile();    //创建文件
            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
            cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return filePathThumb;
    }

    public Bitmap loadBigImage(String fileurl) {
        // 获取手机分辨率
        Display display = wManager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        // 获取图片尺寸
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileurl, opts);
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 计算比例
        int dx = imgWidth / width;
        int dy = imgHeight / height;
        // 选择大的比例
        int scale = 1;
        if (dx >= dy && dx >= 1) {
            scale = dx;
        } else if (dy >= dx && dy >= 1) {
            scale = dy;
        }
        // 通过设置的选择读取图片
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        // 读取图片
        return BitmapFactory.decodeFile(fileurl, opts);
    }

    private void setMatrix(Bitmap bit) {
        int clipHeight = clipview.getClipHeight();
        int clipWidth = clipview.getClipWidth();
        int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
        int midY = clipview.getClipTopMargin() + (clipHeight / 2);

        int imageWidth = bit.getWidth();
        int imageHeight = bit.getHeight();
        // 按裁剪框求缩放比例
        float scale = (clipWidth * 1.0f) / imageWidth;
        if (imageWidth > imageHeight) {
            scale = (clipHeight * 1.0f) / imageHeight;
        }
        // 起始中心点
        float imageMidX = imageWidth * scale / 2;
        float imageMidY = clipview.getCustomTopBarHeight() + imageHeight * scale / 2;
        matrix = new Matrix();
        // 缩放
        matrix.postScale(scale, scale);
        // 平移
        matrix.postTranslate(midX - imageMidX, midY - imageMidY);
        srcPic.setImageMatrix(matrix);
    }

    public void initClipView() {
        bitmap = loadBigImage(filePath);
        clipview = new ClipView(mContext);
        clipview.addOnDrawCompleteListener(new ClipView.OnDrawListenerComplete() {

            public void onDrawCompelete() {
                clipview.removeOnDrawCompleteListener();
                srcPic.setScaleType(ImageView.ScaleType.MATRIX);
                setMatrix(bitmap);
                srcPic.setImageBitmap(bitmap);
            }
        });
        contine_c.addView(clipview, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 设置开始点位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        srcPic.setImageMatrix(matrix);
        return true;
    }

    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public void doBack(KeyEvent event) {
        super.doBack(event);
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            clipPicEvent.backClick(isCamera);
        }
    }
}
