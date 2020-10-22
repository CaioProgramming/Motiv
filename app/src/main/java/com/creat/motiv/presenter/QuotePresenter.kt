package com.creat.motiv.presenter

import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.view.BaseView

class QuotePresenter(override val view: BaseView<Quote>) : BasePresenter<Quote>() {

    override val model: BaseModel<Quote> = QuoteModel(this)


    fun loadFavorites() {

    }

    override fun onDataRetrieve(data: List<Quote>) {
        TODO("Not yet implemented")
    }

    override fun onSingleData(data: Quote) {
        view.showData(data)
    }

    override fun onError() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

}
