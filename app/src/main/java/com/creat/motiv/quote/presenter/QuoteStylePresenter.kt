package com.creat.motiv.quote.presenter

import com.creat.motiv.quote.model.QuoteStyleModel
import com.creat.motiv.quote.beans.QuoteStyle
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class QuoteStylePresenter(override val view: BaseView<QuoteStyle>) : BasePresenter<QuoteStyle>() {
    override val model = QuoteStyleModel(this)
}