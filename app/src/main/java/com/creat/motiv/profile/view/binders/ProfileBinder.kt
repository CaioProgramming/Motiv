package com.creat.motiv.profile.view.binders

import android.content.Context
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.profile.icon.view.IconPickerDialog
import com.ilustris.motiv.base.presenter.UserPresenter
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.view.BaseView

class ProfileBinder(override val context: Context,
                    override val viewBind: FragmentProfileBinding,
                    val user: User? = null) : BaseView<User>() {

    override val presenter = UserPresenter(this)
    var quotesListBinder: QuotesListBinder = QuotesListBinder(viewBind.quotesView)

    init {
        initView()
    }

    override fun initView() {
        if (user == null) {
            presenter.user?.let {
                presenter.getUser()
            }
        } else {
            showData(user)
        }
    }

    override fun error(dataException: DataException) {
        super.error(dataException)
        if (dataException.code == ErrorType.NOT_FOUND) {
            presenter.saveUser()
        }
    }

    override fun showData(data: User) {
        onLoadFinish()
        viewBind.run {
            quotesListBinder.getUserQuotes(data.uid)
            quotesListBinder.viewBind.quotesrecyclerview.slideInBottom()
            if (data.picurl.isEmpty()) {
                DefaultAlert(context, "Atenção",
                        "Você não possui nenhum ícone de perfil, gostaria de adicionar agora?",
                        okClick = {
                            IconPickerDialog(context, onSelectPic = { presenter.changeProfilePic(it) }).buildDialog()
                        }).buildDialog()
            }
        }
    }

}