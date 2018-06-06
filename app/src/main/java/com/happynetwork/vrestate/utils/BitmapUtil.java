package com.happynetwork.vrestate.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.happynetwork.vrestate.R;

/**
 * 图片压缩转换
 * 
 * @author chenggang
 * 
 */
public class BitmapUtil {

    private final static String TAG = "BitmapUtil";

    /**
     * 质量压缩
     * 
     * @param image
     * @param maxKB
     *            ，最大KB
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int maxKB) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxKB) { // 循环判断如果压缩后图片是否大于maxKB
                                                           // kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 根据路径获取图片并压缩
     * 
     * @param srcPath
     * @param maxKB
     * @return
     */
    public static Bitmap compressBySrc(String srcPath, int maxKB) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        float be = 1;
        if (w > ww) {
            be = (newOpts.outWidth / ww);
        } else if (h > hh) {
            be = (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = (int) (be);// 设置采样率
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 根据Bitmap图片压缩
     * 
     * @param image
     * @param maxKB
     * @return
     */
    public static Bitmap compressByBitmap(Bitmap image, int maxKB) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap, maxKB);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * bitmap转inputstream
     * 
     * @param bm
     * @return
     */
    public static InputStream BitmapToStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    
    public static Bitmap fileToBitmap(String file){
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @Description: TODO 将图片转换为圆形
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
    
    public static void saveBitmap(Bitmap bm,String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 
     * @param context
     * @param uri
     * @return
     */
    public  static Bitmap getBitmapFromUri(Context context,Uri uri)
    {
     try
     {
      // 读取uri所在的图片
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
      return bitmap;
     }
     catch (Exception e)
     {
      Log.e("[Android]", e.getMessage());
      e.printStackTrace();
      return null;
     }
    }
    
    /**
    
     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
     * @return  缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
    {
     if(null == bitmap || edgeLength <= 0)
     {
      return  null;
     }
                                                                                  
     Bitmap result = bitmap;
     int widthOrg = bitmap.getWidth();
     int heightOrg = bitmap.getHeight();
                                                                                  
     if(widthOrg > edgeLength && heightOrg > edgeLength)
     {
      //压缩到一个最小长度是edgeLength的bitmap
      int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
      int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
      int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
      Bitmap scaledBitmap;
                                                                                   
            try{
             scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
             return null;
            }
                                                                                        
         //从图中截取正中间的正方形部分。
         int xTopLeft = (scaledWidth - edgeLength) / 2;
         int yTopLeft = (scaledHeight - edgeLength) / 2;
                                                                                      
         try{
          result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
          scaledBitmap.recycle();
         }
         catch(Exception e){
          return null;
         }       
     }
                                                                                       
     return result;
    }
    
    /**
     * 由颜色生成bitmap
     * @param color
     * @return
     */
    @SuppressLint("ResourceAsColor") 
    public static Bitmap getBackGroundBitmap(int color,int width, int height) {
        Paint p = new Paint();
        p.setColor(R.color.mask_color);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(R.color.mask_color);
//        canvas.drawColor(color);
        return bitmap;
    }
    
    public static Bitmap MaskBitmap(Bitmap bitmap, Bitmap mask, int size, Xfermode mode) {
        if (null == bitmap || mask == null) {
            return null;
        }
        //定义期望大小的bitmap
        Bitmap dstBmp = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        //定义一个画布
        Canvas canvas = new Canvas(dstBmp);
        //创建一个取消锯齿画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //定义需要绘制的某图片上的那一部分矩形空间
        Rect src = new Rect(0, 0, mask.getWidth(), mask.getHeight());
        //定义需要将上面的矩形绘制成新的矩形大小
        Rect dst = new Rect(0, 0, size, size);
        //将蒙版图片绘制成imageview本身的大小,这样从大小才会和UE标注的一样大
        canvas.drawBitmap(mask, src, dst, paint);
        //设置两张图片的相交模式
        //至于这个函数介绍参考网址:http://blog.csdn.net/wm111/article/details/7299294
        paint.setXfermode(mode);
        //将src修改为需要添加mask的bitmap大小,因为是要将此bitmap整个添加上蒙版
        src.right = bitmap.getWidth();
        src.bottom = bitmap.getHeight();
        //在已经绘制的mask上叠加bitmap
        canvas.drawBitmap(bitmap, src, dst, paint);
        return dstBmp;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

}
