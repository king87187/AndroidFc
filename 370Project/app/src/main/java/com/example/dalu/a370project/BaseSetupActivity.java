package com.example.dalu.a370project;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by DALU on 2016/4/25.
 */
public abstract  class BaseSetupActivity extends Activity {
    public GestureDetector mGod;
    final  int FLIP_DISTANCE= 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGod = new GestureDetector(this
        ,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 500) {
                    Toast.makeText(BaseSetupActivity.this, "不能这样划哦!",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                // 判断滑动是否过慢
                if (Math.abs(velocityX) < 100) {
                    Toast.makeText(BaseSetupActivity.this, "滑动的太慢了!",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                else {
                    if (e2.getRawX()-e1.getRawX()>FLIP_DISTANCE){
                        showPreviousPage();
                        return  true;
                    }
                    else if (e1.getRawX()-e2.getRawY()>FLIP_DISTANCE){
                        showNextPage();
                        return  true;
                    }
                }
                return false;
            }
        }
        );
    }
    public void next(View v){
        showNextPage();
    }
    public void previous(View v){
        showPreviousPage();
    }
    /**
     * 展示下一页, 子类必须实现
     */
    public abstract void showNextPage();

    /**
     * 展示上一页, 子类必须实现
     */
    public abstract void showPreviousPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mGod.onTouchEvent(event);// 委托手势识别器处理触摸事件
        return super.onTouchEvent(event);
    }
}
