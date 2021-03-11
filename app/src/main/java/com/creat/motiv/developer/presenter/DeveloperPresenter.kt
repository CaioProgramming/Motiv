package com.creat.motiv.developer.presenter

import com.creat.motiv.developer.model.DeveloperModel
import com.ilustris.motiv.base.beans.Developer
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class DeveloperPresenter(override val view: BaseView<Developer>) : BasePresenter<Developer>() {
    override val model: BaseModel<Developer> = DeveloperModel(this)

}