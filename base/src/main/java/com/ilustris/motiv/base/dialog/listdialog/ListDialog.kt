package com.ilustris.motiv.base.dialog.listdialog

import android.content.Context
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.ListDialogLayoutBinding
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles


class ListDialog(context: Context, private val dialogItems: DialogItems, private val onSelectItem: (Int) -> Unit,
                 dialogStyle: DialogStyles) : BaseAlert<ListDialogLayoutBinding>(context, R.layout.list_dialog_layout, dialogStyle) {

    override fun ListDialogLayoutBinding.configure() {
        listRecyclerView.run {
            adapter = ListDialogAdapter(dialogItems) {
                onSelectItem.invoke(it)
                dialog.dismiss()
            }
        }
    }

}