package com.ilustris.motiv.base.ui.presentation

import android.app.Application
import com.ilustris.motiv.base.data.model.Icon
import com.ilustris.motiv.base.service.CoverService
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoversViewModel @Inject constructor(
    application: Application,
    override val service: CoverService
) : BaseViewModel<Icon>(application)