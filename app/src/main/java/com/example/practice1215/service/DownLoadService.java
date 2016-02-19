package com.example.practice1215.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by guanjun on 2015/12/31.
 */
public class DownLoadService extends IntentService {
    private static final String TAG = DownLoadService.class.getSimpleName();
    private Context context;

    public DownLoadService() {
        super("com.example.practice1215.service");
    }

    private NotificationManager notificationManager;
    private Notification notification;
    private RemoteViews remoteViews;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        //获取要下载文件的url
        String downloadUrl = bundle.getString("url");
        //设置文件下载后的保存路径,根目录下的/Download文件夹里
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");

        //判断路径是否存在，不存在则创建
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
//        File file = new File(fileDir, "file.apk");
        File file = new File(fileDir, fileName);
        //设置NOtification
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.mipmap.ic_launcher, "Version Update", System.currentTimeMillis());

        Intent intentNotifi = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNotifi, 0);
        notification.contentIntent = pendingIntent;
        //加载Notification的布局文件
        remoteViews = new RemoteViews(getPackageName(), R.layout.download_file);
        //设置下载进度条
        remoteViews.setProgressBar(R.id.downloadFile_pb, 100, 0, false);
        notification.contentView = remoteViews;
        notificationManager.notify(0, notification);

        // 开始下载
        downloadFile(downloadUrl, file);
        // 移除通知栏
        notificationManager.cancel(0);
        // 广播出去，由广播接收器来处理下载完成的文件
        Intent sendIntent = new Intent("com.test.downloadComplete");
        // 把下载好的文件的保存地址加进Intent
        sendIntent.putExtra("downloadFile", file.getPath());
        sendBroadcast(sendIntent);
    }

    private int fileLength, downloadLength = 0;

    private void downloadFile(String downloadUrl, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setReadTimeout(10000);
            huc.setConnectTimeout(3000);
            fileLength = Integer.valueOf(huc.getHeaderField("Content-Length"));
            is = huc.getInputStream();
            //拿到服务器返回的响应码
            int hand = huc.getResponseCode();
            if (hand == 200) {
                //开始检查下载进度
                handler.post(run);
                // 建立一个byte数组作为缓冲区，等下把读取到的数据储存在这个数组
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    downloadLength = downloadLength + len;
                }
            } else {
                //返回码
                Toast.makeText(this, "Error: " + hand, Toast.LENGTH_SHORT).show();
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        // 移除定時器
        handler.removeCallbacks(run);
        super.onDestroy();
    }

    // 定时器，每隔一段时间检查下载进度，然后更新Notification上的ProgressBar
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        public void run() {
            remoteViews.setProgressBar(R.id.downloadFile_pb, 100, downloadLength * 100 / fileLength, false);
            remoteViews.setTextViewText(R.id.tv_progress, downloadLength * 100 / fileLength + "%");
            notification.contentView = remoteViews;
            notificationManager.notify(0, notification);
            handler.postDelayed(run, 1000);
        }
    };
}
