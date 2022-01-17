package com.ilustris.motiv.base.dialog

import android.content.Context
import android.view.View
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.DefaultDialogBinding
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.view.BaseAlert

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

}