package com.example.practice1215.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

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
}
