package com.ilustris.motiv.manager.features.cover

import android.app.Application
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.service.CoverService
import com.silent.ilustriscore.core.model.BaseViewModel

class CoversViewModel(application: Application) : BaseViewModel<Cover>(application) {
    override val service = CoverService()
}