package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.data.model.Style
import com.silent.ilustriscore.core.service.BaseService

class StyleService : BaseService() {

    override val dataPath = "Styles"
    override val requireAuth: Boolean = true
    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Style? =
        dataSnapshot.toObject(Style::class.java).apply {
            this?.id = dataSnapshot.id
        }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Style =
        dataSnapshot.toObject(Style::class.java).apply {
            this.id = dataSnapshot.id
        }
}