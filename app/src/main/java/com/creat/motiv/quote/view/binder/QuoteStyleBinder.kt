package com.creat.motiv.quote.view.binder

import com.bumptech.glide.Glide
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.databinding.StyleLayoutViewBinding
import com.silent.ilustriscore.core.view.BaseView

class QuoteStyleBinder(override val viewBind: StyleLayoutViewBinding, private val onFindStyle: (QuoteStyle) -> Unit) : BaseView<QuoteStyle>() {
    override val presenter = QuoteStylePresenter(this)

    fun getStyle(styleID: String) {
        presenter.loadSingleData(styleID)
    }

    override fun showData(data: QuoteStyle) {
        super.showData(data)
        Glide.with(context).asGif().load(data.backgroundURL).centerCrop().into(viewBind.quoteBack)
        onFindStyle.invoke(data)
    }

    override fun initView() {}
}