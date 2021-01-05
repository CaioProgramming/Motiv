package com.creat.motiv.utilities

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.*
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.DefaultDialogBinding
import com.creat.motiv.databinding.DeleteAllAlertBinding
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.binders.DeleteAllBinder
import com.creat.motiv.view.binders.LikesBinder
import com.creat.motiv.view.binders.ProfilePicSelectBinder
import com.google.android.material.bottomsheet.BottomSheetDialog


class Alert(private val activity: Activity, val dialogStyle: DialogStyles = DialogStyles.DEFAULT_NO_BORDER) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private val myDialog = if (dialogStyle == DialogStyles.FULL_SCREEN || dialogStyle == DialogStyles.DEFAULT_NO_BORDER)
        Dialog(activity, dialogStyle.style)
    else
        BottomSheetDialog(activity, dialogStyle.style)

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


    fun picturePicker(profilePresenter: UserPresenter, admin: Boolean) {
        val profilepicselectBinding = DataBindingUtil.inflate<ProfilepicselectBinding>(LayoutInflater.from(activity), R.layout.profilepicselect_, null, false)
        ProfilePicSelectBinder(myDialog, activity, admin, profilepicselectBinding) {
            profilePresenter.changeProfilePic(it)
            myDialog.dismiss()
        }
        configureDialog(profilepicselectBinding.root)

    }

    fun showLikes(likes: List<String>) {
        val profilepicselectBinding = DataBindingUtil.inflate<ProfilepicselectBinding>(LayoutInflater.from(activity), R.layout.profilepicselect_, null, false)
        LikesBinder(myDialog, likes, activity, profilepicselectBinding)
        configureDialog(profilepicselectBinding.root)
    }

    fun deleteAllDialog() {
        val deleteAllAlertBinding = DataBindingUtil.inflate<DeleteAllAlertBinding>(LayoutInflater.from(activity), R.layout.delete_all_alert, null, false)
        DeleteAllBinder(myDialog, activity, deleteAllAlertBinding)
        configureDialog(deleteAllAlertBinding.root, false)
    }

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
        unblurView(activity)
    }



}

