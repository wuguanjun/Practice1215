package com.example.practice1215.activity.baseActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.tools.BaseTools;
import com.example.practice1215.utils.StaticFields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guanjun on 2015/12/15.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {
    /**
     * 按名称加载布局
     */
    private ViewGroup vg_base_root;
    protected String resourceName;
    private int recourseId = -1;

    /**
     * 基本布局 标题
     */
    private LinearLayout llt_title;
    private TextView tv_title_left, tv_title, tv_title_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        BaseTools.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
        resourceName = getMatcherRecourseName();

        setContentView(R.layout.base);

        initView();

        vg_base_root = (ViewGroup) findViewById(R.id.llt_container);

        addRootView(vg_base_root);
    }

    /**
     * 初始化标题栏控件
     */
    private void initView() {
        llt_title = (LinearLayout) findViewById(R.id.llt_title);
        tv_title_left = (TextView) findViewById(R.id.tv_title_left);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title_right.setOnClickListener(this);
        tv_title_left.setOnClickListener(this);
    }

    /**
     * 隐藏标题
     */
    protected void hideTitle() {
        llt_title.setVisibility(View.GONE);
    }

    protected void setTitleText(String str) {
        tv_title.setText(str);
    }

    protected void setTitleLeftText(String str) {
        tv_title_left.setText(str);
    }

    protected void setTitleRightText(String str) {
        tv_title_right.setText(str);
    }

    protected void setTitleback(int color) {
    }


    protected void addRootView(ViewGroup rootView) {
        int resid = -1;
        try {
            if (recourseId == -1) {
                resid = getFieldValue("layout", resourceName);
            } else {
                resid = recourseId;
            }

            if (rootView == null) {
                setContentView(resid);
            } else {
                getLayoutInflater().inflate(resid, rootView);
            }
            setOnClickListener(getWindow().getDecorView());
        } catch (Exception e) {
            Log.d(StaticFields.TAG, "未找到匹配文件");
            Toast.makeText(this, "未找到匹配文件", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setOnClickListener(View view) {
        try {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    setOnClickListener(vg.getChildAt(i));
                }
            } else {
                view.setOnClickListener(this);
            }
        } catch (Exception e) {

        }
    }

    protected int getFieldValue(String typeName, String fieldName) {
        int i = BaseTools.getFieldValue(typeName, fieldName);
        return i;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_left:
                finish();
                break;
            case R.id.tv_title_right:
                break;
        }
    }

    private String getMatcherRecourseName() {
        String resName;
        String activityName = this.getClass().getSimpleName();
        if (activityName.contains("Activity")) {
            resName = activityName.substring(0, activityName.indexOf("Activity"));
        } else {
            resName = activityName;
        }
        resName = BaseTools.toFirstLowerCase(resName);
        Pattern pattern = Pattern.compile("\\p{Upper}");
        Matcher m = pattern.matcher(resName);
        while (m.find()) {
            String s = m.group();
            resName = resName.replace(s, "_" + s.toLowerCase());
        }
        return resName;
    }
}
