package com.example.practice1215.activity;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.adapter.MainFragmentAdapter;
import com.example.practice1215.broadcastreceiver.MainReceiver;
import com.example.practice1215.constants.Constants;
import com.example.practice1215.fragments.Fragment02;
import com.example.practice1215.fragments.Fragment03;
import com.example.practice1215.fragments.MainFragment;
import com.example.practice1215.fragments.SelfFragment;
import com.example.practice1215.interfaces.ReceiveBroadcast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private LinearLayout llt_activity, llt_2, llt_3, llt_self, llt_bottom_menu;
    ViewPager vp_container;
    TextView tv_phone_type;
    List<Fragment> fragments;
    MainFragmentAdapter mfAdapter;

    MainReceiver mainReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

        setTitleText("主界面");
        setTitleLeftText("退出");

        setTitleRightText("");
        initFragment();
        initView();

        if(ContextCompat.checkSelfPermission(this,"")!= PackageManager.PERMISSION_GRANTED){

        }
    }

    private void initView() {
        llt_activity = (LinearLayout) findViewById(R.id.llt_activity);
        llt_2 = (LinearLayout) findViewById(R.id.llt_2);
        llt_3 = (LinearLayout) findViewById(R.id.llt_3);
        llt_self = (LinearLayout) findViewById(R.id.llt_self);
        llt_bottom_menu = (LinearLayout) findViewById(R.id.llt_bottom_menu);

        tv_phone_type = (TextView) findViewById(R.id.tv_phone_type);
        vp_container = (ViewPager) findViewById(R.id.vp_container);

        llt_activity.setSelected(true);
        llt_activity.setOnClickListener(this);
        llt_2.setOnClickListener(this);
        llt_3.setOnClickListener(this);
        llt_self.setOnClickListener(this);

        tv_phone_type.setText(android.os.Build.MODEL);

        vp_container.setAdapter(mfAdapter);

        vp_container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initItemState();
                (llt_bottom_menu.getChildAt(position)).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new MainFragment());
        fragments.add(new Fragment02());
        fragments.add(new Fragment03());
        fragments.add(new SelfFragment());
        FragmentManager fm = getSupportFragmentManager();

        mfAdapter = new MainFragmentAdapter(this, fm, fragments);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        initItemState();
        switch (view.getId()) {
            case R.id.tv_activity:
                llt_activity.setSelected(true);
                vp_container.setCurrentItem(0);
                break;
            case R.id.tv_2:
                llt_2.setSelected(true);
                vp_container.setCurrentItem(1);
                break;
            case R.id.tv_3:
                llt_3.setSelected(true);
                vp_container.setCurrentItem(2);
                break;
            case R.id.tv_self:
                llt_self.setSelected(true);
                vp_container.setCurrentItem(fragments.size() - 1);
                break;
//            default:
//                Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    private void initItemState() {
        llt_activity.setSelected(false);
        llt_2.setSelected(false);
        llt_3.setSelected(false);
        llt_self.setSelected(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mainReceiver = new MainReceiver(new ReceiveBroadcast() {
            @Override
            public void onReceiveBroadcast() {
                vp_container.setCurrentItem(0);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.RETURN_MAIN_FRAGMENT);

        registerReceiver(mainReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainReceiver != null) {
            unregisterReceiver(mainReceiver);
        }
    }
}
