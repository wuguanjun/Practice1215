package com.example.practice1215.application;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yolanda.nohttp.NoHttp;

import java.util.logging.Logger;

/**
 * Created by guanjun on 2015/12/15.
 */
public class PracticeApplication extends Application {
    private static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        initImageLoader(getApplicationContext());

        //初始化NoHttp
        NoHttp.init(this);
        //设置调试模式
        com.yolanda.nohttp.Logger.setDebug(true);
        com.yolanda.nohttp.Logger.setTag("NoHttpSample");
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.MAX_PRIORITY).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Application getInstance() {
        return _instance;
    }
}
