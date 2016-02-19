package com.example.practice1215.utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by guanjun on 2015/12/28.
 */
public class ImageLoaderUtils {
    private ImageLoaderUtils() {
    }

    private static ImageLoaderUtils instance = null;

    public static synchronized ImageLoaderUtils getInstance() {
        if (instance == null) {
            instance = new ImageLoaderUtils();
        }
        return instance;
    }

    public void displayFromSDCard(String uri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage(uri, imageView);
    }
}
