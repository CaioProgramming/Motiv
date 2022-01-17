package com.creat.motiv.profile.cover

import android.content.Context
import android.view.View
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.animations.fadeIn
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.Cover
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.view.BaseAlert

class CoverPickerDialog(context: Context, private val onCoverPick: (Cover) -> Unit) : BaseAlert(
    context, R.layout.profilepicselect_,
    DialogStyles.BOTTOM_NO_BORDER
) {

    override fun View.configure() {
        ProfilepicselectBinding.bind(this).run {

            coversGifMark.slideInBottom()
        }
    }


}