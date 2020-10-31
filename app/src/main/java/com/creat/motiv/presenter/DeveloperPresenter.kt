package com.creat.motiv.presenter

import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.DeveloperModel
import com.creat.motiv.model.beans.Developer
import com.creat.motiv.view.BaseView

class DeveloperPresenter(override val view: BaseView<Developer>) : BasePresenter<Developer>() {
    override val model: BaseModel<Developer> = DeveloperModel(this)

}