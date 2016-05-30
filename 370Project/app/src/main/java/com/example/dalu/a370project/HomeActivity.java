package com.example.dalu.a370project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by DALU on 2016/4/12.
 */
public class HomeActivity extends Activity {
    private String[] mItems = new String[]{"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
    private int [] pic = new int[]{R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
    R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,R.drawable.home_sysoptimize,
            R.drawable.home_tools,R.drawable.home_settings
    };
    Button btnSave;
    Button btnCancel;
    EditText edpass;
    EditText redpass;
    SharedPreferences mspf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gv_main);
        mspf = getSharedPreferences("config",MODE_PRIVATE);
        gridView.setAdapter(new MyGridAdpater());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showPassDlg();
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this,MycommunicationActivity.class));
                    break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this,AppManageActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this,ProcessManageActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this,AtoolsActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                        break;
                }
            }
        });
    }
    private  void showPassDlg(){
        String passw = mspf.getString("password",null);
        if (!TextUtils.isEmpty(passw)){
            showInputDialog();
        }else {
            showSetDialog();
        }
    }
    private  void showSetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(HomeActivity.this,R.layout.dailog_set_password,null);
        alertDialog.setView(view,0,0,0,0);
        alertDialog.show();
        edpass = (EditText) view.findViewById(R.id.et_password);
        redpass = (EditText) view.findViewById(R.id.et_password_confirm);

        btnSave = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edpass.getText().toString();
                 String Rpassword = redpass.getText().toString();

                if (!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(Rpassword)){
                    if(password.equals(Rpassword)){
                        mspf.edit().putString("password",password).commit();
                        Toast.makeText(HomeActivity.this, "密码设置成功!",
                                Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(HomeActivity.this, "密码不能为空!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(HomeActivity.this,R.layout.dailog_input_password,null);
        alertDialog.setView(view,0,0,0,0);
        alertDialog.show();
        edpass = (EditText) view.findViewById(R.id.et_password);

        btnSave = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String password = edpass.getText().toString();
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(HomeActivity.this, "密码不能为空!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    String surePass = mspf.getString("password",null);
                    if(surePass.equals(password)){
                     /*   Toast.makeText(HomeActivity.this, "成功!",
                                Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();*/
                        startActivity(new Intent(HomeActivity.this,LostAndFound.class));
                    }else{
                        Toast.makeText(HomeActivity.this, "密码错误!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    class  MyGridAdpater extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(HomeActivity.this,R.layout.gv_main_item,null);
            TextView textView = (TextView) v.findViewById(R.id.item_tv);
            ImageView imageView = (ImageView) v.findViewById(R.id.item_iv);
            textView.setText(mItems[position]);
            imageView.setImageResource(pic[position]);
            return v;

        }
    }
}
