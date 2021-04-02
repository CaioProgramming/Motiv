package com.creat.motiv.quote.view.binder

import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.databinding.StyleLayoutViewBinding
import com.ilustris.motiv.base.utils.loadGif
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.view.BaseView

class QuoteStyleBinder(override val viewBind: StyleLayoutViewBinding, private val onFindStyle: (Style) -> Unit) : BaseView<Style>() {
    override val presenter = QuoteStylePresenter(this)

    fun getStyle(styleID: String) {
        presenter.loadSingleData(styleID)
    }

    override fun showData(data: Style) {
        super.showData(data)
        viewBind.quoteBack.loadGif(data.backgroundURL)
        onFindStyle.invoke(data)
    }

    override fun error(dataException: DataException) {
        super.error(dataException)
        when (dataException.code) {
            ErrorType.NOT_FOUND -> showData(Style.defaultStyle)
            ErrorType.UNKNOWN -> showData(Style.defaultStyle)
            else -> showSnackBar(context, "Ocorreu um erro desconhecido", rootView = viewBind.root)
        }
    }


    override fun initView() {}
}