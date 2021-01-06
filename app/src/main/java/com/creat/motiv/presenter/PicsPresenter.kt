package com.creat.motiv.presenter

import com.creat.motiv.model.PicsModel
import com.creat.motiv.model.beans.Pics
import com.ilustriscore.core.base.BasePresenter
import com.ilustriscore.core.base.BaseView

class PicsPresenter(override val view: BaseView<Pics>) : BasePresenter<Pics>() {
    override val model = PicsModel(this)


}