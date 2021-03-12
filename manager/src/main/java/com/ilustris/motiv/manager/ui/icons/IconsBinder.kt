package com.ilustris.motiv.manager.ui.icons

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

class IconsBinder(override val viewBind: FragmentIconsBinding, val fragmentManager: FragmentManager) : BaseView<Pics>() {
    override val presenter = PicsPresenter(this)

    override fun initView() {
        presenter.loadData()
    }

    override fun showListData(list: List<Pics>) {
        super.showListData(list)
        val pictures = ArrayList(list)
        pictures.add(0, Pics.addPic())
        viewBind.managerRecycler.run {
            adapter = RecyclerPicAdapter(pictures, {
                BottomSheetAlert.buildAlert(fragmentManager, "Não gostou?", "Caso queira apagar esse ícone basta confirmar", { presenter.deleteData(it) })
            }, {
                AddIconsDialog.buildNewIconsDialog(fragmentManager) { presenter.savePics(it) }
            })
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        }
    }


}