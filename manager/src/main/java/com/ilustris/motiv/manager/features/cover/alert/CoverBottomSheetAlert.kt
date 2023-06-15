package com.ilustris.motiv.manager.features.cover.alert

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.utils.unBlurView
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.GifBottomsheetBinding
import com.ilustris.ui.alert.BaseAlert
import com.ilustris.ui.alert.DialogStyles

class CoverBottomSheetAlert(
    context: Context,
    val title: String? = null,
    private val message: String? = null,
    val gifUrl: String,
    private val okClick: (() -> Unit)? = null,
    private val cancelClick: (() -> Unit)? = null
) : BaseAlert(context, R.layout.gif_bottomsheet, style = DialogStyles.BOTTOM_NO_BORDER) {

    override fun View.configure() {
        GifBottomsheetBinding.bind(this).run {
            Glide.with(context).asGif().load(gifUrl).into(picView.image)
            title?.let {
                titleTextview.text = it
            }
            message?.let {
                messageTextView.text = it
            }
            okButton.setOnClickListener {
                okClick?.invoke()
                dialog.dismiss()
                unBlurView(context)
            }
            cancelButton.setOnClickListener {
                cancelClick?.invoke()
                dialog.dismiss()
                unBlurView(context)
            }
        }

    }
}

