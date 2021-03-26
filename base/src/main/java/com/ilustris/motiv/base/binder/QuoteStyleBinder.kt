package com.ilustris.motiv.base.binder

import com.bumptech.glide.Glide
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.databinding.StyleLayoutViewBinding
import com.ilustris.motiv.base.utils.loadGif
import com.silent.ilustriscore.core.view.BaseView

class QuoteStyleBinder(override val viewBind: StyleLayoutViewBinding, private val onFindStyle: (QuoteStyle) -> Unit) : BaseView<QuoteStyle>() {
    override val presenter = QuoteStylePresenter(this)

    fun getStyle(styleID: String) {
        when (styleID) {
            DEFAULT_STYLE_ID -> showData(QuoteStyle.defaultStyle)
            SPLASH_STYLE_ID -> showData(QuoteStyle.splashStyle)
            EMPTY_STYLE_ID -> showData(QuoteStyle.emptyStyle)
            FAVORITES_STYLE_ID -> showData(QuoteStyle.favoriteStyle)
            SEARCH_STYLE_ID -> showData(QuoteStyle.searchStyle)
            else -> presenter.loadSingleData(styleID)
        }
    }

    override fun showData(data: QuoteStyle) {
        super.showData(data)
        viewBind.quoteBack.loadGif(data.backgroundURL)
        onFindStyle.invoke(data)
    }

    override fun initView() {}
}