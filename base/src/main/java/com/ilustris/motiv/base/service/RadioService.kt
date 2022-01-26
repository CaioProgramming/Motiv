package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.Radio
import com.silent.ilustriscore.core.model.BaseService

class RadioService : BaseService() {
    override val dataPath = "Radios"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Radio {
        return dataSnapshot.toObject(Radio::class.java)!!.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Radio {
        return dataSnapshot.toObject(Radio::class.java).apply {
            this.id = dataSnapshot.id
        }
    }

}