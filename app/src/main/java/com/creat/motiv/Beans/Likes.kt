package com.creat.motiv.Beans

import com.google.firebase.auth.FirebaseAuth

class Likes {
    var userid: String = ""
    var username: String = ""
    var userpic: String = ""
    internal var istheuser: Boolean = false

    constructor(userid: String, username: String, userpic: String) {
        this.userid = userid
        this.username = username
        this.userpic = userpic
        val user = FirebaseAuth.getInstance().currentUser


    }

    constructor()
}
