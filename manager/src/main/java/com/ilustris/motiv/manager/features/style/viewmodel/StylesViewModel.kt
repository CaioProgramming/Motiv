package com.ilustris.motiv.manager.features.style.viewmodel

import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel

class StylesViewModel : BaseViewModel<Style>() {
    override val service = StyleService()
}