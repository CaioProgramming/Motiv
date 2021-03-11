package com.creat.motiv.profile.presenter

import com.creat.motiv.model.PicsModel
import com.creat.motiv.profile.model.beans.Pics
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class PicsPresenter(override val view: BaseView<Pics>) : BasePresenter<Pics>() {
    override val model = PicsModel(this)


}