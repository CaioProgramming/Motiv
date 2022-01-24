package com.creat.motiv.profile.icon.view

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.motiv.base.adapters.RecyclerIconAdapter
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.view.BaseAlert

class IconPickerDialog(
    context: Context,
    private val icons: List<Icon>,
    private val onSelectPic: (Icon) -> Unit
) :
    BaseAlert(
        context, R.layout.profilepicselect_,
        DialogStyles.BOTTOM_NO_BORDER
    ) {

    override fun View.configure() {
        ProfilepicselectBinding.bind(this).run {
            dialogTitle.text = "Selecionar ícone de perfil"
            picsrecycler.adapter = RecyclerIconAdapter(ArrayList(icons)) {
                onSelectPic(it)
                dialog.dismiss()
            }
        }
    }

    override fun onShow(p0: DialogInterface?) {
        blurView(context)
    }

    override fun onDismiss(p0: DialogInterface?) {
        unBlurView(context)
    }


}