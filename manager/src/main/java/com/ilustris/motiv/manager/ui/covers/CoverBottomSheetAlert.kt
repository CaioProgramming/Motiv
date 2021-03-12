package com.ilustris.motiv.manager.ui.covers

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ilustris.motiv.base.databinding.DefaultBottomsheetBinding
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unblurView
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.GifBottomsheetBinding
import kotlinx.android.synthetic.main.gif_bottomsheet.*

class CoverBottomSheetAlert(
        val context: Context,
        val title: String? = null,
        private val message: String? = null,
        val gifUrl: String,
        val okClick: (() -> Unit)? = null,
        val cancelClick: (() -> Unit)? = null) :  DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    val dialog = BottomSheetDialog(context,R.style.Bottom_Dialog_No_Border)

    init {
        initialize()
    }

    private fun initialize() {
        setupDialog()
        val bottomsheetBinding: GifBottomsheetBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.gif_bottomsheet,null,false)
        bottomsheetBinding.run {
            Glide.with(context).asGif().load(gifUrl).into(picView.pic)
            title?.let {
                titleTextview.text = it
            }
            message?.let {
                messageTextView.text = it
            }
            okButton.setOnClickListener {
                okClick?.invoke()
                dialog.dismiss()
                unblurView(context)
            }
            cancelButton.setOnClickListener {
                cancelClick?.invoke()
                dialog.dismiss()
                unblurView(context)
            }
        }
        dialog.setContentView(bottomsheetBinding.root)
        dialog.show()
    }

    private fun setupDialog() {
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setCanceledOnTouchOutside(true)
            setOnShowListener(this@CoverBottomSheetAlert)
            setOnDismissListener(this@CoverBottomSheetAlert)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        unblurView(context)
    }


    override fun onShow(dialog: DialogInterface?) {
        blurView(context)
    }

}