package com.example.dalu.a370project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.dalu.a370project.customView.SettingClickView;
import com.example.dalu.a370project.customView.SettingItem;
import com.example.dalu.a370project.service.AddressService;
import com.example.dalu.a370project.service.BlackDefenceService;
import com.example.dalu.a370project.utils.ServiceStatusUtils;


/**
 * Created by DALU on 2016/4/18.
 */
public class SettingActivity extends Activity {
    SettingItem settingItem;
    SettingItem settingItemAddress;
    SettingItem settingItemBlack;

    SettingClickView scvAddressStyle;
    SettingClickView scvAddressLocation;
    SharedPreferences mspf;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_setting);
        settingItem = (SettingItem) findViewById(R.id.msv_update);
    mspf=getSharedPreferences("config",MODE_PRIVATE);
        //settingItem.setTitle("开启自动更新");
        initUpdateView();
        initAddressView();
        initAddressLocation();
        initAddressStyle();
        initBlack();
    }

    private void initUpdateView(){

        boolean updateFlag = mspf.getBoolean("auto_update",true);
        if (updateFlag){
            settingItem.setCheckBox(true);
            //settingItem.setDes("自动更新已开启");
        }else {
            settingItem.setCheckBox(false);
            // settingItem.setDes("自动更新已关闭");
        }
        settingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingItem.ischeckOk()){
                    settingItem.setCheckBox(false);
                    // settingItem.setDes("自动更新已关闭");
                    mspf.edit().putBoolean("auto_update",false).commit();
                }else{
                    settingItem.setCheckBox(true);
                    // settingItem.setDes("自动更新已开启");
                    mspf.edit().putBoolean("auto_update",true).commit();
                }
            }
        });
    }
    private  void initBlack(){
        settingItemBlack = (SettingItem) findViewById(R.id.msv_black);

        // 根据黑名单服务是否运行来更新checkbox
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
                "com.example.dalu.a370project.service.BlackDefenceService");

        if (serviceRunning) {
            settingItemBlack.setCheckBox(true);
        } else {
            settingItemBlack.setCheckBox(false);
        }

        settingItemBlack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (settingItemBlack.ischeckOk()) {
                    settingItemBlack.setCheckBox(false);
                    stopService(new Intent(SettingActivity.this,
                            BlackDefenceService.class));// 停止归属地服务
                } else {
                    settingItemBlack.setCheckBox(true);
                    startService(new Intent(SettingActivity.this,
                            BlackDefenceService.class));// 开启归属地服务
                }
            }
        });
    }

    private void initAddressView() {
        settingItemAddress = (SettingItem) findViewById(R.id.msv_address);

        // 根据归属地服务是否运行来更新checkbox
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
                "com.example.dalu.a370project.service.AddressService");

        if (serviceRunning) {
            settingItemAddress.setCheckBox(true);
        } else {
            settingItemAddress.setCheckBox(false);
        }

        settingItemAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (settingItemAddress.ischeckOk()) {
                    settingItemAddress.setCheckBox(false);
                    stopService(new Intent(SettingActivity.this,
                            AddressService.class));// 停止归属地服务
                } else {
                    settingItemAddress.setCheckBox(true);
                    startService(new Intent(SettingActivity.this,
                            AddressService.class));// 开启归属地服务
                }
            }
        });
    }

    final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

    /**
     * 修改提示框显示风格
     */
    private void initAddressStyle() {
        scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);

        scvAddressStyle.setTitle("归属地提示框风格");

        int style = mspf.getInt("address_style", 0);// 读取保存的style
        scvAddressStyle.setDesc(items[style]);

        scvAddressStyle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSingleChooseDailog();
            }
        });
    }

    /**
     * 弹出选择风格的单选框
     */
    protected void showSingleChooseDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("归属地提示框风格");

        int style = mspf.getInt("address_style", 0);// 读取保存的style

        builder.setSingleChoiceItems(items, style,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mspf.edit().putInt("address_style", which).commit();// 保存选择的风格
                        dialog.dismiss();// 让dialog消失

                        scvAddressStyle.setDesc(items[which]);// 更新组合控件的描述信息
                    }
                });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 修改归属地显示位置
     */
    private void initAddressLocation() {
        scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示位置");
        scvAddressLocation.setDesc("设置归属地提示框的显示位置");

        scvAddressLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,
                        DragViewActivity.class));
            }
        });
    }
}
