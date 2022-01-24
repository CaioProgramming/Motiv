package com.ilustris.motiv.manager.features.style.newstyle.viewmodel

import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel

class NewStyleViewModel : BaseViewModel<Style>() {
    override val service = StyleService()
}