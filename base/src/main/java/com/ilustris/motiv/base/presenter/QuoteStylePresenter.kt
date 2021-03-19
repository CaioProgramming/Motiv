package com.ilustris.motiv.base.presenter

import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.model.QuoteStyleModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class QuoteStylePresenter(override val view: BaseView<QuoteStyle>) : BasePresenter<QuoteStyle>() {
    override val model = QuoteStyleModel(this)

    override fun loadSingleData(key: String) {
        try {
            when (key) {
                DEFAULT_STYLE_ID -> onSingleData(QuoteStyle.defaultStyle)
                SPLASH_STYLE_ID -> onSingleData(QuoteStyle.splashStyle)
                EMPTY_STYLE_ID -> onSingleData(QuoteStyle.emptyStyle)
                FAVORITES_STYLE_ID -> onSingleData(QuoteStyle.favoriteStyle)
                SEARCH_STYLE_ID -> onSingleData(QuoteStyle.searchStyle)
                ADMIN_STYLE_ID -> onSingleData(QuoteStyle.adminStyle)
                COMPANY_STYLE_ID -> onSingleData(QuoteStyle.companyStyle)
                else -> model.getSingleData(key)
            }
        } catch (e: Exception) {
            onSingleData(QuoteStyle.defaultStyle)
        } catch (d: Throwable) {
            onSingleData(QuoteStyle.defaultStyle)
        }
    }


}