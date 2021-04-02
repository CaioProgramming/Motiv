package com.ilustris.motiv.manager.ui.styles

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.beans.NEW_STYLE_ID
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.presenter.QuoteStylePresenter
import com.ilustris.motiv.manager.databinding.StylesRecyclerBinding
import com.silent.ilustriscore.core.view.BaseView

class StyleRecyclerBinder(override val viewBind: StylesRecyclerBinding) : BaseView<Style>() {
    override val presenter = QuoteStylePresenter(this)

    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<Style>) {
        super.showListData(list)
        val styles = ArrayList(list)
        styles.add(0, Style.newStyle)
        viewBind.stylesRecycler.run {
            adapter = StylePreviewAdapter(styles.toList(), false) { position ->
                val style = styles[position]
                if (style.id != NEW_STYLE_ID) {
                    BottomSheetAlert(context, "Tem certeza?", "Ao remover esse estilo não será possível recuperá-lo", {
                        presenter.deleteData(style)
                    }).buildDialog()

                } else {
                    val i = Intent(context, NewStyleActivity::class.java)
                    context.startActivity(i)
                }
            }
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        }
    }

}