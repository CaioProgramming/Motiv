package com.ilustris.motiv.base.binder

import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.databinding.StyleLayoutViewBinding
import com.ilustris.motiv.base.utils.loadGif
import com.silent.ilustriscore.core.view.BaseView

class QuoteStyleBinder(override val viewBind: StyleLayoutViewBinding, private val onFindStyle: (Style) -> Unit) : BaseView<Style>() {
    override val presenter = QuoteStylePresenter(this)

    fun getStyle(styleID: String) {
        when (styleID) {
            DEFAULT_STYLE_ID -> showData(Style.defaultStyle)
            SPLASH_STYLE_ID -> showData(Style.splashStyle)
            EMPTY_STYLE_ID -> showData(Style.emptyStyle)
            FAVORITES_STYLE_ID -> showData(Style.favoriteStyle)
            SEARCH_STYLE_ID -> showData(Style.searchStyle)
            else -> presenter.loadSingleData(styleID)
        }
    }

    override fun showData(data: Style) {
        super.showData(data)
        viewBind.quoteBack.loadGif(data.backgroundURL)
        onFindStyle.invoke(data)
    }

    override fun initView() {}
}