package com.creat.motiv.presenter

import com.creat.motiv.model.QuoteModel
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.view.BaseView

class QuotePresenter(override val view: BaseView<Quote>) : BasePresenter<Quote>() {

    override val model: QuoteModel = QuoteModel(this)


    fun loadFavorites(uid: String) {
        model.getFavorites(uid)
    }

    fun deleteAll(data: List<Quote>) {
        model.deleteAllData(data)
    }

    fun delete(id: String) {
        model.deleteData(id)
    }

    override fun onDataRetrieve(data: List<Quote>) {
        view.showListData(data)
    }

    override fun onSingleData(data: Quote) {
        view.showData(data)
    }

    fun likeQuote(quote: Quote) {
        val user = currentUser()
        user?.let {
            quote.likes.remove(user.uid)
            model.editData(quote)
        }
    }

    fun deslikeQuote(quote: Quote) {
        val user = currentUser()
        user?.let {
            quote.likes.remove(user.uid)
            model.editData(quote)
        }
    }


}
