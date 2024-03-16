package org.quickjedis.test;

import org.joda.time.DateTime;

public class UserInfo {

    private String name;
    private int age;
    private Boolean isMale;
    private DateTime time;

    public UserInfo(String name, int age, Boolean ismale) {
        this.name = name;
        this.age = age;
        this.isMale = ismale;
        this.time = new DateTime("2024-03-17T01:54:20.407+08:00");
        System.out.println("UserInfo Create");
    }

    public UserInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean ismale) {
        this.isMale = ismale;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

}
