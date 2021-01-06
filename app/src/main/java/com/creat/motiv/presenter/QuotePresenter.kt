package com.creat.motiv.presenter

import com.creat.motiv.model.QuoteModel
import com.creat.motiv.model.beans.Quote
import com.ilustriscore.core.base.BasePresenter
import com.ilustriscore.core.base.BaseView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuotePresenter(override val view: BaseView<Quote>) : BasePresenter<Quote>() {

    override val model: QuoteModel = QuoteModel(this)

    fun loadFavorites(uid: String) {
        view.onLoading()
        GlobalScope.launch {
            model.getFavorites(uid)
        }

    }


    fun deleteAll(data: List<Quote>) {
        view.onLoading()
        model.deleteAllData(data)
    }

    fun delete(id: String) {
        view.onLoading()
        model.deleteData(id)
    }

    fun likeQuote(quote: Quote) {
        view.onLoading()
        val user = currentUser
        user?.let {
            quote.likes.add(user.uid)
            model.addData(quote, quote.id)
        }
    }

    fun deslikeQuote(quote: Quote) {
        view.onLoading()
        val user = currentUser
        user?.let {
            quote.likes.remove(user.uid)
            model.addData(quote, quote.id)
        }
    }

}
