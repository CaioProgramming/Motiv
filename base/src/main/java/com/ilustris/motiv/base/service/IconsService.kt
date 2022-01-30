package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.Icon
import com.silent.ilustriscore.core.model.BaseService

class IconsService : BaseService() {
    override val dataPath = "Icons"
    override var requireAuth = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Icon {
        return dataSnapshot.toObject(Icon::class.java)!!.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Icon {
        return dataSnapshot.toObject(Icon::class.java).apply {
            this.id = dataSnapshot.id
        }
    }

}