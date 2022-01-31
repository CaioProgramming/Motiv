package com.ilustris.motiv.manager.features.style.viewmodel

import android.app.Application
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel

class StylesViewModel(application: Application) : BaseViewModel<Style>(application) {
    override val service = StyleService()
}