package com.example.dalu.a370project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by DALU on 2016/4/23.
 */
public class SetupActivity4 extends BaseSetupActivity {
    SharedPreferences spf ;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spf = getSharedPreferences("config",MODE_PRIVATE);
        boolean flag =spf.getBoolean("protected",false);

        setContentView(R.layout.activity_setup4);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        if (flag) {
            checkBox.setText("防盗保护已经开启");
            checkBox.setChecked(true);
        } else {
            checkBox.setText("防盗保护没有开启");
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    checkBox.setText("防盗已经开启");
                    spf.edit().putBoolean("protected",true).commit();
                }else {
                    checkBox.setText("防盗暂未开启");
                    spf.edit().putBoolean("protected",false).commit();
                }
            }
        });
    }


    @Override
    public void showNextPage() {
        startActivity(new Intent(this,LostAndFound.class));
        finish();
        spf.edit().putBoolean("HasEnter", true).commit();
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,SetupActivity3.class));
        finish();
    }
}
