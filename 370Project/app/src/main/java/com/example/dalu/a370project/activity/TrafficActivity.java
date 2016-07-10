package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;

import com.example.dalu.a370project.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by DALU on 2016/7/10.
 */
@ContentView(R.layout.activity_traffic)
public class TrafficActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //获取手机下载流量
        //获取流量(R 手机(2G,3G,4G)下载流量)
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        //获取手机的总流量(上传+下载)
        //T total(手机(2G,3G,4G)总流量(上传+下载))
        long mobileTxBytes = TrafficStats.getMobileTxBytes();
        //total(下载流量总和(手机+wifi))
        long totalRxBytes = TrafficStats.getTotalRxBytes();
        //(总流量(手机+wifi),(上传+下载))
        long totalTxBytes = TrafficStats.getTotalTxBytes();
    }
}
