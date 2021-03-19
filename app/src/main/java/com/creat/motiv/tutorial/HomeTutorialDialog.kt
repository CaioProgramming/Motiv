package com.creat.motiv.tutorial

import android.content.Context
import android.content.DialogInterface
import com.creat.motiv.R
import com.creat.motiv.databinding.HomeTutorialDialogLayoutBinding
import com.creat.motiv.utilities.HOME_TUTORIAL
import com.creat.motiv.utilities.MotivPreferences
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles

class HomeTutorialDialog(context: Context) : BaseAlert<HomeTutorialDialogLayoutBinding>(context, R.layout.home_tutorial_dialog_layout, DialogStyles.BOTTOM_NO_BORDER) {

    override fun HomeTutorialDialogLayoutBinding.configure() {
        buttonContinue.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onShow(p0: DialogInterface?) {
        super.onShow(p0)
        MotivPreferences(context).updateTutorial(HOME_TUTORIAL)
    }

}