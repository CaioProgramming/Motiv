package com.creat.motiv.features.profile.alerts

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.adapters.RecyclerCoverAdapter
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unBlurView
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.view.BaseAlert

class CoverPickerDialog(
    context: Context,
    private val covers: List<Cover>,
    private val onCoverPick: (Cover) -> Unit
) : BaseAlert(
    context, R.layout.profilepicselect_,
    DialogStyles.BOTTOM_NO_BORDER
) {

    override fun View.configure() {
        ProfilepicselectBinding.bind(this).run {
            dialogTitle.text = "Selecionar capa de perfil"
            picsrecycler.adapter = RecyclerCoverAdapter(ArrayList(covers)) {
                onCoverPick(it)
                dialog.dismiss()
            }
            picsrecycler.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            coversGifMark.slideInBottom()
        }
    }

    override fun onShow(p0: DialogInterface?) {
        blurView(context)
    }

    override fun onDismiss(p0: DialogInterface?) {
        unBlurView(context)
    }

}