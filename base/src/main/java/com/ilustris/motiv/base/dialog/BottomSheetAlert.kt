package com.ilustris.motiv.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.DefaultBottomsheetBinding
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unblurView

class BottomSheetAlert(val context: Context,
                       val title: String? = null,
                       val message: String? = null,
                       private val okClick: (() -> Unit)? = null,
                       private val cancelClick: (() -> Unit)? = null) :  DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    val dialog = BottomSheetDialog(context,R.style.Bottom_Dialog_No_Border)


    init {
        initialize()
    }

    private fun initialize() {
       val defaultBottomsheetBinding : DefaultBottomsheetBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.default_bottomsheet,null,false)
        setupDialog()
        defaultBottomsheetBinding.run {
            title?.let {
                titleTextview.text = it
            }
            message?.let {
                messageTextView.text = it
            }
            okButton.setOnClickListener {
                okClick?.invoke()
                dialog.dismiss()
            }
            cancelButton.setOnClickListener {
                cancelClick?.invoke()
                dialog.dismiss()
            }
        }
        dialog.setContentView(defaultBottomsheetBinding.root)
        dialog.show()
    }


    fun setupDialog() {
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setCanceledOnTouchOutside(true)
            setOnShowListener(this@BottomSheetAlert)
            setOnDismissListener(this@BottomSheetAlert)
        }
    }

    override fun onShow(p0: DialogInterface?) {
        blurView(context)
    }

    override fun onDismiss(p0: DialogInterface?) {
        unblurView(context)
    }

}