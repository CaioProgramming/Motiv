package com.creat.motiv.profile.presenter

import com.creat.motiv.profile.model.CoversModel
import com.creat.motiv.profile.model.beans.Cover
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class CoversPresenter(override val view: BaseView<Cover>) : BasePresenter<Cover>() {
    override val model = CoversModel(this)
}