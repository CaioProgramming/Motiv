package com.ilustris.motiv.base.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresFeature
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView

abstract class BaseAlert<V>(var context: Context,
                            val layout: Int,
                            val style: DialogStyles = DialogStyles.DEFAULT_NO_BORDER) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener where V : ViewDataBinding {


    val dialog = if (style == DialogStyles.BOTTOM_NO_BORDER) BottomSheetDialog(context) else Dialog(
        context,
        style.resource
    )
    val view: View by lazy {
        LayoutInflater.from(context).inflate(layout, null, false).rootView
    }


    abstract fun V.configure()


    fun buildDialog() {
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            //window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setCanceledOnTouchOutside(true)
            setOnShowListener(this@BaseAlert)
            setOnDismissListener(this@BaseAlert)
            setContentView(view)
            DataBindingUtil.bind<V>(view)?.run {
                configure()
            }
            dialog.show()
        }
    }

    override fun onShow(p0: DialogInterface?) {
        blurView(context)
    }

    override fun onDismiss(p0: DialogInterface?) {
        unBlurView(context)
    }

}