package com.creat.motiv.model.Beans

import java.io.Serializable

class User: Serializable {
    var name: String? = null
    var uid: String? = null
    var picurl: String? = null

    var token: String? = null
    var phonenumber: String? = null
    var email: String? = null

    constructor(name: String, uid: String, token: String, picurl: String, phonenumber: String, email: String) {
        this.name = name
        this.uid = uid
        this.token = token
        this.picurl = picurl
        this.phonenumber = phonenumber
        this.email = email
    }


    constructor()
    constructor(name: String?, uid: String?, picurl: String?) {
        this.name = name
        this.uid = uid
        this.picurl = picurl
    }

    init {
        showdata()
    }

    fun showdata(){
        //Log.i(javaClass.simpleName,"Usuário \n nome -> ${name} \n uid -> ${uid} \n token-> ${token} \n picurl->${picurl} \n email ->${email} \n")
    }

}
