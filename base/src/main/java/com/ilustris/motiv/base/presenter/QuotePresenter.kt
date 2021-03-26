package com.ilustris.motiv.base.presenter

import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.model.QuoteModel
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuotePresenter(override val view: BaseView<Quote>) : BasePresenter<Quote>() {

    override val model: QuoteModel = QuoteModel(this)

    fun loadFavorites(uid: String, onGetFavorites: (List<Quote>) -> Unit) {
        view.onLoading()
        GlobalScope.launch(Dispatchers.IO) {
            model.getFavorites(uid) {
                GlobalScope.launch(Dispatchers.Main) {
                    onGetFavorites.invoke(it)
                }
            }
        }
    }

    fun reportQuote(quote: Quote) {
        quote.isReport = true
        model.editData(quote)
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
        val user = this.user
        user?.let {
            quote.likes.add(it.uid)
            model.addData(quote, quote.id)
        }
    }

    fun deslikeQuote(quote: Quote) {
        view.onLoading()
        this.user?.let {
            quote.likes.remove(it.uid)
            model.addData(quote, quote.id)
        }
    }

}