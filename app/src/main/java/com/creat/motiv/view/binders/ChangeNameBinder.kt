package com.creat.motiv.view.binders

import android.content.Context
import com.creat.motiv.databinding.ChangeNameViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.view.BaseView

class ChangeNameBinder(override val context: Context, override val viewBind: ChangeNameViewBinding) : BaseView<User>() {


    init {
        initView()
    }

    override fun presenter() = UserPresenter(this)

    override fun onLoading() {
        viewBind.nameProgress.fadeIn()
    }

    override fun onLoadFinish() {
        viewBind.nameProgress.fadeOut()
    }

    override fun initView() {
        viewBind.run {
            saveName.setOnClickListener {
                presenter().changeUserName(viewBind.changename.text.toString())
            }
        }
    }


}