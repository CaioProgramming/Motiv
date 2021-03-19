package com.ilustris.motiv.manager.ui.icons

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.adapters.RecyclerPicAdapter
import com.ilustris.motiv.base.beans.Pics
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.presenter.PicsPresenter
import com.ilustris.motiv.manager.databinding.FragmentIconsBinding
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.utilities.showSnackBar
import com.silent.ilustriscore.core.view.BaseView

class IconsBinder(override val viewBind: FragmentIconsBinding, private val fragmentManager: FragmentManager) : BaseView<Pics>() {
    override val presenter = PicsPresenter(this)
    private val picAdapter = RecyclerPicAdapter(ArrayList(listOf(Pics.addPic())), { pic ->
        BottomSheetAlert(context, "Não gostou?", "Caso queira apagar esse ícone basta confirmar", {
            presenter.deleteData(pic)
        }).buildDialog()

    }, {
        AddIconsDialog.buildNewIconsDialog(fragmentManager) {
            presenter.savePics(it)
        }
    })

    override fun initView() {

        presenter.loadData()
    }

    override fun showListData(list: List<Pics>) {
        super.showListData(list)
        val pics = ArrayList(list)
        pics.add(0, Pics.addPic())
        viewBind.managerRecycler.run {
            adapter = RecyclerPicAdapter(pics, { pic ->
                (context as Activity?)?.let {
                    BottomSheetAlert(it, "Não gostou?", "Caso queira apagar esse ícone basta confirmar", {
                        presenter.deleteData(pic)
                    }).buildDialog()

                }
            }, {
                AddIconsDialog.buildNewIconsDialog(fragmentManager) {
                    presenter.savePics(it)
                }
            })
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        }

    }

    override fun error(dataException: DataException) {
        super.error(dataException)
        when (dataException.code) {
            ErrorType.SAVE -> showSnackBar(context = context, message = "Ocorreu um erro ao salvar um dos ícones", rootView = viewBind.root)
            ErrorType.UNKNOWN -> showSnackBar(context = context, message = "Ocorreu um erro ao salvar um dos ícones", rootView = viewBind.root)
            ErrorType.DISCONNECTED -> {
            }
            ErrorType.UPDATE -> {
            }
            ErrorType.DELETE -> showSnackBar(context = context, message = "Ocorreu um erro apagando o ícone", rootView = viewBind.root)
            ErrorType.NOT_FOUND -> {
            }
        }
    }

}