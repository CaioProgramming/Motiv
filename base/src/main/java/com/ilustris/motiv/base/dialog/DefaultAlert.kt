package com.ilustris.motiv.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.DefaultDialogBinding
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView
import com.ilustris.ui.alert.BaseAlert
import com.ilustris.ui.alert.DialogStyles

class DefaultAlert(
    context: Context, var title: String? = null, var message: String? = null, var icon: Int? = null,
    private val okClick: (() -> Unit)? = null,
    private val cancelClick: (() -> Unit)? = null
) : BaseAlert(
    context, R.layout.default_dialog,
    DialogStyles.DEFAULT_NO_BORDER
) {


    override fun View.configure() {
        DefaultDialogBinding.bind(this).run {
            titleTextView.text = title
            messageTextView.text = message
            icon?.let {
                iconImageView.setImageResource(it)
                iconImageView.fadeIn()
            }
            confirmButton.setOnClickListener {
                okClick?.invoke()
                dialog.dismiss()
            }
            cancelButton.setOnClickListener {
                cancelClick?.invoke()
                dialog.dismiss()
            }
        }

    }

    override fun onShow(p0: DialogInterface?) {
        blurView(context)
    }

    override fun onDismiss(p0: DialogInterface?) {
        unBlurView(context)
    }
}