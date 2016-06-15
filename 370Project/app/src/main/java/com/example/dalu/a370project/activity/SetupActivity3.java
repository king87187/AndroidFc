package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;

import com.example.dalu.a370project.R;

/**
 * Created by DALU on 2016/4/23.
 */
public class SetupActivity3 extends BaseSetupActivity {
    private String usernumber;
    private EditText mPhoneNum;
    private  SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        mPhoneNum = (EditText) findViewById(R.id.mPhoneNum);
         spf =getSharedPreferences("config",MODE_PRIVATE);
        String num = spf.getString("parentNum","");
        mPhoneNum.setText(num);
    }

    public void chooseContact(View view){
        startActivityForResult(new Intent(
                Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
    }
    @Override
    public void showNextPage() {
        startActivity(new Intent(this,SetupActivity4.class));
        finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//ContentProvider展示数据类似一个单个数据库表
//ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            //URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            //查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            //获得DATA表中的名字
            String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //条件为联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phone.moveToNext()) {
                usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                mPhoneNum.setText(usernumber);
                spf.edit().putString("parentNum",usernumber).commit();
            }

        }
    }
    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,SetupActivity2.class));
        finish();
    }
}
