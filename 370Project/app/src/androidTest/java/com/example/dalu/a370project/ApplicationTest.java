package com.example.dalu.a370project;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.dalu.a370project.dao.BlackNumberInfo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void  test(){
        DbManager db = x.getDb(((WangApplication) mContext.getApplicationContext()).getDaoConfig());
        BlackNumberInfo person1 =null;
        for(int i =0 ;i<100;i++){
           person1 =new BlackNumberInfo();
            person1.setNumber("1805468901"+i);
            person1.setMode("1");


        try {
            db.save(person1);
        } catch (DbException e) {
            e.printStackTrace();
        }
        }
    }

}