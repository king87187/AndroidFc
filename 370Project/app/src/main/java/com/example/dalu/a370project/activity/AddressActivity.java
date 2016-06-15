package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.db.AddressDb;


/**
 * Created by DALU on 2016/5/10.
 */
public class AddressActivity extends Activity {

    private EditText ed_num;
    private TextView ed_result;
    String address ="http://apis.juhe.cn/mobile/get?key=21e1ec39f72aa66b3a5acbfde4ab70c0&";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ed_num = (EditText) findViewById(R.id.et_number);
        ed_result= (TextView) findViewById(R.id.tv_result);
    }
    public String pinAddress(String txt){
        return address+"phone="+txt;
    }
    public void query(View v){
        String address = AddressDb.getAddress(ed_num.getText().toString());
        ed_result.setText(address);
        /*String Paddress = pinAddress(ed_num.getText().toString());
        //Log.e("wang",Paddress);
        RequestParams params = new RequestParams(Paddress);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String jsonString = JSON.toJSONString(result);
                Log.e("wang",result);
                JUHeAddress juHeAddress= JSON.parseObject(result, JUHeAddress.class);
                if(juHeAddress.getResultcode().equals("200")){
                    String addResult = juHeAddress.getResult();
                    Log.e("wang",juHeAddress.toString());
                    AddressDao addressDao = JSON.parseObject(addResult,AddressDao.class);
                    Log.e("wang",addressDao.toString());
                    ed_result.setText(addressDao.getCard());
                }else{
                    ed_result.setText(juHeAddress.getResult());
                }


            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });*/

    }
}
