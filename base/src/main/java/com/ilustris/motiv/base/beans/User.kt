package com.ilustris.motiv.base.beans

import com.google.firebase.auth.FirebaseUser
import com.silent.ilustriscore.core.bean.BaseBean

const val DEFAULT_USER_BACKGROUND = "https://media.giphy.com/media/l49JLNvR1W5y0w5sA/giphy.gif"

data class User(var name: String = "",
                var uid: String = "",
                var token: String = "",
                val admin: Boolean = false,
                var cover: String = DEFAULT_USER_BACKGROUND,
                var followers: ArrayList<User> = ArrayList(),
                var picurl: String = "") : BaseBean(uid) {


    companion object {


        fun fromFirebase(firebaseUser: FirebaseUser): User {
            return User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "Desconhecido"
            )
        }
    }
}
