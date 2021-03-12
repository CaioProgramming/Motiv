package com.ilustris.motiv.manager.ui.styles

import androidx.databinding.ViewDataBinding
import com.creat.motiv.quote.beans.QuoteStyle
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.manager.databinding.StylesRecyclerBinding
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class StyleRecyclerBinder(override val viewBind: StylesRecyclerBinding) : BaseView<QuoteStyle>() {
    override val presenter = QuoteStylePresenter(this)

    override fun initView() {
       presenter.loadData()
    }

    override fun showListData(list: List<QuoteStyle>) {
        super.showListData(list)
        val styles = ArrayList(list)
        styles.add(0, QuoteStyle.newStyle)
        viewBind.stylesRecycler.run {
            adapter = StylePreviewAdapter(list,false) {
                BottomSheetAlert(context, "Tem certeza?", "Ao remover esse estilo não será possível recuperá-lo", {
                    presenter.deleteData(it)
                })
            }
        }
    }

}