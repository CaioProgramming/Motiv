package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.data.model.Cover
import com.silent.ilustriscore.core.service.BaseService

class CoverService : BaseService() {
    override val dataPath = "Covers"
    override val requireAuth: Boolean = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Cover {
        return dataSnapshot.toObject(Cover::class.java)!!.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Cover {
        return dataSnapshot.toObject(Cover::class.java).apply {
            this.id = dataSnapshot.id
        }
    }

}