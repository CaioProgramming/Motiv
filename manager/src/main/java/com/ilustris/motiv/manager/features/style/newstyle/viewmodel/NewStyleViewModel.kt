package com.ilustris.motiv.manager.features.style.newstyle.viewmodel

import android.app.Application
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel

class NewStyleViewModel(application: Application) : BaseViewModel<Style>(application) {
    override val service = StyleService()
}