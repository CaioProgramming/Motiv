package com.creat.motiv.view.binders

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utils.fadeIn
import com.creat.motiv.utils.gone
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.QuotesProfileAdapter

class ProfileBinder(override val context: Context,
                    override val viewBind: FragmentProfileBinding,
                    val user: User? = null) : BaseView<User>() {

    override fun presenter() = UserPresenter(this)

    override fun onLoading() {
        viewBind.profileRecycler.gone()
        viewBind.profileTopView.profileContainer.gone()
        viewBind.loading.fadeIn()
    }

    override fun onLoadFinish() {
        viewBind.loading.gone()
    }

    override fun initView() {
        if (user == null) {
            presenter().getUser()
        } else {
            showData(user)
        }
    }

    override fun showData(data: User) {
        onLoadFinish()
        viewBind.run {
            toolbar.title = data.name
            ProfileTopBinder(data.uid, data, viewBind.profileTopView, context)
            profileRecycler.run {
                layoutManager = LinearLayoutManager(context, VERTICAL, false)
                adapter = QuotesProfileAdapter(context, data.uid)
            }
        }
    }

    init {
        initView()
    }
}