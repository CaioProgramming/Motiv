package com.creat.motiv.quote.view

import android.content.Context
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ShareQuotePreviewBinding
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles
import java.io.File

class QuoteShareDialog(context: Context, val file: File) : BaseAlert<ShareQuotePreviewBinding>(context, R.layout.share_quote_preview, style = DialogStyles.BOTTOM_NO_BORDER) {

    override fun ShareQuotePreviewBinding.configure() {
        Glide.with(context).load(file).into(quoteImage)
    }
}