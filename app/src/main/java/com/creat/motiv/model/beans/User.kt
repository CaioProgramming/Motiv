package com.creat.motiv.model.beans

import com.google.firebase.auth.FirebaseUser

data class User(var name: String = "",
                val uid: String = "",
                val token: String = "",
                var picurl: String = "",
                val phonenumber: String = "",
                val email: String = "") : BaseBean(uid) {


    companion object {
        fun fromFirebase(firebaseUser: FirebaseUser): User {
            return User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "Nome não informado",
                    picurl = firebaseUser.photoUrl.toString(),
                    phonenumber = firebaseUser.phoneNumber ?: "",
                    email = firebaseUser.email ?: ""
            )
        }
    }
}
