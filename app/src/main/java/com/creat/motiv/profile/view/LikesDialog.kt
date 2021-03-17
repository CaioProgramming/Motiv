package com.creat.motiv.profile.view

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.quote.view.binder.LikesBinder
import com.creat.motiv.view.adapters.LikersRecyclerAdapter
import com.ilustris.motiv.base.dialog.BaseAlert
import com.ilustris.motiv.base.utils.DialogStyles

class LikesDialog(context: Activity, private val likeList: List<String>) :
        BaseAlert<ProfilepicselectBinding>(context, R.layout.profilepicselect_,
                DialogStyles.BOTTOM_NO_BORDER) {

    override fun ProfilepicselectBinding.configure() {
        dialogTitle.text = "Curtidas"
        picsrecycler.run {
            adapter = LikersRecyclerAdapter(likeList)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        if (likeList != null) LikesBinder(likeList, this).initView()

    }
}