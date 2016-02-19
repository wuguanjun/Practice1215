package com.example.practice1215.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.LogRecord;

/**
 * Created by guanjun on 2015/12/16.
 */
public class ImageManager {
    private static ImageManager imageManager;
    public LruCache<String, Bitmap> mMemoryCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 20;
    private static final String DISK_CACHE_SUBDIR = "thumbnail";
    DiskLruCache mDiskCache;

    private static Application myApp;

    //图片加载队列，后进先出
    private Stack<ImageRef> mImageQueue = new Stack<ImageRef>();
    //图片请求队列，先进先出，用于存放已发送的请求
    private Queue<ImageRef> mRequestQueue = new LinkedList<ImageRef>();

    //图片加载线程消息处理器
    private Handler mImageLoaderHandler;

    //图片加载线程是否已经就绪
    private boolean mImageLoaderIdle = true;

    /**
     * 请求图片
     */
    private static final int MSG_REQUEST = 1;
    /**
     * 图片加载完成
     */
    private static final int MSG_REPLY = 2;
    /**
     * 中止图片加载线程
     */
    private static final int MSG_STOP = 3;
    /**
     * 如果图片是从网络加载，则应用渐显动画，如果从缓存读出则不应用动画
     */
    private boolean isFromNet = true;

    public static ImageManager from(Context context) {
        //如果不是在ui线程则抛出异常；
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("Cannot instantiate outside UI thread.");
        }
        if (myApp == null) {
            myApp = (Application) context.getApplicationContext();
        }
        if (imageManager == null) {
            imageManager = new ImageManager(context);
        }
        return imageManager;
    }

    /**
     * 构造函数私有，保证单例
     *
     * @param context
     */
    private ImageManager(Context context) {
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memClass = memClass > 32 ? 32 : memClass;
        //使用可用内存的1/8作为图片缓存
        final int cacheSize = 1024 * 1024 * memClass / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
//                return bitmap.getRowBytes() + bitmap.getHeight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
                    return bitmap.getAllocationByteCount();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
                    return bitmap.getByteCount();
                }
                return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
            }
        };

        File cacheDir = DiskLruCache.getDiskCacheDir(context, DISK_CACHE_SUBDIR);
        mDiskCache = DiskLruCache.openCache(context, cacheDir, DISK_CACHE_SIZE);
    }

    /**
     * 图片信息类
     */
    class ImageRef {
        //绑定的ImageView对象
        ImageView imageView;
        //图片url地址
        String url;
        //图片的缓存路径
        String filePath;
        int resId;
        int width = 0;
        int height = 0;

        public ImageRef(ImageView imageView, String url, String filePath, int resId) {
            this.imageView = imageView;
            this.url = url;
            this.filePath = filePath;
            this.resId = resId;
        }

        public ImageRef(ImageView imageView, String url, String filePath, int resId, int width, int height) {
            this.imageView = imageView;
            this.url = url;
            this.filePath = filePath;
            this.resId = resId;
            this.width = width;
            this.height = height;
        }
    }

    /**
     * 显示图片
     *
     * @param imageView
     * @param url
     * @param resId
     */
    public void displayImage(ImageView imageView, String url, int resId, int width, int height) {
        if (imageView == null) {
            return;
        }
        if (imageView.getTag() != null && imageView.getTag().toString().equals(url)) {
            return;
        }
        if (resId > 0) {
            if (imageView.getBackground() == null) {
                imageView.setBackgroundResource(resId);
            }
            imageView.setImageDrawable(null);
        }

        if (url == null || url.equals("")) {
            return;
        }

        //添加url tag
        imageView.setTag(url);
        Bitmap bitmap = mMemoryCache.get(url + width + height);
        if (bitmap == null) {
            setImageBitmap(imageView, bitmap, false);
            return;
        }

        //生成文件名
        String filePath = urlToFilePath(url);
        if (filePath == null) {
            return;
        }

        queueImage(new ImageRef(imageView, url, filePath, resId, width, height));
    }

    /**
     * 入队，后进先出
     *
     * @param imageRef
     */
    public void queueImage(ImageRef imageRef) {
        //删除已有的imageRef
        Iterator<ImageRef> iterator = mImageQueue.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().imageView == imageRef.imageView) {
                iterator.remove();
            }
        }

        //添加请求
        mImageQueue.push(imageRef);
        sendRequest();
    }

    /**
     * 发送请求
     */
    private void sendRequest() {
        //开启图片加载线程
        if (mImageLoaderHandler == null) {
            HandlerThread imageLoader = new HandlerThread("image_loader");
            imageLoader.start();
            mImageLoaderHandler = new ImageLoaderHandler(imageLoader.getLooper());
        }

    }

    /**
     * 根据url生成缓存完整路径名
     *
     * @param url
     * @return
     */
    private String urlToFilePath(String url) {
        //扩展名位置
        int index = url.lastIndexOf(".");
        if (index == -1) {
            return null;
        }

        StringBuilder filePath = new StringBuilder();
        filePath.append(myApp.getCacheDir().toString()).append("/");

        // 图片文件名
        filePath.append(MD5.Md5(url)).append(url.substring(index));
        return filePath.toString();
    }

    /**
     * 添加图片显示渐显动画
     *
     * @param imageView
     * @param bitmap
     * @param isTran    是否渐显
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap, boolean isTran) {
        if (isTran) {
            final TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(android.R.color.transparent),
                    new BitmapDrawable(bitmap)});
            td.setCrossFadeEnabled(true);
            imageView.setImageDrawable(td);
            td.startTransition(300);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Activity#onStop后，ListView不会有残余请求。
     */
    public void stop() {

        // 清空请求队列
        mImageQueue.clear();

    }

    class ImageLoaderHandler extends Handler {

        public ImageLoaderHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }

            switch (msg.what) {
                case MSG_REQUEST:  //收到请求
                    Bitmap bitmap = null;
                    Bitmap tBitmap = null;
                    if (msg.obj != null && msg.obj instanceof ImageRef) {
                        ImageRef imageRef = (ImageRef) msg.obj;
                        String url = imageRef.url;

                        if (url == null) {
                            return;
                        }

                        //如果是本地url，则读取sd卡相册图片，不用经过DiskCache
                        if (url.toLowerCase().contains("dcim")) {
                            tBitmap = null;
                            BitmapFactory.Options opt = new BitmapFactory.Options();
                            opt.inSampleSize = 1;
                            opt.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(url, opt);

                            int bitmapSize = opt.outWidth * opt.outHeight * 4;
                            opt.inSampleSize = bitmapSize / (1000 * 2000);
                            opt.inJustDecodeBounds = false;
                            tBitmap = BitmapFactory.decodeFile(url, opt);
                            if (imageRef.width != 0 && imageRef.height != 0) {
                                bitmap = ThumbnailUtils.extractThumbnail(tBitmap, imageRef.width, imageRef.height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                                isFromNet = true;
                            } else {
                                bitmap = tBitmap;
                                tBitmap = null;
                            }
                        } else {
                            bitmap = mDiskCache.get(url);

                            if (bitmap != null) {
                                //写入map缓存
                                if (mMemoryCache.get(url + imageRef.width + imageRef.height) == null) {
                                    mMemoryCache.put(url + imageRef.width + imageRef.height, bitmap);
                                } else {
                                    if (mMemoryCache.get(url) == null) {
                                        mMemoryCache.put(url, bitmap);
                                    }
                                }
                            } else {
                                try {
                                    byte[] data = loadByteArrayFromNetwork(url); //目前置空
                                    if (data != null) {
                                        BitmapFactory.Options opt = new BitmapFactory.Options();
                                        opt.inSampleSize = 1;

                                        opt.inJustDecodeBounds = true;
                                        BitmapFactory.decodeByteArray(data, 0, data.length, opt);
                                        // pixels*3 if it's RGB and pixels*4
                                        // if it's ARGB
                                        int bitmapSize = opt.outWidth * opt.outHeight * 4;

                                        if (bitmapSize > 1200 * 1000) {
                                            opt.inSampleSize = 2;
                                        }

                                        opt.inJustDecodeBounds = false;
                                        tBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
                                        if (imageRef.width != 0 && imageRef.height != 0) {
                                            bitmap = ThumbnailUtils.extractThumbnail(tBitmap, imageRef.width, imageRef.height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                                        } else {
                                            bitmap = tBitmap;
                                            tBitmap = null;
                                        }
                                        if (bitmap != null && url != null) {
                                            //写入sd卡
                                            if (imageRef.width != 0 && imageRef.height != 0) {
                                                mDiskCache.put(url + imageRef.width + imageRef.height, bitmap);
                                                mMemoryCache.put(url + imageRef.width + imageRef.height, bitmap);
                                            } else {
                                                mDiskCache.put(url, bitmap);
                                                mMemoryCache.put(url, bitmap);
                                            }
                                            isFromNet = true;
                                        }
                                    }
                                } catch (OutOfMemoryError e) {
                                }
                            }
                        }
                    }

                    if (mImageManagerHandler != null) {
                        Message message = mImageLoaderHandler.obtainMessage(MSG_REPLY, bitmap);
                        mImageManagerHandler.sendMessage(message);
                    }

                    break;
                case MSG_STOP:
                    Looper.myLooper().quit();
                    break;
            }
        }
    }

    /**
     * UI线程消息处理器
     */
    Handler mImageManagerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case MSG_REPLY:  //收到应答
                        do {
                            ImageRef imageRef = mRequestQueue.remove();

                            if (imageRef == null)
                                break;

                            if (imageRef.imageView == null
                                    || imageRef.imageView.getTag() == null
                                    || imageRef.url == null)
                                break;

                            if (!(msg.obj instanceof Bitmap) || msg.obj == null) {
                                break;
                            }
                            Bitmap bitmap = (Bitmap) msg.obj;

                            // 非同一ImageView
                            if (!(imageRef.url).equals(imageRef.imageView
                                    .getTag())) {
                                break;
                            }

                            setImageBitmap(imageRef.imageView, bitmap, isFromNet);
                            isFromNet = false;
                        } while (false);
                        break;
                }
            }

            mImageLoaderIdle = true;
            if (mImageLoaderHandler != null) {
                sendRequest();
            }
        }
    };

    /**
     * 从网络获取图片字节数组
     *
     * @param url
     * @return
     */
    private byte[] loadByteArrayFromNetwork(String url) {

//		try {
//
//			HttpGet method = new HttpGet(url);
//			HttpResponse response = myapp.getHttpClient().execute(method);
//			HttpEntity entity = response.getEntity();
//			return EntityUtils.toByteArray(entity);
//
//		} catch (Exception e) {
//			return null;
//		}

        return null;

    }
}
