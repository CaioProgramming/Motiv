package com.creat.motiv.model.beans

import com.google.firebase.auth.FirebaseUser
import com.ilustriscore.core.base.BaseBean

data class User(var name: String = "",
                val uid: String = "",
                var token: String = "",
                val admin: Boolean = false,
                var followers: ArrayList<User> = ArrayList(),
                var picurl: String = "") : BaseBean(uid) {


    companion object {
        fun fromFirebaseWithToken(firebaseUser: FirebaseUser, token: String): User {
            return User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "Desconhecido",
                    picurl = firebaseUser.photoUrl.toString(),
                    token = token
            )
        }

        fun fromFirebase(firebaseUser: FirebaseUser): User {
            return User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "Desconhecido",
                    picurl = firebaseUser.photoUrl.toString()
            )
        }
    }
}
