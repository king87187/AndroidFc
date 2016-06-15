package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.dalu.a370project.R;

/**
 * Created by DALU on 2016/4/23.
 */
public class LostAndFound extends Activity {
    SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        spf = getSharedPreferences("config",MODE_PRIVATE);
        boolean flag = spf.getBoolean("HasEnter",false);
        if(flag){
            setContentView(R.layout.activity_lost_find);
        }else{
            startActivity(new Intent(this, SetupActivity1.class));
            finish();
        }

    }
    public void reEnter(View view) {
        startActivity(new Intent(this, SetupActivity1.class));
        finish();
    }
}
