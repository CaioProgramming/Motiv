package com.creat.motiv.model.Beans

import java.io.Serializable

class User: Serializable {
    var name: String = ""
    var uid: String = ""
    var token: String = ""
    var picurl: String = ""
    var phonenumber: String = ""
    var email: String = ""

    constructor(name: String, uid: String, token: String, picurl: String, phonenumber: String, email: String) {
        this.name = name
        this.uid = uid
        this.token = token
        this.picurl = picurl
        this.phonenumber = phonenumber
        this.email = email
    }

    constructor()

}
