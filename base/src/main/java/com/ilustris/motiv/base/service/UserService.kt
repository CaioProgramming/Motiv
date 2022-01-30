package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.model.BaseService

class UserService : BaseService() {
    override val dataPath = "Users"
    override var requireAuth = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): User? {
        return dataSnapshot.toObject(User::class.java)?.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): User {
        return dataSnapshot.toObject(User::class.java).apply {
            this.id = dataSnapshot.id
        }
    }
}