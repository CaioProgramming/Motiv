package com.creat.motiv.features

import androidx.lifecycle.ViewModel
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.service.RadioService
import com.ilustris.motiv.base.service.UserService
import com.silent.ilustriscore.core.model.BaseViewModel

class MainViewModel : ViewModel() {

    val userService = UserService()
    val radioService = RadioService()


}