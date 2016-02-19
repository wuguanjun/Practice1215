package com.example.practice1215.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.practice1215.constants.Constants;
import com.example.practice1215.interfaces.ReceiveBroadcast;

/**
 * Created by guanjun on 2015/12/24.
 */
public class MainReceiver extends BroadcastReceiver {
    ReceiveBroadcast receiveBroadcast;

    public MainReceiver(ReceiveBroadcast receiveBroadcast) {
        if (receiveBroadcast != null) {
            this.receiveBroadcast = receiveBroadcast;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.RETURN_MAIN_FRAGMENT)) {
            receiveBroadcast.onReceiveBroadcast();
        }
    }
}
