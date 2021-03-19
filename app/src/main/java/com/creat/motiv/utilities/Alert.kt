package com.creat.motiv.utilities

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView


class Alert(private val activity: Activity, val dialogStyle: DialogStyles = DialogStyles.DEFAULT_NO_BORDER) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private val myDialog = if (dialogStyle == DialogStyles.FULL_SCREEN || dialogStyle == DialogStyles.DEFAULT_NO_BORDER)
        Dialog(activity, dialogStyle.resource)
    else
        BottomSheetDialog(activity, dialogStyle.resource)




    private fun configureDialog(view: View, showImediatly: Boolean = true) {
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(view)
        if (showImediatly) {
            myDialog.show()
        }
        if (dialogStyle == DialogStyles.FULL_SCREEN) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            myDialog.window?.setLayout(width, height)
        }
    }





    override fun onShow(dialogInterface: DialogInterface) {
        blurView(activity)
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        unBlurView(activity)
    }



}

