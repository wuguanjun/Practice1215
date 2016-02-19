package com.example.practice1215.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.broadcastreceiver.MainReceiver;
import com.example.practice1215.constants.Constants;

/**
 * Created by guanjun on 2015/12/16.
 */
public class Fragment02 extends Fragment implements View.OnClickListener {
    private Button btn_test;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment02, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btn_test = (Button) view.findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                sendBraodcast();
                break;
        }
    }


    private void sendBraodcast() {
        Intent intent = new Intent(Constants.RETURN_MAIN_FRAGMENT);
        getActivity().sendBroadcast(intent);
    }
}
