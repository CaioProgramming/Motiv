package com.creat.motiv.tutorial

import android.content.Context
import android.content.DialogInterface
import com.creat.motiv.R
import com.creat.motiv.databinding.HomeTutorialDialogLayoutBinding
import com.creat.motiv.databinding.NewPostTutorialDialogLayoutBinding
import com.creat.motiv.utilities.HOME_TUTORIAL
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.utilities.NEW_QUOTE_TUTORIAL
import com.creat.motiv.utilities.PROFILE_TUTORIAL
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles

class NewPostTutorialDialog(context: Context) : BaseAlert<NewPostTutorialDialogLayoutBinding>(context, R.layout.new_post_tutorial_dialog_layout, DialogStyles.BOTTOM_NO_BORDER) {

    override fun NewPostTutorialDialogLayoutBinding.configure() {
        buttonContinue.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onShow(p0: DialogInterface?) {
        super.onShow(p0)
        MotivPreferences(context).updateTutorial(NEW_QUOTE_TUTORIAL)
    }

}