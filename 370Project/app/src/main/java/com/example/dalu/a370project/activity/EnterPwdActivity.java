package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalu.a370project.R;

/**
 * Created by DALU on 2016/7/3.
 */
public class EnterPwdActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String packageName = intent.getStringExtra("packagename");
        setContentView(R.layout.activity_enter_psd);
        tv_app_name = (TextView) findViewById(R.id.tv_app_name);
       PackageManager pm = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = pm.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_app_name.setText(applicationInfo.loadLabel(pm));
        Drawable drawable = applicationInfo.loadIcon(pm);
       // iv_app_icon.setImageDrawable(drawable);
        iv_app_icon = (ImageView) findViewById(R.id.iv_app_icon);
        et_psd = (EditText) findViewById(R.id.et_psd);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd = et_psd.getText().toString();
                if(!TextUtils.isEmpty(psd)){
                    if(psd.equals("123")){
                        //解锁,进入应用,告知看门口不要再去监听以及解锁的应用,发送广播
                        Intent intent = new Intent("android.intent.action.SKIP");
                        intent.putExtra("packagename",packageName);
                        sendBroadcast(intent);

                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "密码错误",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "请输入密码",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        //通过隐式意图,跳转到桌面
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        super.onBackPressed();
    }

    TextView tv_app_name;
   ImageView iv_app_icon;
    EditText et_psd;
    Button bt_submit;
}
