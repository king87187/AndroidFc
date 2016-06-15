package com.example.dalu.a370project.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DALU on 2016/6/14.
 */
public class CommonnumDao {
    private static final String PATH = "data/data/com.example.dalu.a370project/files/commonnum.db";// 注意,该路径必须是data/data目录的文件,否则数据库访问不到
    public  List<Group> getGroup(){
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(PATH,null,SQLiteDatabase.OPEN_READONLY);
        Cursor classlist = sqLiteDatabase.query("classlist", new String[]{"name", "idx"}, null, null, null, null, null);
        List<Group> list = new ArrayList<Group>();
        while (classlist.moveToNext()){
            Group g = new Group();
            g.name = classlist.getString(0);
            g.idx = classlist.getString(1);
            g.clist = getChild(g.idx);
            list.add(g);
        }
        classlist.close();
        sqLiteDatabase.close();
        return list;
    }
    public List<Child> getChild(String idx){
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(PATH,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from table" + idx + ";", null);
        List<Child> lst = new ArrayList<Child>();
        while (cursor.moveToNext()){
            Child g = new Child();
            g._id = cursor.getString(0);
            g.number = cursor.getString(1);
            g.name = cursor.getString(2);
        lst.add(g);
        }

        cursor.close();
        sqLiteDatabase.close();
        return  lst;
    }
    public class Group{
        public String name;
        public String idx;
        public List<Child> clist;
    }

    public class Child{
        public String name;
        public String _id;
        public String number;
    }
}
