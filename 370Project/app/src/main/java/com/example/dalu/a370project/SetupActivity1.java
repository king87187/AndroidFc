package com.example.dalu.a370project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by DALU on 2016/4/23.
 */
public class SetupActivity1 extends BaseSetupActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }


    @Override
    public void showNextPage() {
        startActivity(new Intent(this,SetupActivity2.class));
        finish();
    }

    @Override
    public void showPreviousPage() {

    }
   /* private void previous(View v){
        startActivity(new Intent(this,SetupActivity2.class));
        finish();
    }*/
}
