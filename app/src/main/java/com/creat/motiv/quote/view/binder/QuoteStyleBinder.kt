package com.creat.motiv.quote.view.binder

import com.bumptech.glide.Glide
import com.creat.motiv.databinding.StyleLayoutViewBinding
import com.creat.motiv.quote.presenter.QuoteStylePresenter
import com.creat.motiv.quote.beans.*
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
        Glide.with(context).asGif().load(data.backgroundURL).centerCrop().into(viewBind.quoteBack)
        onFindStyle.invoke(data)
    }

    override fun initView() {}
}