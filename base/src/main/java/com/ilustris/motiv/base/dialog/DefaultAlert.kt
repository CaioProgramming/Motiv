package com.ilustris.motiv.base.dialog

import android.app.Activity
import android.content.Context
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.DefaultDialogBinding
import com.ilustris.motiv.base.utils.DialogStyles

class DefaultAlert(context: Context, var title: String? = null, var message: String? = null, var icon: Int? = null,
                   private val okClick: (() -> Unit)? = null,
                   private val cancelClick: (() -> Unit)? = null) : BaseAlert<DefaultDialogBinding>(context, R.layout.default_dialog, DialogStyles.DEFAULT_NO_BORDER) {


    override fun DefaultDialogBinding.configure() {
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