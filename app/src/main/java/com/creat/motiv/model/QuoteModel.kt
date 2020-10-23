package com.creat.motiv.model

import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.BasePresenter
import com.google.firebase.firestore.DocumentSnapshot


class QuoteModel(override val presenter: BasePresenter<Quote>, override val path: String = "Quotes") : BaseModel<Quote>() {

    fun getFavorites(uid: String) {
        db().whereArrayContains("likes", uid).get().addOnSuccessListener { documents ->
            val dataList: ArrayList<Quote> = ArrayList()
            for (document in documents) {
                val data = deserializeDataSnapshot(document)
                data?.let {
                    dataList.add(it)
                }
                presenter.onDataRetrieve(dataList)
            }
        }
    }

    fun denunciar(quote: Quote) {
        quote.isReport = true
        editData(quote)
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Quote? {
        return dataSnapshot.toObject(Quote::class.java)
    }
}
