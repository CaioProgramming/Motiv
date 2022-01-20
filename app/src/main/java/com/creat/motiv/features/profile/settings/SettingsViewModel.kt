package com.creat.motiv.features.profile.settings

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.creat.motiv.features.profile.viewmodel.ProfileViewState
import com.google.firebase.auth.UserProfileChangeRequest
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.service.CoverService
import com.ilustris.motiv.base.service.IconsService
import com.ilustris.motiv.base.service.UserService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.ArrayList

class SettingsViewModel : BaseViewModel<User>() {

    val settingsViewState = MutableLiveData<SettingsViewState>()

    fun updateUserPic(user: User, uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profileChangeRequest =
                    UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(uri)).build()
                currentUser?.updateProfile(profileChangeRequest)?.await()
                service.editField(currentUser!!.photoUrl.toString(), user.id, "picurl").success
                viewModelState.postValue(ViewModelBaseState.DataUpdateState(user.apply {
                    picurl = uri
                }))
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UPDATE))
            }
        }

    }

    fun updateUserCover(user: User, uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                service.editField(uri, user.id, "cover").success
                viewModelState.postValue(ViewModelBaseState.DataUpdateState(user.apply {
                    cover = uri
                }))
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UPDATE))
            }
        }
    }

    override val service = UserService()
    private val iconsService = IconsService()
    private val coversService = CoverService()

    fun requestIcons(requiredUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val icons = iconsService.getAllData().success.data as ArrayList<Icon>
            settingsViewState.postValue(SettingsViewState.IconsRetrieved(icons, requiredUser))
        }
    }

    fun requestCovers(requiredUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val covers = coversService.getAllData().success.data as ArrayList<Cover>
            settingsViewState.postValue(SettingsViewState.CoversRetrieved(covers, requiredUser))
        }
    }

}