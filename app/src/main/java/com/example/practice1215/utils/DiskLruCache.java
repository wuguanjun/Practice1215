package com.example.practice1215.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by guanjun on 2015/12/16.
 */
public class DiskLruCache {
    /**
     * 缓存文件前缀
     */
    public static String CACHE_FILENAME_PREFIX = "cache_";
    /**
     *
     */
    public static int MAX_REMOVALS = 4;
    /**
     * 起始容量
     */
    public static int INITIAL_CAPACITY = 32;
    /**
     *
     */
    public static float LOAD_FACTOR = 0.75f;

    /**
     * 缓存文件对象
     */
    private final File mCacheDir;
    private int cacheSize = 0;
    private int cacheByteSize = 0;
    private final int maxCacheItemSize = 64; // 64 item default
    private long maxCacheByteSize = 1024 * 1024 * 5; // 5MB default
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private int mCompressQuality = 70;
    private final int IO_BUFFER_SIZE = 4 * 1024;
    private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(INITIAL_CAPACITY, LOAD_FACTOR, true));

    /**
     * 缓存文件过滤，以CACHE_FILENAME_PREFIX开头的是缓存文件
     */
    private static FilenameFilter cacheNameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };

    /**
     * 不能直接访问实例化，用openCache实例化
     *
     * @param cacheDir
     * @param maxByteSize
     */
    private DiskLruCache(File cacheDir, long maxByteSize) {
        this.mCacheDir = cacheDir;
        this.maxCacheByteSize = maxByteSize;
    }

    /**
     * 获取DiskLruCache实例
     *
     * @param context
     * @param cacheDir
     * @param maxByteSize
     * @return
     */
    public static DiskLruCache openCache(Context context, File cacheDir, long maxByteSize) {
        if (!cacheDir.exists()) {  //不存在则创建缓存文件
            cacheDir.mkdir();
        }

        if (cacheDir.isDirectory() & cacheDir.canWrite()) {
            return new DiskLruCache(cacheDir, maxByteSize);
        }

        return null;
    }


    public void put(String key, Bitmap data) {
        synchronized (mLinkedHashMap) {
            if (mLinkedHashMap.get(key) == null) {
                try {
                    final String file = createFilePath(mCacheDir, key);
                    if (writeBitmapToFile(data, file)) {
                        put(key, file);
                        flushCache();
                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 刷新缓存，如果总大小超过指定的缓存大小，删除最旧的条目；
     * 注意：不跟踪在缓存文件中中却不再hashmap里的过期缓存文件；
     * 如果在磁盘缓存中更改图片和钥匙经常那么他们可能永远都不会被删除
     */
    private void flushCache() {
        Map.Entry<String, String> eldestEntry;
        File eldestFile;
        long eldestFileSize;
        int count = 0;

        while (count < MAX_REMOVALS && (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize)) {
            eldestEntry = mLinkedHashMap.entrySet().iterator().next();
            eldestFile = new File(eldestEntry.getValue());
            eldestFileSize = eldestFile.length();

            mLinkedHashMap.remove(eldestEntry.getKey());
            eldestFile.delete();
            cacheSize = mLinkedHashMap.size();
            cacheByteSize -= eldestFileSize;
            count++;
        }
    }

    /**
     * 把位图写进文件，
     *
     * @param bitmap
     * @param file
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    private boolean writeBitmapToFile(Bitmap bitmap, String file) throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file), IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void put(String key, String file) {
        mLinkedHashMap.put(key, file);
        cacheSize = mLinkedHashMap.size();
        cacheByteSize += new File(file).length();
    }

    public static String createFilePath(File cacheDir, String key) {
        try {
            return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX + URLEncoder.encode(key.replace("*", ""
            ), "UTF-8");
        } catch (final UnsupportedEncodingException e) {

        }
        return null;
    }

    /**
     * 从缓存中获取图像
     *
     * @param key
     * @return
     */
    public Bitmap get(String key) {
        synchronized (mLinkedHashMap) {
            try {
                final String file = mLinkedHashMap.get(key);
                if (file != null) {
                    return BitmapFactory.decodeFile(file);
                } else {
                    final String exitingFile = createFilePath(mCacheDir, key);
                    if (new File(exitingFile).exists()) {
                        put(key, exitingFile);
                        return BitmapFactory.decodeFile(exitingFile);
                    }
                }
            } catch (OutOfMemoryError e) {

            }
            return null;
        }
    }

    /**
     * 检查缓存对象中是否有这个键值对应的对象
     *
     * @param key
     * @return
     */
    public boolean containKey(String key) {
        if (mLinkedHashMap.containsKey(key)) {
            return true;
        }

        final String exitingFile = createFilePath(mCacheDir, key);
        if (new File(exitingFile).exists()) {
            put(key, exitingFile);
            return true;
        }
        return false;
    }

    /**
     * 删除当前实例目录下的所有缓存
     */
    public void clearCache() {
        DiskLruCache.clearCache(mCacheDir);
    }

    /**
     * 删除给定缓存目录下的所有文件
     *
     * @param cacheDir
     */
    private static void clearCache(File cacheDir) {
        final File[] files = cacheDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * 把应用的缓存路径里的缓存删掉
     *
     * @param context
     * @param uniqueName
     */
    public static void clearCache(Context context, String uniqueName) {
        File cacheDir = getDiskCacheDir(context, uniqueName);
        clearCache(cacheDir);
    }

    /**
     * 获取可以使用的缓存路径
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 用当前路径创建缓存文件
     *
     * @param Key
     * @return
     */
    public String createFilePath(String Key) {
        return createFilePath(mCacheDir, Key);
    }

    public void setmCompressParams(Bitmap.CompressFormat compressParams, int quality) {
        this.mCompressFormat = compressParams;
        this.mCompressQuality = quality;
    }

}
