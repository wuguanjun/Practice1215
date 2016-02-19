package com.example.practice1215.activity;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.tools.DialogTools;

/**
 * Created by guanjun on 2015/12/30.
 */
public class DialogTestActivity extends BaseActivity {
    DialogTools dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dt = new DialogTools(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.btn_dig_1:
                dt.commenDialog();
                break;
            case R.id.btn_dig_2:
                dt.checkDialog();
                break;
            case R.id.btn_dig_3:
                dt.dialogWithView();
                break;
            case R.id.btn_dig_4:
                dt.singleSelectDialog();
                break;
            case R.id.btn_dig_5:
                dt.multiSelectDialog();
                break;
            case R.id.btn_dig_6:
                dt.singleListDialog();
                break;
            case R.id.btn_dig_7:
                View dig_v = LayoutInflater.from(this).inflate(R.layout.dialog_content, null);

                dt.selfDefineLayoutDialog(dig_v);
                break;
        }
    }
}
