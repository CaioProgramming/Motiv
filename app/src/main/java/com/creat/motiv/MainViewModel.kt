package com.creat.motiv

import android.app.Application
import com.ilustris.motiv.base.beans.Radio
import com.ilustris.motiv.base.service.RadioService
import com.silent.ilustriscore.core.model.BaseViewModel

class MainViewModel(application: Application) : BaseViewModel<Radio>(application) {

    override val service = RadioService()

}