package com.creat.motiv.utils

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.DefaultDialogBinding
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.view.binders.ProfilePicSelectBinder
import com.github.mmin18.widget.RealtimeBlurView
import com.pd.chocobar.ChocoBar

enum class DIALOG_STYLES(val style: Int) {
    DEFAULT_NO_BORDER(R.style.Dialog_No_Border),
    BOTTOM_NO_BORDER(R.style.Bottom_Dialog_No_Border)
}

class Alert(private val activity: Activity, private val dialogStyle: DIALOG_STYLES = DIALOG_STYLES.DEFAULT_NO_BORDER) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private val blur: RealtimeBlurView? = activity.findViewById(R.id.rootblur)

    fun showAlert(message: String,
                  buttonMessage: String? = "Ok",
                  cancelMessage: String? = "Cancelar",
                  icon: Int, okClick: (() -> Unit)? = null,
                  cancelClick: (() -> Unit)? = null) {
        val myDialog = Dialog(activity, dialogStyle.style)
        val dialogbind = DataBindingUtil.inflate<DefaultDialogBinding>(LayoutInflater.from(activity), R.layout.default_dialog, null, false)
        configureDialog(myDialog, dialogbind.root)
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

    }

    private fun configureDialog(myDialog: Dialog, view: View) {
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(view)
        myDialog.show()
    }


    fun snackmessage(backcolor: Int?, message: String) {

        /*val snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
        )
        snackbar.config(activity)
        if (backcolor != null) snackbar.setBackgroundTintList(ColorStateList.valueOf(activity.resources.getColor(backcolor)))
        else snackbar.setBackgroundTintList(ColorStateList.valueOf(R.attr.itemTextColor))
        snackbar.show()*/
        if (backcolor != null) {
            ChocoBar.builder()
                    .setText(message)
                    .setMaxLines(4)
                    .setBackgroundColor(activity.resources.getColor(backcolor))
                    .setActivity(activity)
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .build()
                    .show()
        } else {
            ChocoBar.builder()
                    .setText(message)
                    .setMaxLines(4)
                    .setActivity(activity)
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .build()
                    .show()
        }

        /* if (color == 0)  {
             snackbar.setBackgroundTint(Tools.inversebackcolor(activity))
         }else{
             snackbar.setTextColor(Tools.inversetextcolor(activity))
         }
         snackbar.config(activity)
         snackbar.show()*/


        /* var flashbar = Flashbar.Builder(activity)
                 .gravity(Flashbar.Gravity.BOTTOM)
                 .message(message)
                 .duration(Flashbar.DURATION_LONG)
                 .backgroundColor(Tools.inversebackcolor(activity))
                 .messageColor(Tools.inversetextcolor(activity))
                 .messageTypeface(Typeface.DEFAULT_BOLD)
                 .icon(icon)
                 .build()
         flashbar.show()*/
    }


    override fun onShow(dialogInterface: DialogInterface) {
        if (blur != null) {
            Tools.fadeIn(this.blur, 1500).subscribe()
        }

    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        if (blur != null) {
            Tools.fadeOut(this.blur, 1500).subscribe()
        }
    }



}

