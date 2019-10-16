package com.creat.motiv.Model.Beans

class Likes {
    var userid: String = ""
    var username: String = ""
    var userpic: String = ""

    constructor(userid: String, username: String, userpic: String) {
        this.userid = userid
        this.username = username
        this.userpic = userpic
    }

    constructor()
}
