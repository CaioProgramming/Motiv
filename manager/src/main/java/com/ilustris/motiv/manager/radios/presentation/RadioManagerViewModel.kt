package com.ilustris.motiv.manager.radios.presentation

import android.app.Application
import com.ilustris.motiv.base.data.model.Radio
import com.ilustris.motiv.base.service.RadioService
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RadioManagerViewModel @Inject constructor(
    application: Application,
    override val service: RadioService
) : BaseViewModel<Radio>(application)