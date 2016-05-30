package com.example.dalu.a370project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by DALU on 2016/5/10.
 */
public class AtoolsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }
    public void searchFrom(View v){
        startActivity(new Intent(AtoolsActivity.this,AddressActivity.class));
    }
}
