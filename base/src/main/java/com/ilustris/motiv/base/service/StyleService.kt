package com.ilustris.motiv.base.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.Style
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService

class StyleService : BaseService() {
    override val dataPath = "Styles"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Style {
        return dataSnapshot.toObject(Style::class.java)!!.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Style {
        return dataSnapshot.toObject(Style::class.java).apply {
            this.id = dataSnapshot.id
        }
    }

}