package com.creat.motiv.presenter

import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.Beans.Pics
import com.creat.motiv.view.BaseView

class PicsPresenter(override val view: BaseView<Pics>) : BasePresenter<Pics>() {
    override val model: BaseModel<Pics>
        get() = TODO("Not yet implemented")

    override fun onError() {
        TODO("Not yet implemented")
    }
}