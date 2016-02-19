package com.example.practice1215.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanjun on 2016/1/27.
 */
public class ViewTestActivity extends BaseActivity {
    Spinner sp_color, sp_degree;
    ArrayAdapter<CharSequence> adapterColor;
    ArrayAdapter<CharSequence> adapterDegree;

    List<CharSequence> dataDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp_color = (Spinner) findViewById(R.id.sp_prefer_color);
        sp_color.setPrompt("请选择您喜欢的颜色：");
        adapterColor = ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_color.setAdapter(adapterColor);

        sp_degree = (Spinner) findViewById(R.id.sp_prefer_degree);
        dataDegree = new ArrayList<CharSequence>();
        dataDegree.add("university");
        dataDegree.add("senior");
        dataDegree.add("middle");
        dataDegree.add("little");
        sp_degree.setPrompt("请选择您喜欢的学历：");
        adapterDegree = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dataDegree);
        adapterDegree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_degree.setAdapter(adapterDegree);

//        ArrayList<String> strs = new ArrayList<String>();
//        strs = (ArrayList<String>) getIntent().getSerializableExtra("label");
//        for (int i = 0; i < strs.size(); i++) {
//            Log.d("ViewTestActivity", strs.get(i));
//        }
//
//        Toast.makeText(this, strs.get(6), Toast.LENGTH_SHORT).show();
    }
}
