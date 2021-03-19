package com.ilustris.motiv.base.model

import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.model.CoversModel
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class CoversPresenter(override val view: BaseView<Cover>) : BasePresenter<Cover>() {
    override val model = CoversModel(this)
}