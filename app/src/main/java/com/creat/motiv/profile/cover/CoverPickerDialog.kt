package com.creat.motiv.profile.cover

import android.app.Activity
import android.content.Context
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles

class CoverPickerDialog(context: Context, private val onCoverPick: (Cover) -> Unit) : BaseAlert<ProfilepicselectBinding>(context, R.layout.profilepicselect_,
        DialogStyles.BOTTOM_NO_BORDER) {

    override fun ProfilepicselectBinding.configure() {
        CoversBinder(this) {
            onCoverPick.invoke(it)
            dialog.dismiss()
        }
    }


}