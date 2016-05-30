package com.example.dalu.a370project.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.service.LocationService;

/**
 * Created by DALU on 2016/5/5.
 */
public class SmsReceiver extends BroadcastReceiver {
    private MediaPlayer mPlayer = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] objects = (Object[]) bundle.get("pdus");
        for(Object obj : objects){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
            String body = smsMessage.getDisplayMessageBody();
            String address = smsMessage.getDisplayOriginatingAddress();
          //  long date = smsMessage.getTimestampMillis();

            if(body.equals("#*alarm*#")){
                abortBroadcast();
               /* SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("5556",null,address +" 于  " + dateStr + "给你发了以下内容: " + body,null,null);*/

                mPlayer = MediaPlayer.create(context,R.raw.ylzs);
                mPlayer.setAuxEffectSendLevel(0.1f);
                mPlayer.setLooping(true);
                mPlayer.start();
            }else if (body.equals("#*location*#")){
                context.startService(new Intent(context, LocationService.class));// 开启定位服务

                SharedPreferences sp = context.getSharedPreferences("config",
                        Context.MODE_PRIVATE);
                String location = sp.getString("location",
                        "getting location...");

                System.out.println("location:" + location);
                abortBroadcast();
              //  LocationManger locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);


            }else if(body.equals("#*wipedata*#")){

            } else if (body.equals("#*lockscreen*#")) {

            }
        }
}
}
