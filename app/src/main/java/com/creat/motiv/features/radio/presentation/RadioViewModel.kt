package com.creat.motiv.features.radio.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.data.model.Radio
import com.ilustris.motiv.base.service.RadioService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor(
    application: Application,
    override val service: RadioService
) : BaseViewModel<Radio>(application) {

    val radioList = MutableLiveData<List<Radio>>()

    override fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            updateViewState(ViewModelBaseState.LoadingState)
            val result = service.getAllData(orderBy = "name")
            if (result.isSuccess) {
                updateViewState(ViewModelBaseState.DataListRetrievedState(result.success.data))
                radioList.postValue(result.success.data as List<Radio>)
            } else sendErrorState(result.error.errorException)
        }
    }
}