package com.creat.motiv.view.fragments

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.UPLOAD_ICON
import com.creat.motiv.utilities.blurView
import com.creat.motiv.utilities.unblurView
import com.creat.motiv.view.binders.ProfilePicSelectBinder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.opensooq.supernova.gligar.GligarPicker

class SelectIconFragment(val admin: Boolean = false, val profilePresenter: UserPresenter) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener, DialogFragment() {

    var profilePicSelectBinder: ProfilePicSelectBinder? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (context == null) return super.onCreateDialog(savedInstanceState)
        val profilepicselectBinding = DataBindingUtil.inflate<ProfilepicselectBinding>(LayoutInflater.from(context), R.layout.profilepicselect_, null, false)
        profilePicSelectBinder = ProfilePicSelectBinder(this, admin, profilepicselectBinding) {
            profilePresenter.changeProfilePic(it)
        }
        val dialog = BottomSheetDialog(requireContext())
        dialog.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setCanceledOnTouchOutside(true)
            setOnShowListener(this@SelectIconFragment)
            setOnDismissListener(this@SelectIconFragment)
            setContentView(profilepicselectBinding.root)
        }
        return dialog

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            UPLOAD_ICON -> {
                val imagesList = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)// return list of selected images paths.
                if (!imagesList.isNullOrEmpty()) {
                    profilePicSelectBinder?.uploadIcon(imagesList[0])
                }
            }
        }
    }

    override fun onShow(p0: DialogInterface?) {
        context?.let { blurView(it) }
    }

    override fun onDismiss(dialog: DialogInterface) {
        context?.let { unblurView(it) }
    }


}