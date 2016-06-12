package com.example.dalu.a370project.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.dalu.a370project.engine.ProcessInfoProvider;

public class LockScreenService extends Service {
    IntentFilter intentFilter;
    InnerReceiver innerReceiver;

    public LockScreenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        innerReceiver = new InnerReceiver();
        registerReceiver(innerReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=innerReceiver){
            unregisterReceiver(innerReceiver);
        }
    }
    class InnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ProcessInfoProvider.killALl(context);
        }
    }
}
