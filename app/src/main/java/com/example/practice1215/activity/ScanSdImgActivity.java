package com.example.practice1215.activity;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.adapter.SdImgAdapter;
import com.example.practice1215.tools.BaseTools;

import java.util.ArrayList;

/**
 * Created by guanjun on 2015/12/16.
 */
public class ScanSdImgActivity extends BaseActivity {
    ArrayList<String> imgUrls;
    GridView gv_sd_img;
    SdImgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleText("Phone Image");

        setTitleLeftText("Return");

        initValue();
        initview();
    }

    private void initValue() {
        imgUrls = new ArrayList<String>();
        BaseTools.getSdImgUrl(this, imgUrls);
        adapter = new SdImgAdapter(this, imgUrls);
        Toast.makeText(this, imgUrls.get(0) + "", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, imgUrls.size() + "", Toast.LENGTH_SHORT).show();
    }

    private void initview() {
        gv_sd_img = (GridView) findViewById(R.id.gv_sd_img);

        gv_sd_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initDialog();
                Drawable d = ((ImageView) (view
                        .findViewById(R.id.img_sd))).getDrawable();
                img_dia.setImageDrawable(d);
            }
        });

        gv_sd_img.setAdapter(adapter);
    }

    private AlertDialog dialog;
    ImageView img_dia;

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        img_dia = new ImageView(this);
        img_dia.setLayoutParams(new ViewGroup.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT));
        img_dia.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        builder.setView(img_dia);
        dialog = builder.create();
        dialog.show();
    }

}
