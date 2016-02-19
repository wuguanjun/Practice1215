package com.example.practice1215.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.view.AutoDisplayLayout;

/**
 * Created by guanjun on 2016/1/28.
 */
public class AutoDisplayActivity extends BaseActivity {
    private EditText et_label;
    private Button btn_add;
    AutoDisplayLayout adl_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("自适应布局");
        initView();
    }

    private void initView() {
        et_label = (EditText) findViewById(R.id.et_label);
        adl_container = (AutoDisplayLayout) findViewById(R.id.adl_container);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_add:
                if ((et_label.getText()).toString().trim().equals("") || null == et_label.getText()) {
                    Toast.makeText(AutoDisplayActivity.this, "请输入标签内容...", Toast.LENGTH_SHORT).show();
                } else {
                    addItem((et_label.getText()).toString().trim());
                }
                break;
        }
    }

    private void addItem(String str) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv_Item = new TextView(this);
        tv_Item.setLayoutParams(params);
        tv_Item.setText(str);

        adl_container.addView(tv_Item);
    }
}
