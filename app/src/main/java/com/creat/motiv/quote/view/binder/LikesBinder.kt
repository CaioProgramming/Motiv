package com.creat.motiv.quote.view.binder

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.ilustris.motiv.base.presenter.UserPresenter
import com.creat.motiv.view.adapters.LikersRecyclerAdapter
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.animations.slideUp
import com.ilustris.motiv.base.beans.User
import com.silent.ilustriscore.core.view.BaseView

class LikesBinder(val dialog: Dialog,
                  val likeList: List<String>,
                  override val context: Context,
                  override val viewBind: ProfilepicselectBinding) : BaseView<User>() {
    override val presenter = UserPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        viewBind.dialogTitle.text = "Curtidas"
        viewBind.closeButton.setOnClickListener {
            dialog.dismiss()
        }
        viewBind.picsrecycler.run {
            adapter = LikersRecyclerAdapter(likeList)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
    }

    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.fadeOut()
        viewBind.picsrecycler.slideUp()
    }

}