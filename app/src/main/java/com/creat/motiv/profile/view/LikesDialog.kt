package com.creat.motiv.profile.view

import android.app.Activity
import android.view.View
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.view.BaseAlert

class LikesDialog(context: Activity, private val likeList: List<String>) :
    BaseAlert(
        context, R.layout.profilepicselect_,
        DialogStyles.BOTTOM_NO_BORDER
    ) {


    override fun View.configure() {
        ProfilepicselectBinding.bind(this).run {
            dialogTitle.text = "Curtidas"

        }
    }
}