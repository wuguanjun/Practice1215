package com.example.practice1215.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice1215.R;

/**
 * Created by guanjun on 2015/12/16.
 */
public class Fragment03 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment03, container, false);
        return view;
    }
}
