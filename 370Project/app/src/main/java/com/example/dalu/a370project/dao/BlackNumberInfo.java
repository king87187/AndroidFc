package com.example.dalu.a370project.dao;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="blacklist")
public class BlackNumberInfo {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 黑名单电话号码
     */

    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "number")
    private String number;
    /**
     * 黑名单拦截模式
     * 1 全部拦截 电话拦截 + 短信拦截
     * 2 电话拦截
     * 3 短信拦截
     */
    @Column(name = "mode")
    private String mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
