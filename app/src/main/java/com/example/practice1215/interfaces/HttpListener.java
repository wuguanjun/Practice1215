package com.example.practice1215.interfaces;

import com.yolanda.nohttp.Response;

/**
 * Created by guanjun on 2016/2/18.
 */
public abstract interface HttpListener<T> {
    public abstract void onSucceed(int what, Response<T> response);

    public abstract void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis);
}
