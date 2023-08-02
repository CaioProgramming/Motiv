package com.ilustris.motiv.base.ui.presentation

import android.app.Application
import com.ilustris.motiv.base.data.model.Icon
import com.ilustris.motiv.base.service.IconService
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IconsViewModel @Inject constructor(
    application: Application,
    override val service: IconService
) : BaseViewModel<Icon>(application)