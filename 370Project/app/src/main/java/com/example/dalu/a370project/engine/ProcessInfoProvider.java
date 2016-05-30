package com.example.dalu.a370project.engine;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.dao.ProcessInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DALU on 2016/5/25.
 */
public class ProcessInfoProvider {
    //获取进程总数的方法
    public static int getProcessCount(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        return runningAppProcesses.size();
    }
    public static long getAvailSpace(Context ctx){
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        return  info.availMem;
    }
    public static long getTotalSpace(Context ctx){
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        return  info.totalMem;
    }
    public static List<ProcessInfo> getProcessInfo(Context ctx){

        List<ProcessInfo> list = new ArrayList<ProcessInfo>();
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = ctx.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info :runningAppProcesses){
            ProcessInfo pInfo = new ProcessInfo();
            pInfo.packageName = info.processName;
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{info.pid});
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            pInfo.memSize = memoryInfo.getTotalPrivateDirty()*1024;

            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(info.processName, 0);
                pInfo.name = applicationInfo.loadLabel(pm).toString();
                pInfo.icon = applicationInfo.loadIcon(pm);
                if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                    pInfo.isSystem = true;
                }else {
                    pInfo.isSystem = false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                pInfo.name = info.processName;
                pInfo.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher);
                pInfo.isSystem = false;
                e.printStackTrace();
            }
            list.add(pInfo);
        }
        return  list;
    }
}
