package com.ilustris.motiv.base.presenter

import com.ilustris.motiv.base.model.PicsModel
import com.ilustris.motiv.base.beans.Pics
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class PicsPresenter(override val view: BaseView<Pics>) : BasePresenter<Pics>() {
    override val model = PicsModel(this)

    fun savePics(pic: ArrayList<Pics>) {
        pic.forEach {
            model.addData(it)
        }
    }


}