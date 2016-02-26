package com.example.practice1215.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by guanjun on 2015/12/16.
 */
public class BitmapUtils {
    public BitmapUtils() {
    }

    public static Bitmap getBitmapThumbnail(String path, int width, int height) {
        Bitmap bitmap = null;
        //按比例缩小图片
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inSampleSize = 4;//宽和高都是原来的1/4
//		bitmap = BitmapFactory.decodeFile(path, opts);
        /**
         * 进一步，如何计算inSampleSize成为关键，BitmapFactory.Options提供了另外一个
         * 成员变量inJustDecodeBound,设置inJustDecodeBounds为true后，
         * decodeFile并不分配空间，但可计算出原始图片的长度和宽度，即opts.width和opts.height。
         * 有了这两个参数，再通过一定的算法，即可得到一个恰当的inSampleSize。
         */

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opt);
        opt.inSampleSize = Math.max((int) (opt.outHeight / (float) height), (int) (opt.outWidth / (float) width));
        opt.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, opt);
        return bitmap;
    }

    /**
     * 根据给定的宽高裁剪图片，是预览不变形
     *
     * @param bmp
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapThumbnail(Bitmap bmp, int width, int height) {
        Bitmap bitmap = null;
        if (bmp != null) {
            int bmpWidth = bmp.getWidth();
            int bmpHeight = bmp.getHeight();
            if (bmpHeight != 0 && 0 != bmpWidth) {
                Matrix matrix = new Matrix();
                float scaleWidth = ((float) width / bmpWidth);
                float scaleHeight = ((float) height / bmpHeight);
                matrix.postScale(scaleWidth, scaleHeight);
                bitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
            } else {
                bitmap = bmp;
            }
        }
        return bitmap;
    }

    /**
     * 压缩图片:按质量压缩
     */
    public static Bitmap compressBitmapByQuality(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//100表示不压缩，把压缩后的保存在baos中
        int option = 100;
        while (baos.toByteArray().length > 1024 * 100) { //循环压缩，使Bitmap小雨100K
            baos.reset(); //重置baos，即清空
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);//这里option是压缩比例；option%
            option -= 10;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(bais, null, null);
        return bitmap;
    }

    /**
     * 按图片比例大小压缩图片，由路径获取图片
     *
     * @param path
     * @return
     */
    public static Bitmap compressByScaleWithPath(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //开始读入图片，把options.inJustDecodeBounds设回true了
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); //此时返回的bitmap为空
        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        //现在主流手机为800*480分辨率的，所以设置
        float hh = 800f; //高为800
        float ww = 480f; //宽为480
        //缩放比例，由于固定比例缩放，所以，宽高计算一个比例即可
        int be = 1; //1表示不缩放
        if (w > h && w > ww) {    //宽度大的话，根据宽度固定大小缩放
            be = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {    //高度大的话根据高度进行缩放
            be = (int) (options.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }

        options.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意：这是options.inJustDecodeBounds已经设置为false;返回的bitmap部位空
        bitmap = BitmapFactory.decodeFile(path, options);
        return compressBitmapByQuality(bitmap);    //压缩好比例后再进行质量压缩
    }

    /**
     * 按图片比例大小压缩图片，传入原Bitmap
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap compressByScaleWithBitmap(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length > 1024 * 1024) {    //判断如果图片大小大于1M，进行压缩，避免生成图片时溢出
            baos.reset();
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);  //压缩50%
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        //开始读入图片，把option.inJustDecodeBounds设为true
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(bais, null, options);
        options.inJustDecodeBounds = false;

        int w = options.outWidth;
        int h = options.outHeight;

        float ww = 800f;
        float hh = 480f;

        int be = 1;
        if (w > h && w > ww) {
            be = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (options.outHeight / hh);
        }

        if (be <= 0) {
            be = 1;
        }

        options.inSampleSize = be;
        bais = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(bais, null, options);
        return compressBitmapByQuality(bitmap);
    }

}
