package com.douguo.dc.model;

/**
 * Created by lichang on 2017/9/13.
 */
public class RegisterUserModel {
    private String dateTime;
    private int total;
    private String type;
    private String name;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "RegisterUserModel{" +
                "dateTime='" + dateTime + '\'' +
                ", total=" + total +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
