package com.ilustris.motiv.base.model

import com.ilustris.motiv.base.beans.Style
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter

class QuoteStyleModel(presenter: BasePresenter<Style>) : BaseModel<Style>(presenter) {
    override val path = "Styles"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Style {
        return dataSnapshot.toObject(Style::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Style {
        return dataSnapshot.toObject(Style::class.java).apply {
            id = dataSnapshot.id
        }
    }
}