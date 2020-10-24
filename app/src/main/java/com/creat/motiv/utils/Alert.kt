package com.creat.motiv.utils

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.DefaultDialogBinding
import com.creat.motiv.databinding.DeleteAllAlertBinding
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.binders.DeleteAllBinder
import com.creat.motiv.view.binders.ProfilePicSelectBinder
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.snackbar.Snackbar



class Alert(private val activity: Activity, dialogStyle: DialogStyles = DialogStyles.DEFAULT_NO_BORDER) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private val blur: RealtimeBlurView? = activity.findViewById(R.id.rootblur)
    private val myDialog = Dialog(activity, dialogStyle.style)

    fun showAlert(message: String,
                  buttonMessage: String? = "Ok",
                  cancelMessage: String? = "Cancelar",
                  icon: Int, okClick: (() -> Unit)? = null,
                  cancelClick: (() -> Unit)? = null) {
        val dialogbind = DataBindingUtil.inflate<DefaultDialogBinding>(LayoutInflater.from(activity), R.layout.default_dialog, null, false)
        configureDialog(dialogbind.root)
        dialogbind.run {
            messageTextView.text = message
            confirmButton.text = buttonMessage
            confirmButton.setOnClickListener {
                okClick?.invoke()
                myDialog.dismiss()
            }
            cancelButton.setOnClickListener {
                cancelClick?.invoke()
                myDialog.dismiss()
            }
            cancelButton.text = cancelMessage
            Glide.with(activity).load(icon).into(iconImageView)
        }

    }

    fun picturePicker(profilePresenter: UserPresenter) {
        val profilepicselectBinding = DataBindingUtil.inflate<ProfilepicselectBinding>(LayoutInflater.from(activity), R.layout.profilepicselect_, null, false)
        ProfilePicSelectBinder(activity, profilepicselectBinding) {
            profilePresenter.changeProfilePic(it)
        }
        configureDialog(profilepicselectBinding.root)

    }


    fun deleteAllDialog() {
        val deleteAllAlertBinding = DataBindingUtil.inflate<DeleteAllAlertBinding>(LayoutInflater.from(activity), R.layout.delete_all_alert, null, false)
        DeleteAllBinder(myDialog, activity, deleteAllAlertBinding)
        configureDialog(deleteAllAlertBinding.root)
    }

    private fun configureDialog(view: View) {
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(view)
        myDialog.show()
    }


    fun snackmessage(backcolor: Int = Color.BLACK, textColor: Int = Color.WHITE, message: String) {

        val contextView = activity.findViewById<View>(R.id.mainContainer)
        Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT)
                .setTextColor(textColor)
                .setBackgroundTint(backcolor)
                .show()


    }


    override fun onShow(dialogInterface: DialogInterface) {
        blurView(activity)
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        blurView(activity)
    }



}

