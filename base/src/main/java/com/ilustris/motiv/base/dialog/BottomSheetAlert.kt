package com.ilustris.motiv.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.DefaultBottomsheetBinding
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.view.BaseAlert

class BottomSheetAlert(
    context: Context,
    private val title: String? = null,
    private val message: String? = null,
    private val okClick: (() -> Unit)? = null,
    private val cancelClick: (() -> Unit)? = null
) :
    BaseAlert(context, R.layout.default_bottomsheet, DialogStyles.BOTTOM_NO_BORDER) {


    override fun onShow(p0: DialogInterface?) {
        blurView(context)
    }

    override fun onDismiss(p0: DialogInterface?) {
        unBlurView(context)
    }

    override fun View.configure() {
        DefaultBottomsheetBinding.bind(this).run {
            title.let {
                titleTextview.text = it
            }
            message.let {
                messageTextView.text = it
            }
            buttonsView.okButton.setOnClickListener {
                okClick?.invoke()
                dialog.dismiss()
            }
            buttonsView.cancelButton.setOnClickListener {
                cancelClick?.invoke()
                dialog.dismiss()
            }
        }

    }
}