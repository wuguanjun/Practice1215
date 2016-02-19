package com.example.practice1215.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.practice1215.AppConfig;
import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.constants.Constants;
import com.example.practice1215.interfaces.HttpListener;
import com.example.practice1215.tools.WaitDialog;
import com.example.practice1215.utils.CallServer;
import com.example.practice1215.utils.FastJsonRequest;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.download.StatusCode;

import org.json.JSONArray;


/**
 * Created by guanjun on 2016/2/18.
 */
public class NoHttpTestActivity extends BaseActivity {
    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int NOHTTP_ORIGIN_TEST = 0x001;
    private static final int NOHTTP_FASTJSON_TEST = 0x002;
    private static final int NOHTTP_METHOD_TEST = 0x003;
    private static final int NOHTTP_JSON_OBJECT_TEST = 0x004;
    private static final int NOHTTP_JSON_ARRAY_TEST = 0x005;


    /**
     * 请求的时候等待框
     */
    private WaitDialog mWaitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("NohttpSampleTest");

        mWaitDialog = new WaitDialog(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_no_origin:
                origin();
                break;
            case R.id.btn_no_self_define_request_object:
                fastJsonRequest();
                break;
            case R.id.btn_no_request_method_get:
                method(METHOD_GET);
                break;
            case R.id.btn_no_request_method_post:
                method(METHOD_POST);
                break;
            case R.id.btn_no_request_method_put:
                method(METHOD_PUT);
                break;
            case R.id.btn_no_request_method_head:
                method(METHOD_HEAD);
                break;
            case R.id.btn_no_request_method_delete:
                method(METHOD_DELETED);
                break;
            case R.id.btn_no_request_method_options:
                method(METHOD_OPTIONS);
                break;
            case R.id.btn_no_request_method_trace:
                method(METHOD_TRACE);
                break;
            case R.id.btn_no_request_method_patch:
                method(METHOD_PATCH);
                break;
            case R.id.btn_no_json_object:
                jsonObject();
                break;
            case R.id.btn_no_json_array:
                jsonArray();
                break;
            case R.id.btn_no_file_download:
                download();
                break;
        }
    }

    private DownloadRequest downloadRequest;
    private final static String PROGRESS_KEY = "download_progress";
    /***
     * 下载地址
     */
    private String url = "http://m.apk.67mo.com/apk/999129_21769077_1443483983292.apk";
    private ProgressBar pb_dl_pro;
    AlertDialog mDialog = null;

    /**
     * Download
     */
    private void download() {
        downloadRequest = NoHttp.createDownloadRequest(url, AppConfig.getInstance().APP_PATH_ROOT, "nohttp.apk", true, false);

        View v = LayoutInflater.from(this).inflate(R.layout.download_layout, null);
        pb_dl_pro = (ProgressBar) v.findViewById(R.id.pb_download_progress);
        v.findViewById(R.id.btn_start_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallServer.getDownloadInstance().add(0, downloadRequest, new DownloadListener() {
                    @Override
                    public void onDownloadError(int what, StatusCode statusCode, CharSequence errorMessage) {
                        initDialog("Download Error!");
                    }

                    @Override
                    public void onStart(int what, boolean isResume, long beforeLength, Headers responseHeaders, long allCount) {
                        int progress = (int) (beforeLength * 100 / allCount);
                        pb_dl_pro.setProgress(progress);
                    }

                    @Override
                    public void onProgress(int what, int progress, long fileCount) {
                        pb_dl_pro.setProgress(progress);
                        AppConfig.getInstance().putInt(PROGRESS_KEY, progress);
                    }

                    @Override
                    public void onFinish(int what, String filePath) {
                        mDialog.dismiss();
                        initDialog("Download Completed!");
                    }

                    @Override
                    public void onCancel(int what) {
                        mDialog.dismiss();
                        initDialog("Cancel Download!");
                    }
                });
            }
        });
        v.findViewById(R.id.btn_cancel_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadRequest.isStarted()) {
                    // 取消下载
                    downloadRequest.cancel();
                }
            }
        });


        // 检查之前的下载状态
        int beforeStatus = downloadRequest.checkBeforeStatus();
        switch (beforeStatus) {
            case DownloadRequest.STATUS_RESTART:
                pb_dl_pro.setProgress(0);
                break;
            case DownloadRequest.STATUS_RESUME:
                int progress = AppConfig.getInstance().getInt(PROGRESS_KEY, 0);
                pb_dl_pro.setProgress(progress);
                break;
            case DownloadRequest.STATUS_FINISH:
                pb_dl_pro.setProgress(100);
                break;
            default:
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        mDialog = builder.create();
        mDialog.setTitle("下载");
        mDialog.show();
    }


    /**
     * jsonArray
     */
    private void jsonArray() {
        Request<JSONArray> request = NoHttp.createJsonArrayRequest(Constants.URL_NOHTTP_JSONARRAY);
        CallServer.getRequestInstance().add(this, NOHTTP_JSON_ARRAY_TEST, request, new HttpListener<JSONArray>() {
            @Override
            public void onSucceed(int what, Response<JSONArray> response) {
                JSONArray jsonArray = response.get();
                StringBuilder sb = new StringBuilder(jsonArray.toString() + "\n");
                sb.append("\n解析数据：\n\n");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String str = jsonArray.optString(i);
                    sb.append(str + "\n");
                }
                initDialog(sb.toString());
            }

            @Override
            public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {
                initDialog("JSONArray Request" + "Failed " + "\n" + message);
            }
        }, true, true);
    }

    /**
     * JsonObject
     */
    private void jsonObject() {
        Request<org.json.JSONObject> request = NoHttp.createJsonObjectRequest(Constants.URL_NOHTTP_JSONOBJECT);
        CallServer.getRequestInstance().add(this, NOHTTP_JSON_OBJECT_TEST, request, new HttpListener<org.json.JSONObject>() {
            @Override
            public void onSucceed(int what, Response<org.json.JSONObject> response) {
                org.json.JSONObject jsonObject = response.get();
                if (0 == jsonObject.optInt("error", -1)) {
                    StringBuilder builder = new StringBuilder(jsonObject.toString());
                    builder.append("\n\n解析数据: \n\n请求方法: ").append(jsonObject.optString("method")).append("\n");
                    builder.append("请求地址: ").append(jsonObject.optString("url")).append("\n");
                    builder.append("响应数据: ").append(jsonObject.optString("data")).append("\n");
                    builder.append("错误码: ").append(jsonObject.optInt("error"));

                    initDialog(builder.toString());
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {
                initDialog("JsonObject Request" + "Failed " + "\n" + message);
            }
        }, true, true);
    }

    private static final int METHOD_GET = 0;
    private static final int METHOD_POST = 1;
    private static final int METHOD_PUT = 2;
    private static final int METHOD_HEAD = 3;
    private static final int METHOD_DELETED = 4;
    private static final int METHOD_OPTIONS = 5;
    private static final int METHOD_TRACE = 6;
    private static final int METHOD_PATCH = 7;

    /**
     * 请求对象
     */
    private Request<String> mRequest;

    /**
     * Method
     *
     * @param method
     */
    private void method(int method) {
        RequestMethod method1 = RequestMethod.GET;
        switch (method) {
            case METHOD_GET:
                break;
            case METHOD_POST:
                method1 = RequestMethod.POST;
                break;
            case METHOD_PUT:
                method1 = RequestMethod.PUT;
                break;
            case METHOD_HEAD:
                method1 = RequestMethod.HEAD;
                break;
            case METHOD_DELETED:
                method1 = RequestMethod.DELETE;
                break;
            case METHOD_OPTIONS:
                method1 = RequestMethod.OPTIONS;
                break;
            case METHOD_TRACE:
                method1 = RequestMethod.TRACE;
                break;
            case METHOD_PATCH:
                method1 = RequestMethod.PATCH;
                break;
            default:
                break;
        }

        mRequest = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, method1);

        mRequest.add("userName", "yolanda");// String类型
        mRequest.add("userPass", "yolanda.pass");
        mRequest.add("userAge", 20);// int类型
        mRequest.add("userSex", '1');// char类型，还支持其它类型

        //添加到请求队列
        CallServer.getRequestInstance().add(this, NOHTTP_METHOD_TEST, mRequest, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                initDialog("Success " + "\n" + response.get());
            }

            @Override
            public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {
                initDialog("Method Request" + "Failed " + "\n" + message);
            }
        }, true, true);
    }

    /**
     * Origin
     */
    private void origin() {
        RequestQueue requestQueue = null;
        requestQueue = NoHttp.newRequestQueue();
        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_TEST, RequestMethod.POST);

        // 添加请求参数
        request.add("userName", "yolanda");
        request.add("userPass", 1);
        request.add("userAge", 1.25);

        // 上传文件
        // request.add("userHead", new FileBinary(new File(AppConfig.APP_UPLOAD_FILE_PATH)));

        // 添加请求头
        request.addHeader("Author", "nohttp_sample");

        // 设置一个tag, 在请求完(失败/成功)时原封不动返回; 多数情况下不需要
        request.setTag(new Object());

        /**
         * what: 当多个请求同时使用同一个OnResponseListener时用来区分请求, 类似handler的what一样
         * request: 请求对象
         * onResponseListener 回调对象，接受请求结果
         */
        requestQueue.add(NOHTTP_ORIGIN_TEST, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                // 请求开始，这里可以显示一个dialog
                mWaitDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                // 请求成功
                StringBuilder sb = new StringBuilder();
                sb.append(response.get() + "\n");// 响应结果

                Object tag = response.getTag();// 拿到请求时设置的tag
                byte[] responseBody = response.getByteArray();// 如果需要byteArray
                // 响应头
                Headers headers = response.getHeaders();
                sb.append("响应码: ");
                sb.append(headers.getResponseCode());// 响应码
                sb.append("\n请求花费时间: ");
                sb.append(response.getNetworkMillis()).append("毫秒"); // 请求花费的时间

                initDialog(sb.toString());
            }

            @Override
            public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {
                initDialog("Origin Request" + "Failed " + "\n" + message);
            }

            @Override
            public void onFinish(int what) {
                // 请求结束，这里关闭dialog
                mWaitDialog.dismiss();
            }
        });
    }

    /**
     * fastJson
     */
    private void fastJsonRequest() {
        FastJsonRequest request = new FastJsonRequest(Constants.URL_NOHTTP_JSONOBJECT);
        CallServer.getRequestInstance().add(this, NOHTTP_FASTJSON_TEST, request, new HttpListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                JSONObject jsonObject = response.get();
                if (NOHTTP_FASTJSON_TEST == what)
                    if (0 == jsonObject.getIntValue("error")) {
                        if (0 == jsonObject.getIntValue("error")) {
                            StringBuilder builder = new StringBuilder(jsonObject.toString());
                            builder.append("\n\n解析数据: \n\n请求方法: ").append(jsonObject.getString("method")).append("\n");
                            builder.append("请求地址: ").append(jsonObject.getString("url")).append("\n");
                            builder.append("响应数据: ").append(jsonObject.getString("data")).append("\n");
                            builder.append("错误码: ").append(jsonObject.getIntValue("error"));
                            initDialog(builder.toString());
                        }
                    }
            }

            @Override
            public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode,
                                 long networkMillis) {
                initDialog("FastJson Request" + "请求失败\n" + message);
            }

        }, false, true);
    }


    /**
     * AlertDialog
     */
    AlertDialog dialog = null;

    private void initDialog(String str) {
        TextView tv_content = new TextView(this);
        tv_content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_content.setPadding(30, 30, 30, 30);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        tv_content.setText(str);
        builder.setView(tv_content);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequest != null) {
            mRequest.cancel();
        }

        if (downloadRequest != null)
            downloadRequest.cancel();
    }
}
