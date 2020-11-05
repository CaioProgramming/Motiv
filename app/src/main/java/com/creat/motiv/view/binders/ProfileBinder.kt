package com.creat.motiv.view.binders

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.gone
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.QuotesProfileAdapter

class ProfileBinder(override val context: Context,
                    override val viewBind: FragmentProfileBinding,
                    val user: User? = null, val fragmentManager: FragmentManager) : BaseView<User>() {

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
            ProfileTopBinder(uid = data.uid, user = data, viewBind = viewBind.profileTopView, context = context, fragmentManager = fragmentManager)
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