package com.creat.motiv.Beans;

public class User {
    String name;
    String uid;
    String token;
    String picurl;
    String phonenumber;
    String email;

    public User(String name, String uid, String token, String picurl, String phonenumber, String email) {
        this.name = name;
        this.uid = uid;
        this.token = token;
        this.picurl = picurl;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public User() {
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

}
