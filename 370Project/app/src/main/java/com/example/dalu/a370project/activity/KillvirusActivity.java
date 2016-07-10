package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.db.VirusDb;
import com.example.dalu.a370project.utils.Md5Util;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by DALU on 2016/7/7.
 */
@ContentView(R.layout.activity_anit_virus)
public class KillvirusActivity extends Activity{
    protected static final int SCANING = 100;

    protected static final int SCAN_FINISH = 101;

    private int index = 0;
    private List<ScanInfo> mVirusScanInfoList;
    @ViewInject(R.id.iv_scanning)
    private ImageView iv_scanning;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.pb_bar)
    private ProgressBar pb_bar;
    @ViewInject(R.id.ll_add_text)
    private LinearLayout ll_add_text;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANING:
                    //1,显示正在扫描应用的名称
                    ScanInfo info = (ScanInfo)msg.obj;
                    tv_name.setText(info.name);
                    //2,在线性布局中添加一个正在扫描应用的TextView
                    TextView textView = new TextView(getApplicationContext());
                    if(info.isVirus){
                        //是病毒
                        textView.setTextColor(Color.RED);
                        textView.setText("发现病毒:"+info.name);
                    }else{
                        //不是病毒
                        textView.setTextColor(Color.BLACK);
                        textView.setText("扫描安全:"+info.name);
                    }
                    ll_add_text.addView(textView, 0);
                    break;
                case SCAN_FINISH:
                    tv_name.setText("扫描完成");
                    //停止真正执行的旋转动画
                    iv_scanning.clearAnimation();
                    //告知用户卸载包含了病毒的应用
                    unInstallVirus();
                    break;
            }
        }


    };

    private void unInstallVirus() {
        for(ScanInfo scanInfo:mVirusScanInfoList){
            String packageName = scanInfo.packageName;
            //源码
            Intent intent = new Intent("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:"+packageName));
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        checkVirus();

        initAnimation();
    }
    class ScanInfo{
        public boolean isVirus;
        public String packageName;
        public String name;
    }
    private void checkVirus() {

        new Thread(){
            @Override
            public void run() {
                List<String> virusInfo = VirusDb.getVirusInfo();
                PackageManager pm =getPackageManager();
                List<PackageInfo> installedPackages = pm.getInstalledPackages(
                        PackageManager.GET_SIGNATURES + PackageManager.GET_UNINSTALLED_PACKAGES);
                //创建记录病毒的集合
                mVirusScanInfoList = new ArrayList<ScanInfo>();
                //记录所有应用的集合
                List<ScanInfo> scanInfoList = new ArrayList<ScanInfo>();

                //设置进度条的最大值
                pb_bar.setMax(installedPackages.size());

                //3.遍历应用集合

                for (PackageInfo packageInfo : installedPackages) {
                    ScanInfo scanInfo = new ScanInfo();
                    //获取签名文件的数组
                    Signature[] signatures = packageInfo.signatures;
                    //获取签名文件数组的第一位,然后进行md5,将此md5和数据库中的md5比对
                    Signature signature = signatures[0];
                    String string = signature.toCharsString();
                    //32位字符串,16进制字符(0-f)
                    String encoder = Md5Util.encoder(string);
                    //4,比对应用是否为病毒
                    if(virusInfo.contains(encoder)){
                        //5.记录病毒
                        scanInfo.isVirus = true;
                        mVirusScanInfoList.add(scanInfo);
                    }else{
                        scanInfo.isVirus = false;
                    }
                    //6,维护对象的包名,以及应用名称
                    scanInfo.packageName = packageInfo.packageName;
                    scanInfo.name = packageInfo.applicationInfo.loadLabel(pm).toString();
                    scanInfoList.add(scanInfo);
                    //7.在扫描的过程中,需要更新进度条
                    index++;
                    pb_bar.setProgress(index);

                    try {
                        Thread.sleep(50+new Random().nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //8.在子线程中发送消息,告知主线程更新UI(1:顶部扫描应用的名称2:扫描过程中往线性布局中添加view)
                    Message msg = Message.obtain();
                    msg.what = SCANING;
                    msg.obj = scanInfo;
                    mHandler.sendMessage(msg);
                }
                Message msg = Message.obtain();
                msg.what = SCAN_FINISH;
                mHandler.sendMessage(msg);

            }
        }.start();

    }
    private void initAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        //指定动画一直旋转
        rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
        //保持动画执行结束后的状态
        rotateAnimation.setFillAfter(true);
        //一直执行动画

            iv_scanning.startAnimation(rotateAnimation);

    }
}
