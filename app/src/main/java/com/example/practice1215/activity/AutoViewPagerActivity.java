package com.example.practice1215.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.practice1215.R;
import com.example.practice1215.adapter.BannerAdapter;
import com.example.practice1215.view.AutoPlayViewPager;

import java.util.ArrayList;
import java.util.List;

public class AutoViewPagerActivity extends Activity implements View.OnClickListener {

    private AutoPlayViewPager autoPlayViewPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_viewpager);

        findViewById(R.id.btn_scroll_change_left).setOnClickListener(this);
        findViewById(R.id.btn_scroll_change_right).setOnClickListener(this);
        findViewById(R.id.btn_scroll_previous).setOnClickListener(this);
        findViewById(R.id.btn_scroll_next).setOnClickListener(this);
        autoPlayViewPage = (AutoPlayViewPager)findViewById(R.id.view_pager);

        // 这里可以换成http://www.xx.com/xx.png这种连接的集合
        List<Integer> resIds = new ArrayList<>();
        // 模拟几张图片
        resIds.add(R.mipmap.ic_launcher);
        resIds.add(R.mipmap.nohttp);
        resIds.add(R.mipmap.nohttp_delete);
        resIds.add(R.mipmap.nohttp_des);
        resIds.add(R.mipmap.nohttp_get);
        resIds.add(R.mipmap.nohttp_head);
        resIds.add(R.mipmap.nohttp_options);
        resIds.add(R.mipmap.nohttp_patch);
        resIds.add(R.mipmap.nohttp_post);
        resIds.add(R.mipmap.nohttp_put);
        resIds.add(R.mipmap.nohttp_trace);


        BannerAdapter bannerAdapter = new BannerAdapter(this);
        bannerAdapter.update(resIds);

        autoPlayViewPage.setAdapter(bannerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scroll_previous:// 播放上一个
                autoPlayViewPage.previous();
                break;
            case R.id.btn_scroll_next:// 播放下一个
                autoPlayViewPage.next();
                break;
            case R.id.btn_scroll_change_left:// 改变为向左滑动
                autoPlayViewPage.setDirection(AutoPlayViewPager.Direction.LEFT);
                break;
            case R.id.btn_scroll_change_right:// 改变为向右滑动
                autoPlayViewPage.setDirection(AutoPlayViewPager.Direction.RIGHT);
                break;
        }
    }
}
