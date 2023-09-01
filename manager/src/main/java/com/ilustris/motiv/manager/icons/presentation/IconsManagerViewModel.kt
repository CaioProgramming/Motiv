package com.ilustris.motiv.manager.icons.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.data.model.Icon
import com.ilustris.motiv.base.service.IconService
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class IconsManagerViewModel @Inject constructor(
    application: Application,
    override val service: IconService
) : BaseViewModel<Icon>(application) {

    fun saveIcon(uri: Uri) {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fileName = File(uri.path!!).name
                val icon = Icon(uri.toString(), id = fileName)
                editData(icon)
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataError.Unknown(e.message)))
            }
        }
    }


}