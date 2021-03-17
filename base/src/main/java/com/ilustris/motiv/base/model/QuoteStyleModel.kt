package com.ilustris.motiv.base.model

import com.ilustris.motiv.base.beans.QuoteStyle
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter

class QuoteStyleModel(presenter: BasePresenter<QuoteStyle>) : BaseModel<QuoteStyle>(presenter) {
    override val path = "Styles"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): QuoteStyle {
        return dataSnapshot.toObject(QuoteStyle::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): QuoteStyle {
        return dataSnapshot.toObject(QuoteStyle::class.java).apply {
            id = dataSnapshot.id
        }
    }
}