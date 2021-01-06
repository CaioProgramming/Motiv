package com.creat.motiv.presenter

import com.creat.motiv.model.DeveloperModel
import com.creat.motiv.model.beans.Developer
import com.ilustriscore.core.base.BaseModel
import com.ilustriscore.core.base.BasePresenter
import com.ilustriscore.core.base.BaseView

class DeveloperPresenter(override val view: BaseView<Developer>) : BasePresenter<Developer>() {
    override val model: BaseModel<Developer> = DeveloperModel(this)

}