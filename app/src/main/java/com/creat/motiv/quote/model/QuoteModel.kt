package com.creat.motiv.quote.model

import com.creat.motiv.model.beans.Quote
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter

class QuoteModel(presenter: BasePresenter<Quote>, override val path: String = "Quotes") : BaseModel<Quote>(presenter) {

    fun getFavorites(uid: String) {
        this.reference.whereArrayContains("likes", uid).addSnapshotListener(this)
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Quote {
        return dataSnapshot.toObject(Quote::class.java)!!.apply {
            id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Quote {
        return dataSnapshot.toObject(Quote::class.java).apply {
            id = dataSnapshot.id
        }
    }


}