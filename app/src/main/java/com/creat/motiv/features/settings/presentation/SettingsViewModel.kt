package com.creat.motiv.features.settings.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.creat.motiv.features.settings.data.UserMetaData
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.data.model.Cover
import com.ilustris.motiv.base.data.model.Icon
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.service.AdService
import com.ilustris.motiv.base.service.UserService
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    override val service: UserService,
    val adService: AdService
) : BaseViewModel<User>(application) {


    val user = MutableLiveData<User>(null)
    val userMetadata = MutableLiveData<UserMetaData?>(null)
    val adAvailable = MutableLiveData<RewardedAd?>(null)

    private fun fetchAdAvailability() {
        adService.getRewardedAd {
            if (it.isSuccess) {
                adAvailable.postValue(it.success.data)
            }
        }
    }

    fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                val userRequest = service.getSingleData(it.uid)
                if (userRequest.isSuccess) {
                    user.postValue(userRequest.success.data as User)
                    it.buildMetadaData((userRequest.success.data as User).admin)
                } else {
                    sendErrorState(userRequest.error.errorException)
                }
            } ?: run {
                sendErrorState(DataError.Auth)
                user.postValue(null)
            }
        }
        fetchAdAvailability()
    }

    fun FirebaseUser.buildMetadaData(admin: Boolean) {
        Log.i(
            javaClass.simpleName,
            "buildMetadaData: providers -> \n ${this.providerData.joinToString { it.providerId }}}"
        )
        userMetadata.postValue(
            UserMetaData(
                email = this.email,
                admin = admin,
                provider = this.providerData.first().providerId,
                createTimeStamp = this.metadata?.creationTimestamp,
                emailVerified = this.isEmailVerified,
            )
        )
    }

    fun deleteAccount() {
        sendLoading()
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                service.deleteData(it.uid).run {
                    if (isSuccess) {
                        delayedFunction(2000) {
                            FirebaseAuth.getInstance().currentUser?.delete()
                            viewModelState.postValue(ViewModelBaseState.DataDeletedState)
                        }
                    } else {
                        Log.e(javaClass.simpleName, "deleteAccount: Error  ${error.errorException}")
                        sendErrorState(error.errorException)
                    }
                }

            }
        }
    }

    private fun sendLoading() {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
    }

    fun logOut() {
        FirebaseAuth.getInstance().signOut()
        fetchUser()
    }

    fun updateUserName(newName: String) {
        sendLoading()
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                service.editField(newName, it.uid, "name").run {
                    user.value?.let {
                        viewModelState.postValue(ViewModelBaseState.DataUpdateState(it.copy(name = newName)))
                    }
                }
            }

        }
    }

    fun updateUserIcon(icon: Icon) {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                service.editField(icon.uri, it.uid, "picurl").run {
                    user.value?.let {
                        viewModelState.postValue(ViewModelBaseState.DataUpdateState(it.copy(picurl = icon.uri)))
                    }
                }
            }

        }
    }

    private fun refreshUser() {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
        user.postValue(null)
        delayedFunction(500) {
            fetchUser()
        }
    }

    fun updateUserCover(cover: Cover) {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                service.editField(cover.url, it.uid, "cover").run {
                    user.value?.let { user ->
                        viewModelState.postValue(ViewModelBaseState.DataUpdateState(user.copy(cover = cover.url)))
                    }
                }
            }

        }
    }

}