package com.ilustris.motiv.manager.features.cover

import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.service.CoverService
import com.silent.ilustriscore.core.model.BaseViewModel

class CoversViewModel : BaseViewModel<Cover>() {
    override val service = CoverService()
}