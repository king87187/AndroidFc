package com.example.dalu.a370project.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dalu.a370project.R;

/**
 * Created by DALU on 2016/4/18.
 */
public class SettingItem extends RelativeLayout {
    private TextView textView1;
    private TextView textView2;
    private CheckBox checkBox;
    private String mTitle;
    private String mDescOff;
    private String mDescOn;
    private static final String NAMESPACE ="http://schemas.android.com/apk/res/com.example.dalu.a370project";
    public SettingItem(Context context) {
        super(context);
        initView();
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTitle = attrs.getAttributeValue(NAMESPACE, "titleName");// 根据属性名称,获取属性的值
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        initView();

    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
      View.inflate(getContext(), R.layout.item_setting,this);
        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        setTitle(mTitle);
    }
    public  void setTitle(String title){
        textView1.setText(title);
    }
    public  void setDes(String title){
        textView2.setText(title);
    }
    public boolean ischeckOk(){
        return checkBox.isChecked();
    }
    public  void setCheckBox(boolean flag){
        checkBox.setChecked(flag);
        if(flag){
            setDes(mDescOn);
        }else {
            setDes(mDescOff);
        }
    }

}
