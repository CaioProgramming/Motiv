package com.creat.motiv.model

import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot


class QuoteModel(override val presenter: BasePresenter<Quote>, override val path: String = "Quotes") : BaseModel<Quote>() {

    fun getFavorites(uid: String) {
        db().whereArrayContains("likes", uid).addSnapshotListener(this)
    }

    fun denunciar(quote: Quote) {
        quote.isReport = true
        editData(quote)
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Quote? {
        val q = dataSnapshot.toObject(Quote::class.java)
        q?.id = dataSnapshot.id
        return q
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Quote? {
        val q = dataSnapshot.toObject(Quote::class.java)
        q.id = dataSnapshot.id
        return q
    }
}
