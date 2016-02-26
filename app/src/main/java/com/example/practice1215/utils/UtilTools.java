package com.example.practice1215.utils;

import android.content.Context;

/**
 * Created by guanjun on 2016/1/28.
 */
public class UtilTools {
    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int px2dip(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue / scale + 0.5f);
    }
}
