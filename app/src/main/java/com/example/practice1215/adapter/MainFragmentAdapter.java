package com.example.practice1215.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by guanjun on 2015/12/15.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    Context context;
    List<Fragment> fragments;

    public MainFragmentAdapter(Context context, FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
