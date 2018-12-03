package com.creat.motiv.Beans;

public class Likes {
    String userid,username,userpic;

    public Likes(String userid, String username, String userpic) {
        this.userid = userid;
        this.username = username;
        this.userpic = userpic;
    }

    public Likes() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
