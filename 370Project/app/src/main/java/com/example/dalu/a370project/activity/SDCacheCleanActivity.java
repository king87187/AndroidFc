package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by DALU on 2016/7/9.
 */
public class SDCacheCleanActivity  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("sdcardCache");
        setContentView(textView);
    }
}
