package com.creat.motiv.radio

import com.ilustris.motiv.base.beans.Radio
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.view.BaseView

class RadioPresenter(override val view: BaseView<Radio>) : BasePresenter<Radio>() {
    override val model = RadioModel(this)
}