package com.creat.motiv

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.ilustris.motiv.base.data.model.Icon
import com.ilustris.motiv.base.data.model.Radio
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.service.IconService
import com.ilustris.motiv.base.service.UserLiveService
import com.ilustris.motiv.base.service.UserService
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.contract.ServiceResult
import com.silent.ilustriscore.core.model.BaseLiveViewModel
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    override val liveService: UserLiveService,
    private val iconService: IconService,
    application: Application
) : BaseLiveViewModel<User>(
    application
) {

    private var userFlow: Flow<ServiceResult<DataError,BaseBean>> = flow {  liveService.getSingleData(getUser()?.uid ?: "") }
    val currentUser = MutableLiveData<User?>(null)
    val playingRadio = MutableLiveData<Radio>(null)

    fun validateAuth() {
        if (!isAuthenticated()) {
            updateViewState(ViewModelBaseState.RequireAuth)
        } else {
            collectFlow()
        }
    }

    private fun collectFlow() {
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                userFlow = liveService.getSingleData(it.uid)
                userFlow.collect { result ->
                    if (result.isSuccess) {
                        currentUser.postValue(result.success.data as User)
                    } else {
                        saveNewUser()
                    }
                }
            }
        }
    }

    fun validateLogin(result: FirebaseAuthUIAuthenticationResult?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (result == null) {
                sendErrorState(DataError.Auth)
            } else {
                collectFlow()
            }
        }
    }

    private fun saveNewUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val icons = iconService.getAllData()
            if (icons is ServiceResult.Success) {
                val iconList = icons.data as List<Icon>
                val newUserResult = liveService.saveNewUser(iconList.random().uri)
                if (newUserResult !is ServiceResult.Success) {
                    updateViewState(ViewModelBaseState.ErrorState(newUserResult.error.errorException))
                }
            }
        }

    }

    fun updatePlayingRadio(radio: Radio?) {
        playingRadio.postValue(radio)
    }




}