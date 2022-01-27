package com.creat.motiv.features

import com.ilustris.motiv.base.beans.Radio
import com.ilustris.motiv.base.service.RadioService
import com.silent.ilustriscore.core.model.BaseViewModel

class MainViewModel : BaseViewModel<Radio>() {

    override val service = RadioService()
    var currentRadio: Radio? = null


}