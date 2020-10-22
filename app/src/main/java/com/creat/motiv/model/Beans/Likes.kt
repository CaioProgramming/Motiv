package com.creat.motiv.model.Beans

import com.google.firebase.database.DataSnapshot

class Likes(var userid: String = "", var username: String = "name not found", var userpic: String = "") : BaseBean(userid) {

    override fun convertSnapshot(snapshot: DataSnapshot): Likes? {
        return snapshot.getValue(Likes::class.java)
    }
}
