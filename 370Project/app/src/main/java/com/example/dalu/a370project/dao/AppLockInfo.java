package com.example.dalu.a370project.dao;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="applock")
public class AppLockInfo {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "packname")
    private String packname;


    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }
}
