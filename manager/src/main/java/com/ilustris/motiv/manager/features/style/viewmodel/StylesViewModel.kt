package com.ilustris.motiv.manager.features.style.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.service.FontsService
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.launch

class StylesViewModel(application: Application) : BaseViewModel<Style>(application) {
    override val service = StyleService()
    private val fontsService = FontsService(application)

    override fun getAllData() {
        viewModelScope.launch {
            val styles = service.getAllData().success.data as ArrayList<Style>
            styles.forEachIndexed { index, style ->
                fontsService.requestDownload(
                    fontsService.getFamilyName(style.font)
                ) { typeface, s ->
                    style.typeface = typeface
                }
                if (index == styles.lastIndex) {
                    viewModelState.postValue(ViewModelBaseState.DataListRetrievedState(styles as ArrayList<BaseBean>))
                }
            }
        }
    }

}