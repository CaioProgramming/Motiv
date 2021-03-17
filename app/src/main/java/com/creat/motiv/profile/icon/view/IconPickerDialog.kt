package com.creat.motiv.profile.icon.view

import android.app.Activity
import android.content.Context
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.motiv.base.beans.Pics
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles

class IconPickerDialog(context: Context, private val onSelectPic: (Pics) -> Unit) : BaseAlert<ProfilepicselectBinding>(context, R.layout.profilepicselect_,
        DialogStyles.BOTTOM_NO_BORDER) {


    override fun ProfilepicselectBinding.configure() {
        ProfilePicSelectBinder(this) {
            onSelectPic.invoke(it)
            dialog.dismiss()
        }.initView()
    }


}