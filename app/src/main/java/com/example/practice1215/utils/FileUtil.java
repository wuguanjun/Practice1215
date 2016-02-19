package com.example.practice1215.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by guanjun on 2016/2/18.
 */
public class FileUtil {
    /**
     * 获取SD卡根目录
     *
     * @return
     */
    public static File getRootPath() {
        File path = null;
        if (FileUtil.isSdCardAvailable()) {
            path = Environment.getExternalStorageDirectory();//获取sd卡文件路径
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    /**
     * sd卡是否可用
     *
     * @return
     */
    public static boolean isSdCardAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else {
            return false;
        }
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除指定文件夹下所有文件，保留文件夹
     *
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        }

        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File exeFile = files[i];
            if (exeFile.isDirectory()) {
                delAllFile(exeFile.getAbsolutePath());
            } else {
                exeFile.delete();
            }
        }
        return flag;
    }

    /**
     * 拷贝
     *
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean copy(String srcFile, String destFile) {
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 复制整个文件夹
     *
     * @param oldPath
     * @param newPath
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream in = new FileInputStream(temp);
                    FileOutputStream out = new FileOutputStream(newPath + File.separator + temp.getName().toString());
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = in.read()) != -1) {
                        out.write(bytes, 0, len);
                    }
                    out.flush();
                    out.close();
                    in.close();
                }

                if (temp.isDirectory()) {
                    copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
                }
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
    }

    /**
     * 重命名文件
     *
     * @param resFilePath
     * @param newFilePath
     * @return
     */
    public static boolean renameFile(String resFilePath, String newFilePath) {
        File resFile = new File(resFilePath);
        File newFile = new File(newFilePath);
        return resFile.renameTo(newFile);
    }

    /**
     * 获取磁盘可用空间
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static long getSdCardAvailableSize() {
        File path = getRootPath();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /**
     * 获取某个目录的可用大小
     *
     * @param path
     * @return
     */
    public static long getDirSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /**
     * 获取文件或文件夹大小
     *
     * @param path
     * @return
     */
    public static long getFileAllSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                long size = 0;
                for (File f : files) {
                    size += getFileAllSize(f.getPath());
                }
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    /**
     * 创建一个文件
     *
     * @param path
     * @return
     */
    public static boolean initFile(String path) {
        boolean result = false;
        try {
            File file = new File(path);
            if (!file.exists()) {
                result = file.createNewFile();
            } else if (file.isDirectory()) {
                file.delete();
                file.createNewFile();
            } else if (file.exists()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建一个文件夹
     */
    public static boolean initDirctory(String path) {
        boolean result = false;
        File file = new File(path);
        if (!file.exists()) {
            result = file.mkdir();
        } else if (!file.isDirectory()) {
            file.delete();
            result = file.mkdir();
        } else if (file.exists()) {
            result = true;
        }
        return result;
    }

    /**
     * 复制文件
     */
    public static long copyFile(InputStream from, File to) throws IOException {
        long totalBytes = 0;
        FileOutputStream fos = new FileOutputStream(to, false);
        try {
            byte[] data = new byte[1024];
            int len;
            while ((len = from.read(data)) > -1) {
                fos.write(data, 0, len);
                totalBytes += len;
            }
            fos.flush();
        } finally {
            fos.close();
        }
        return totalBytes;
    }

    /**
     * 复制文件
     */
    public static void copyFile(File from, File to) throws IOException {
        if (!from.exists()) {
            throw new IOException("The source file not exist: " + from.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(from);
        try {
            copyFile(fis, to);
        } finally {
            fis.close();
        }
    }

    /**
     * 保存流到文件
     *
     * @param inputStream
     * @param filePath
     */
    public static void saveFile(InputStream inputStream, String filePath) {
        try {
            OutputStream outputStream = new FileOutputStream(new File(filePath), false);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用UTF-8保存一个文件
     *
     * @param path
     * @param content
     * @param append
     * @throws IOException
     */
    public static void saveFileUTF8(String path, String content, Boolean append) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, append);
        Writer out = new OutputStreamWriter(fos, "UTF-8");
        out.write(content);
        out.flush();
        out.close();
        fos.flush();
        fos.close();
    }

    /**
     * 用UTF-8读取一个文件
     *
     * @param path
     */
    public static String readFileUTF8(String path) {
        String result = "";
        InputStream in = null;
        try {
            in = new FileInputStream(path);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            in.close();
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 得到一个文件的Intent
     *
     * @param path
     * @param mimetype
     * @return
     */
    public static Intent getFileIntent(String path, String mimetype) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile((new File(path))), mimetype);
        return intent;
    }
}
