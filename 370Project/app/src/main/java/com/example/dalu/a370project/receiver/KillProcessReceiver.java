package com.example.dalu.a370project.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.dalu.a370project.engine.ProcessInfoProvider;

/**
 * Created by DALU on 2016/6/27.
 */
public class KillProcessReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ProcessInfoProvider.killALl(context);
    }
}