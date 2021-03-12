package com.ilustris.motiv.base.presenter

import com.creat.motiv.quote.beans.*
import com.ilustris.motiv.base.model.QuoteStyleModel
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class QuoteStylePresenter(override val view: BaseView<QuoteStyle>) : BasePresenter<QuoteStyle>() {
    override val model = QuoteStyleModel(this)

    override fun loadSingleData(key: String) {
        when (key) {
            DEFAULT_STYLE_ID -> onSingleData(QuoteStyle.defaultStyle)
            SPLASH_STYLE_ID -> onSingleData(QuoteStyle.splashStyle)
            EMPTY_STYLE_ID -> onSingleData(QuoteStyle.emptyStyle)
            FAVORITES_STYLE_ID -> onSingleData(QuoteStyle.favoriteStyle)
            SEARCH_STYLE_ID -> onSingleData(QuoteStyle.searchStyle)
            ADMIN_STYLE_ID -> onSingleData(QuoteStyle.adminStyle)
            else -> model.getSingleData(key)
        }
    }
}