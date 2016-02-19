package com.example.practice1215.utils;

import java.io.File;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.v4.util.LruCache;

public class ImageShowManager {

	private static ImageShowManager imageManager;
	private static Application myApp;
	private LruCache<String, Bitmap> mMemoryCache;
	private DiskLruCache mDiskCache;
	// 硬盘缓存区域的大小，可以提升之前浏览过的图片的显示速度
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 20;
	// 硬盘缓存区域的文件名称
	private static final String DISK_CACHE_SUBDIR = "thumbnails";
	
	public static final int bitmap_width = 100;
	public static final int bitmap_height = 100;

	public static ImageShowManager from(Context context) {
		// 如果不在ui线程中，则抛出异常
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new RuntimeException("Cannot instantiate outside UI thread.");
		}

		if (myApp == null) {
			myApp = (Application) context.getApplicationContext();
		}

		if (imageManager == null) {
			imageManager = new ImageShowManager(myApp);
		}
		// 
		return imageManager;
	}

	/**
	 * 私有的构造器为了保持单例模式
	 */
	private ImageShowManager(Context context) {
		/********** 初始化内存缓冲区 ******/
		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		memClass = memClass > 32 ? 32 : memClass;
		// 使用可用内存的1/8作为图片缓存
		final int cacheSize = 1024 * 1024 * memClass / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};

		/********* 初始化硬盘缓冲区 *********/
		File cacheDir = DiskLruCache
				.getDiskCacheDir(context, DISK_CACHE_SUBDIR);
		mDiskCache = DiskLruCache.openCache(context, cacheDir, DISK_CACHE_SIZE);
		
	}
	
	/**
	 * 将图片保存至本地缓存
	 * @param key
	 * @param value
	 */
	public void putBitmapToDisk(String key,Bitmap value){
		mDiskCache.put(key, value);
	}
	
	/**
	 * 从硬盘缓存中读取图像
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFormDisk(String url){
		return mDiskCache.get(url);
	}
	
	/**
	 * 从内存缓存区中获取bitmap，根据特定的url
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromMemory(String url){
		return mMemoryCache.get(url);
	}
	
	/**
	 * 将新的图片添加至内存缓存区
	 * @param key
	 * @param value
	 */
	public void putBitmapToMemery(String key,Bitmap value){
		mMemoryCache.put(key, value);
	}
	
	
}
