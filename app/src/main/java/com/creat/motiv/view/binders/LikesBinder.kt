package com.creat.motiv.view.binders

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.utilities.slideInBottom
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.LikersRecyclerAdapter

class LikesBinder(val dialog: Dialog,
                  val likeList: List<String>,
                  override val context: Context,
                  override val viewBind: ProfilepicselectBinding) : BaseView<User>() {
    override fun presenter() = UserPresenter(this)

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
        viewBind.picsrecycler.slideInBottom()
    }

}