package com.creat.motiv.features.about

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.creat.motiv.features.about.data.AboutData
import com.creat.motiv.features.about.data.Reference
import com.ilustris.motiv.base.beans.Developer
import com.ilustris.motiv.base.service.DeveloperService
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AboutViewModel : BaseViewModel<Developer>() {
    override val service = DeveloperService()
    val aboutViewState = MutableLiveData<AboutViewState>()

    sealed class AboutViewState {
        data class AboutDataRetrieved(val aboutData: ArrayList<AboutData>) : AboutViewState()
    }


    fun getAboutScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val aboutData = ArrayList<AboutData>()
                val developers = service.getAllData() as ArrayList<Developer>
                aboutData.add(AboutData("Desenvolvedores", developers = developers))
                aboutData.add(AboutData("Referências", references = Reference.references))
                aboutViewState.postValue(AboutViewState.AboutDataRetrieved(aboutData))
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }

    }


}