package com.example.practice1215.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.view.RingView;


/**
 * Created by guanjun on 2016/5/10.
 */
public class SelfDefineActivity extends BaseActivity {
    RingView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self);

        initView();
    }

    private  void initView(){
        rv = (RingView)findViewById(R.id.rv_test);
        rv.setTotalMoney("123456.11");
        rv.setTextColor(Color.BLACK);
        rv.setValues(new float[]{10,30,50});
    }
}
