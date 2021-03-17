package com.ilustris.motiv.manager.ui.covers

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.*
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.GifBottomsheetBinding
import kotlinx.android.synthetic.main.gif_bottomsheet.*

class CoverBottomSheetAlert(
        context: Context,
        val title: String? = null,
        private val message: String? = null,
        val gifUrl: String,
        private val okClick: (() -> Unit)? = null,
        private val cancelClick: (() -> Unit)? = null) : BaseAlert<GifBottomsheetBinding>(context, R.layout.gif_bottomsheet, style = DialogStyles.BOTTOM_NO_BORDER) {

    override fun GifBottomsheetBinding.configure() {
        Glide.with(context).asGif().load(gifUrl).into(picView.pic)
        title?.let {
            titleTextview.text = it
        }
        message?.let {
            messageTextView.text = it
        }
        this.dialogButtons.okButton.setOnClickListener {
            okClick?.invoke()
            dialog.dismiss()
            unBlurView(context)
        }
        this.dialogButtons.cancelButton.setOnClickListener {
            cancelClick?.invoke()
            dialog.dismiss()
            unBlurView(context)
        }
    }
}

