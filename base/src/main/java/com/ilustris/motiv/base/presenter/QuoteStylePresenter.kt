package com.ilustris.motiv.base.presenter

import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.model.QuoteStyleModel
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class QuoteStylePresenter(override val view: BaseView<Style>) : BasePresenter<Style>() {
    override val model = QuoteStyleModel(this)

    override fun loadSingleData(key: String) {
        try {
            when (key) {
                DEFAULT_STYLE_ID -> onSingleData(Style.defaultStyle)
                SPLASH_STYLE_ID -> onSingleData(Style.splashStyle)
                EMPTY_STYLE_ID -> onSingleData(Style.emptyStyle)
                FAVORITES_STYLE_ID -> onSingleData(Style.favoriteStyle)
                SEARCH_STYLE_ID -> onSingleData(Style.searchStyle)
                ADMIN_STYLE_ID -> onSingleData(Style.adminStyle)
                else -> model.getSingleData(key)
            }
        } catch (e: Exception) {
            onSingleData(Style.defaultStyle)
        } catch (d: Throwable) {
            onSingleData(Style.defaultStyle)
        }
    }


}