package com.ilustris.motiv.manager.ui.icons

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.adapters.RecyclerPicAdapter
import com.ilustris.motiv.base.beans.Pics
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.presenter.PicsPresenter
import com.ilustris.motiv.manager.databinding.FragmentIconsBinding
import com.silent.ilustriscore.core.view.BaseView

class IconsBinder(override val viewBind: FragmentIconsBinding, private val fragmentManager: FragmentManager) : BaseView<Pics>() {
    override val presenter = PicsPresenter(this)

    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<Pics>) {
        super.showListData(list)
        val pictures = ArrayList(list)
        pictures.add(0, Pics.addPic())
        viewBind.managerRecycler.run {
            adapter = RecyclerPicAdapter(pictures, { pic ->
                (context as Activity?)?.let {
                    BottomSheetAlert(it, "Não gostou?", "Caso queira apagar esse ícone basta confirmar", {
                        presenter.deleteData(pic)
                    }).buildDialog()

                }
            }, {
                AddIconsDialog.buildNewIconsDialog(fragmentManager) { presenter.savePics(it) }
            })
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        }
    }


}