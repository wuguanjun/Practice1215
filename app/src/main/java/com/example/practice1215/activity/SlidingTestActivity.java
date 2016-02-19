package com.example.practice1215.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.practice1215.R;
import com.example.practice1215.adapter.sliding.DividerItemDecoration;
import com.example.practice1215.adapter.sliding.ItemRecycleAdapter;
import com.example.practice1215.adapter.sliding.LeftItemAdapter;
import com.example.practice1215.view.DragLayout;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class SlidingTestActivity extends AppCompatActivity {

    private DragLayout dl;
    private ListView lv;
    private RecyclerView recycleview;
    private List<String> mLists = new ArrayList<>();

    private ImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_test);
    }
}
