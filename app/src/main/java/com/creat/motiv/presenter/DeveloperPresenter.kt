package com.creat.motiv.presenter

import com.creat.motiv.model.DeveloperModel
import com.creat.motiv.model.beans.Developer
import com.silent.ilustriscore.core.model.BaseModel
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class DeveloperPresenter(override val view: BaseView<Developer>) : BasePresenter<Developer>() {
    override val model: BaseModel<Developer> = DeveloperModel(this)

}