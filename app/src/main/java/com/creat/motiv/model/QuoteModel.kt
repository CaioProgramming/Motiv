package com.creat.motiv.model

import android.util.Log
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

    fun queryQuote(search: String, field: String = "quote") {
        Log.i(javaClass.simpleName, "queryQuote: Searching for $search in $field")
        db().whereEqualTo(field, search).addSnapshotListener { value, error ->
            if (error != null) {
                errorMessage("Ocorreu um erro ao buscar informações ${error.message}")
            } else if (value == null ||
                    value.isEmpty && field != "author") {
                queryQuote("author")
            } else {
                val dataList: ArrayList<Quote> = ArrayList()
                for (doc in value) {
                    deserializeDataSnapshot(doc)?.let { dataList.add(it) }
                }
                presenter.modelCallBack(successMessage("Dados recebidos: $dataList"))
                presenter.onDataRetrieve(dataList)
            }


        }
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
