package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.Developer
import com.silent.ilustriscore.core.model.BaseService

class DeveloperService : BaseService() {
    override val dataPath = "Developers"
    override var requireAuth = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Developer {
        return dataSnapshot.toObject(Developer::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Developer {
        return dataSnapshot.toObject(Developer::class.java).apply {
            id = dataSnapshot.id
        }
    }
}