package com.example.practice1215.activity;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.tools.PopupWindowTools;

/**
 * Created by guanjun on 2016/1/5.
 */
public class PopTestActivity extends BaseActivity {
    Button btn_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_down = (Button) findViewById(R.id.btn_down);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_left:
                PopupWindowTools.getInstances(this).initPop(R.layout.pop_root_view).showAtViewLeft(view);
                break;
            case R.id.btn_right:
                PopupWindowTools.getInstances(this).initPop(R.layout.pop_root_view).showAtViewRight(view);
                break;
            case R.id.btn_up:
                PopupWindowTools.getInstances(this).initPop(R.layout.pop_root_view).showUponView(view);
                break;
            case R.id.btn_down:
                PopupWindowTools.getInstances(this).initPop(R.layout.pop_root_view).showBelowView(view);
                break;
        }
    }
}
