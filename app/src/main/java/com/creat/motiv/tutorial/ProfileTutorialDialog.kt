package com.creat.motiv.tutorial

import android.content.Context
import android.content.DialogInterface
import com.creat.motiv.R
import com.creat.motiv.databinding.HomeTutorialDialogLayoutBinding
import com.creat.motiv.databinding.ProfileTutorialDialogLayoutBinding
import com.creat.motiv.utilities.HOME_TUTORIAL
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.utilities.PROFILE_TUTORIAL
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles

class ProfileTutorialDialog(context: Context) : BaseAlert<ProfileTutorialDialogLayoutBinding>(context, R.layout.profile_tutorial_dialog_layout, DialogStyles.BOTTOM_NO_BORDER) {

    override fun ProfileTutorialDialogLayoutBinding.configure() {
        buttonContinue.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onShow(p0: DialogInterface?) {
        super.onShow(p0)
        MotivPreferences(context).updateTutorial(PROFILE_TUTORIAL)
    }

}