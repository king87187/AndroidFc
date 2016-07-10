package com.example.dalu.a370project.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DALU on 2016/5/14.
 */
public class VirusDb {
    private static final String PATH = "data/data/com.example.dalu.a370project/files/antivirus.db";// 注意,该路径必须是data/data目录的文件,否则数据库访问不到

        public static List<String> getVirusInfo(){
            SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.query("datable", new String[]{"md5"}, null, null, null, null, null);
            List<String> viruList  = new ArrayList<String>();
            while (cursor.moveToNext()){
                viruList.add(cursor.getString(0));
            }
            cursor.close();
            db.close();
            return viruList;
        }

}
