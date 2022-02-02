package com.ilustris.motiv.manager.features.style.newstyle.viewmodel

import android.app.Application
import android.graphics.Typeface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.dialog.listdialog.DialogData
import com.ilustris.motiv.base.service.FontsService
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewStyleViewModel(application: Application) : BaseViewModel<Style>(application) {
    override val service = StyleService()
    private val fontsService = FontsService(application)
    val newStyleState = MutableLiveData<NewStyleState>()
    override fun getAllData() {
        viewModelScope.launch {
            try {
                val styles = service.getAllData().success.data as ArrayList<Style>
                styles.forEachIndexed { index, style ->
                    fontsService.requestDownload(fontsService.getFamilyName(style.font)) { tp, s ->
                        style.typeface = tp
                        if (index == styles.lastIndex) {
                            viewModelState.postValue(
                                ViewModelBaseState.DataListRetrievedState(
                                    styles as ArrayList<BaseBean>
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }

    }

    fun getFontOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val families = fontsService.getFamilies()
            val dialogItems = ArrayList<DialogData>()
            families.forEachIndexed { index, s ->
                dialogItems.add(DialogData(s) {
                    requestFont(s, index)
                })
            }
            newStyleState.postValue(NewStyleState.FontOptionsRetrieved(dialogItems))
        }
    }

    fun requestFont(family: String, index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            fontsService.requestDownload(family) { typeface: Typeface?, s: String? ->
                typeface?.let {
                    newStyleState.postValue(NewStyleState.SelectFont(index, it))
                }
            }
        }
    }
}