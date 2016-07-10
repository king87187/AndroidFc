package com.example.dalu.a370project;

import android.app.Application;
import android.os.Environment;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by DALU on 2016/5/11.
 */
public class WangApplication  extends Application {
    private DbManager.DaoConfig daoConfig;
    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
        @Override
        public void onCreate() {
            super.onCreate();
            x.Ext.init(this);//Xutils初始化
            daoConfig = new DbManager.DaoConfig()
                    .setDbName("safeDb")//创建数据库的名称
                    .setDbVersion(1)//数据库版本号
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            // TODO: ...
                            // db.addColumn(...);
                            // db.dropTable(...);
                            // ...
                        }
                    });//数据库更新操作
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"wangError.log";
                    File file = new File(path);
                    try {
                        PrintWriter pw = new PrintWriter(file);
                        ex.printStackTrace(pw);
                        pw.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

}
