package com.creat.motiv.model.Beans

import com.google.firebase.database.DataSnapshot

class User(var name: String = "",
           val uid: String = "",
           val token: String = "",
           var picurl: String = "",
           val phonenumber: String = "",
           val email: String = "") : BaseBean(uid) {

    override fun convertSnapshot(snapshot: DataSnapshot): User? {
        return snapshot.getValue(User::class.java)
    }
}
