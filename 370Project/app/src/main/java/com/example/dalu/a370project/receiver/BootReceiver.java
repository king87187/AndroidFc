package com.example.dalu.a370project.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by DALU on 2016/4/26.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       SharedPreferences spf =context.getSharedPreferences("setting",context.MODE_PRIVATE);
        String sim =spf.getString("sim",null);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String  ssn =tm.getSimSerialNumber();
        if(!TextUtils.isEmpty(sim)){
            if (sim.equals(ssn)){
                Log.e("sim","没问题");
            }else {
                Log.e("sim","有问题");
            }
        }
    }
}
