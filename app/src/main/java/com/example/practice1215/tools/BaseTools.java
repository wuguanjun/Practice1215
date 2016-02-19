package com.example.practice1215.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.example.practice1215.utils.StaticFields;

import java.util.ArrayList;

/**
 * Created by guanjun on 2015/12/15.
 */
public class BaseTools {
    Context context;

    /**
     * @param context
     */
    public static void init(Context context) {
        try {
            StaticFields.packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(StaticFields.packageName, 0);
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            StaticFields.window_height = dm.heightPixels;
            StaticFields.window_width = dm.widthPixels;
            StaticFields.density = dm.density;
            StaticFields.densityDip = dm.densityDpi;

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            StaticFields.deviceId = tm.getDeviceId();
            StaticFields.deviceName = android.os.Build.MODEL;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取R文件中的对应的id
     *
     * @param valueType
     * @param fieldValue
     * @return
     */
    public static int getFieldValue(String valueType, String fieldValue) {
        int id = -1;
        try {
            Class<?> clazz = Class.forName(StaticFields.packageName + ".R$" + valueType);
            id = clazz.getField(fieldValue).getInt(null);
        } catch (Exception e) {

        }
        return id;
    }

    /**
     * 获取id
     *
     * @param fieldValue
     * @return
     */
    public static int getId(String fieldValue) {
        return getFieldValue("id", fieldValue);
    }

    public static int getLayout(String fieldName) {
        return getFieldValue("layout", fieldName);
    }

    public static int getDrawable(String fieldName) {
        return getFieldValue("drawable", fieldName);
    }

    /**
     * 把首字母转化为小写
     *
     * @param str
     * @return
     */
    public static String toFirstLowerCase(String str) {
        if (str != null && str.length() > 0) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        } else {
            return str;
        }
    }

    /**
     * 获取手机上的图片路径
     *
     * @param context
     * @param imgUrls
     */
    public static void getSdImgUrl(Context context, ArrayList<String> imgUrls) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                imgUrls.add(path);
            }
            cursor.close();
        }
    }
}
