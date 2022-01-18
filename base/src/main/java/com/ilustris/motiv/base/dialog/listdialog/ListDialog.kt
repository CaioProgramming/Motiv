package com.ilustris.motiv.base.dialog.listdialog

import android.content.Context
import android.view.View
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.ListDialogLayoutBinding
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.view.BaseAlert


class ListDialog(
    context: Context,
    private val dialogItems: dialogItems,
    private val onSelectItem: (Int, DialogData) -> Unit,
    dialogStyle: DialogStyles
) : BaseAlert(context, R.layout.list_dialog_layout, dialogStyle) {

    override fun View.configure() {
        ListDialogLayoutBinding.bind(this).run {
            listRecyclerView.run {
                adapter = ListDialogAdapter(dialogItems) {
                    onSelectItem.invoke(it, dialogItems[it])
                    dialog.dismiss()
                }
            }
        }

    }

}