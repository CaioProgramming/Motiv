package com.creat.motiv.model.beans

import com.google.firebase.auth.FirebaseUser

data class User(var name: String = "",
                val uid: String = "",
                val token: String = "",
                val admin: Boolean = false,
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
