package com.ilustris.motiv.manager.features.icon.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.service.IconsService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.launch
import java.util.*

class IconsViewModel : BaseViewModel<Icon>() {

    override val service = IconsService()
    val iconsViewState = MutableLiveData<IconsViewState>()

    fun uploadIcons(icons: ArrayList<Icon>) {
        viewModelScope.launch {
            try {
                icons.forEach {
                    val uploadRequest = service.uploadToStorage(it.uri)
                    if (uploadRequest.isError) {
                        viewModelState.postValue(ViewModelBaseState.ErrorState(uploadRequest.error.errorException))
                        return@launch
                    } else {
                        saveData(Icon(uri = uploadRequest.success.data))
                    }
                }
                iconsViewState.postValue(IconsViewState.IconsUploaded)
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }

    }

}