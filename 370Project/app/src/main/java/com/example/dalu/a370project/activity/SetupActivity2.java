package com.example.dalu.a370project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.customView.SettingItem;

/**
 * Created by DALU on 2016/4/23.
 */
public class SetupActivity2 extends BaseSetupActivity {
    private SettingItem settingItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        final SharedPreferences mspf =getSharedPreferences("config",MODE_PRIVATE);
        settingItem = (SettingItem) findViewById(R.id.siv_sim);
        String sim = mspf.getString("sim",null);
        if(TextUtils.isEmpty(sim)){
            settingItem.setCheckBox(false);

        }else {
            settingItem.setCheckBox(true);
        }
        settingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingItem.ischeckOk()){
                    settingItem.setCheckBox(false);
                    mspf.edit().remove("sim").commit();
                }else{
                  TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                   String ssn =  tm.getSimSerialNumber();
                    Log.e("sim",ssn);
                    mspf.edit().putString("sim",ssn).commit();
                    settingItem.setCheckBox(true);
                }
            }
        });
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this,SetupActivity3.class));
        finish();
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,SetupActivity1.class));
        finish();
    }
}
