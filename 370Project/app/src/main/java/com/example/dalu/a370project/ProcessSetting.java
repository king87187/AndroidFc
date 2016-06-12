package com.example.dalu.a370project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.dalu.a370project.service.LockScreenService;
import com.example.dalu.a370project.utils.ConstantValue;
import com.example.dalu.a370project.utils.ServiceStatusUtils;
import com.example.dalu.a370project.utils.SpUtil;

/**
 * Created by DALU on 2016/6/6.
 */
public class ProcessSetting extends Activity {
    CheckBox cb_show_system;
    CheckBox cb_sc_clean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_process_setting);
        super.onCreate(savedInstanceState);
        initUi();
        initUi2();
    }

    private void initUi() {

        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
        boolean flag =SpUtil.getBoolean(this, ConstantValue.SHOW_SYSTEM,false);
        cb_show_system.setChecked(flag);
        if(flag){
            cb_show_system.setText("显示系统进程");
        }else {
            cb_show_system.setText("隐藏系统进程");
        }
        cb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_show_system.setText("显示系统进程");
                }else {
                    cb_show_system.setText("隐藏系统进程");
                }
                SpUtil.putBoolean(ProcessSetting.this,ConstantValue.SHOW_SYSTEM,isChecked);
            }
        });
    }
    private void initUi2() {


        cb_sc_clean = (CheckBox) findViewById(R.id.cb_lock_clear);
        boolean flag = ServiceStatusUtils.isServiceRunning(this,"com.example.dalu.a370project.service.LockScreenService");
        cb_sc_clean.setChecked(flag);
        if(flag){
            cb_sc_clean.setText("开启锁屏清理");
        }else {
            cb_sc_clean.setText("关闭锁屏清理");
        }
        cb_sc_clean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_sc_clean.setText("开启锁屏清理");
                    startService(new Intent(getApplicationContext(), LockScreenService.class));

                }else {
                    cb_sc_clean.setText("关闭锁屏清理");
                    stopService(new Intent(getApplicationContext(), LockScreenService.class));

                }

            }
        });
    }
}
