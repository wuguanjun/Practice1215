package com.example.practice1215.responseListener;


import android.content.Context;
import android.content.DialogInterface;

import com.example.practice1215.interfaces.HttpListener;
import com.example.practice1215.tools.WaitDialog;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * Created by guanjun on 2016/2/18.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {
    /**
     * WaitDialog
     */
    WaitDialog mDialog;
    private Request<?> mRequest;

    /**
     * 结果回调
     */
    private HttpListener<T> callback;

    /**
     * 是否显示dialog
     */
    private boolean isLoading;

    /**
     * @param context   用来实例化dialog
     * @param request   请求对象
     * @param callback  回调对象
     * @param canCancel 是否允许用户取消请求
     * @param isLoading 是否显示dialog
     */
    public HttpResponseListener(Context context, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        this.mRequest = request;
        if (null != context && isLoading) {
            mDialog = new WaitDialog(context);
            mDialog.setCancelable(canCancel);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = callback;
        this.isLoading = isLoading;
    }

    /**
     * 开始请求，显示一个dialog
     *
     * @param what
     */
    @Override
    public void onStart(int what) {
        if (isLoading && mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    /**
     * 成功的回调
     *
     * @param what
     * @param response
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callback != null) {
            callback.onSucceed(what, response);
        }
    }

    /**
     * 失败的回调
     *
     * @param what
     * @param url
     * @param tag
     * @param message
     * @param responseCode
     * @param networkMillis
     */
    @Override
    public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {
        if (callback != null) {
            callback.onFailed(what, url, tag, message, responseCode, networkMillis);
        }
    }

    /**
     * 结束请求，关闭dialog
     *
     * @param what
     */
    @Override
    public void onFinish(int what) {
        // TODO 结束请求
        if (isLoading && mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
