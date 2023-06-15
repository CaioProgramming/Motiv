package com.creat.motiv.features.profile.alerts

import android.app.Activity
import android.view.View
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.ui.alert.BaseAlert
import com.ilustris.ui.alert.DialogStyles

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