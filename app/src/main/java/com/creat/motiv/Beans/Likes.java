package com.creat.motiv.Beans;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Likes {
    String userid,username,userpic;
    boolean istheuser;

    public Likes(String userid, String username, String userpic) {
        this.userid = userid;
        this.username = username;
        this.userpic = userpic;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


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
