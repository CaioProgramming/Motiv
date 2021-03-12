package com.ilustris.motiv.manager.ui.styles

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.quote.beans.NEW_STYLE_ID
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
            adapter = StylePreviewAdapter(styles.toList(), false) {
                if (it.id != NEW_STYLE_ID) {
                    BottomSheetAlert(context, "Tem certeza?", "Ao remover esse estilo não será possível recuperá-lo", {
                        presenter.deleteData(it)
                    })
                } else {
                    context.startActivity(Intent(context, NewStyleActivity::class.java))
                }
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

}