package com.example.practice1215.utils;

import android.content.Context;

/**
 * Created by guanjun on 2016/1/28.
 */
public class UtilTools {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
