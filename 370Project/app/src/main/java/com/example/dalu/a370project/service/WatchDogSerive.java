package com.example.dalu.a370project.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dalu.a370project.WangApplication;
import com.example.dalu.a370project.activity.AppLockActivity;
import com.example.dalu.a370project.activity.AtoolsActivity;
import com.example.dalu.a370project.activity.EnterPwdActivity;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DALU on 2016/7/3.
 */
public class WatchDogSerive extends Service {
    private DbManager db;
    Cursor cursor;


  /*  private InnerReceiver mInnerReceiver;
    private String mSkipPackagename;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    DbManager db;

    public List<String> findAll(){

        List<String> lockPackageList=new ArrayList<String>();;
        try {
            db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
            cursor = db.execQuery("select packname from applock");

            while(cursor.moveToNext()){
                lockPackageList.add(cursor.getString(0));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lockPackageList;
    }
    boolean isWatch;
    @Override
    public void onCreate() {
        super.onCreate();

        isWatch = true;
        watch();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SKIP");

        mInnerReceiver = new InnerReceiver();
        registerReceiver(mInnerReceiver, intentFilter);
    }
    class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取发送广播过程中传递过来的包名,跳过次包名检测过程
            mSkipPackagename = intent.getStringExtra("packageName");
        }
    }
    List<String> lockedAppInfo;
    private void watch() {
       new Thread(){
           @Override
           public void run() {
               lockedAppInfo = findAll();
               while (isWatch) {
                   ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                   List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                   String packageName = runningTasks.get(0).topActivity.getPackageName();
                   if (lockedAppInfo.contains(packageName)) {
                       if (!packageName.equals(mSkipPackagename)) {
                           Intent intent = new Intent(getApplicationContext(), EnterPwdActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           intent.putExtra("packageName", packageName);
                           startActivity(intent);
                       }
                   }
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
       }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }*/


        private boolean isWatch;
        private List<String> mPacknameList;
        private InnerReceiver mInnerReceiver;
        private String mSkipPackagename;
    private MyContentOb myContob;

    @Override
        public void onCreate() {
            //维护一个看门狗的死循环,让其时刻监测现在开启的应用,是否为程序锁中要去拦截的应用
          
            isWatch = true;
            watch();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SKIP");

            mInnerReceiver = new InnerReceiver();
            registerReceiver(mInnerReceiver, intentFilter);
        Uri uri = Uri.parse("content://applock/change");
        myContob = new MyContentOb(new Handler());
            getContentResolver().registerContentObserver(uri,true,myContob);

           
            super.onCreate();
        }

        public List<String> findAll(){

            List<String> lockPackageList=new ArrayList<String>();;
            try {
                db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
                cursor = db.execQuery("select packname from applock");

                while(cursor.moveToNext()){
                    lockPackageList.add(cursor.getString(0));
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lockPackageList;
        }
    class  MyContentOb extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentOb(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    mPacknameList = findAll();
                }
            }.start();
        }
    }

        class InnerReceiver extends BroadcastReceiver{
            @Override
            public void onReceive(Context context, Intent intent) {
                //获取发送广播过程中传递过来的包名,跳过次包名检测过程
                mSkipPackagename = intent.getStringExtra("packagename");
            }
        }

        private void watch() {
            //1,子线程中,开启一个可控死循环
            new Thread(){
                public void run() {
                    mPacknameList = findAll();
                    while(isWatch){
                        //2.监测现在正在开启的应用,任务栈
                        //3.获取activity管理者对象
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        //4.获取正在开启应用的任务栈
                        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
                        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(0);
                        //5.获取栈顶的activity,然后在获取此activity所在应用的包名
                        String mpackagename = runningAppProcessInfo.processName;

                        //如果任务栈指向应用有切换,将mSkipPackagename空字符串
                        Log.e("packagename",mpackagename+"");
                        //6.拿此包名在已加锁的包名集合中去做比对,如果包含次包名,则需要弹出拦截界面
                        if(mPacknameList.contains(mpackagename)){
                            //如果现在检测的程序,以及解锁了,则不需要去弹出拦截界面
                            if(!mpackagename.equals(mSkipPackagename)){
                                //7,弹出拦截界面
                                Intent intent = new Intent(getApplicationContext(),EnterPwdActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("packagename", mpackagename);
                                startActivity(intent);
                            }
                        }
                        //睡眠一下,时间片轮转
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }.start();

        }
        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }
        @Override
        public void onDestroy() {
            //停止看门狗循环
            isWatch = false;
            //注销广播接受者
            if(mInnerReceiver!=null){
                unregisterReceiver(mInnerReceiver);
            }
            if (myContob!=null){
                getContentResolver().unregisterContentObserver(myContob);
            }
            super.onDestroy();
        }
    }

