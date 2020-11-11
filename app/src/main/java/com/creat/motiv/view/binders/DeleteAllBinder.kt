package com.creat.motiv.view.binders

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.creat.motiv.databinding.DeleteAllAlertBinding
import com.creat.motiv.model.beans.Quote
import com.creat.motiv.presenter.QuotePresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.view.BaseView

class DeleteAllBinder(val dialog: Dialog, override val context: Context, override val viewBind: DeleteAllAlertBinding) : BaseView<Quote>() {


    init {
        initView()
    }

    override fun presenter() = QuotePresenter(this)


    override fun initView() {
        presenter().loadData()
    }

    override fun showListData(list: List<Quote>) {
        viewBind.run {
            mainView.fadeIn()
            messageTextView.text = "Você está prestes a deletar ${list.size} frases! Tem certeza disso? Não quero que você me culpe depois..."
            cancelDelete.setOnClickListener {
                dialog.dismiss()
            }

            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            deleteConfirm.setOnClickListener {
                presenter().deleteAll(list)
            }
            if (!(context as Activity).isFinishing) {
                dialog.show()
            }
        }
    }
}