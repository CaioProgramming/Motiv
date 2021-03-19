package com.ilustris.motiv.base.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.AnimationDrawable
import android.view.*
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.DefaultBottomsheetBinding
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView

class BottomSheetAlert(context: Context,
                       private val title: String? = null,
                       private val message: String? = null,
                       private val okClick: (() -> Unit)? = null,
                       private val cancelClick: (() -> Unit)? = null) :
        BaseAlert<DefaultBottomsheetBinding>(context, R.layout.default_bottomsheet, DialogStyles.BOTTOM_NO_BORDER) {


    override fun onShow(p0: DialogInterface?) {
        blurView(context)
    }

    override fun onDismiss(p0: DialogInterface?) {
        unBlurView(context)
    }

    override fun DefaultBottomsheetBinding.configure() {
        title?.let {
            titleTextview.text = it
        }
        message?.let {
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
        val animationDrawable = topAnimation.background as AnimationDrawable
        animationDrawable.run {
            setEnterFadeDuration(1000)
            setExitFadeDuration(2500)
            start()
        }
    }
}