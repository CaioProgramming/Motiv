package com.creat.motiv.presenter

import com.creat.motiv.model.QuoteModel
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.view.BaseView

class QuotePresenter(override val view: BaseView<Quote>) : BasePresenter<Quote>() {

    override val model: QuoteModel = QuoteModel(this)


    fun loadFavorites() {
        model.getFavorites()
    }

    fun deleteAll(data: List<Quote>) {
        model.deleteAllData(data)
    }

    override fun onDataRetrieve(data: List<Quote>) {
        view.showListData(data)
    }

    override fun onSingleData(data: Quote) {
        view.showData(data)
    }


}
