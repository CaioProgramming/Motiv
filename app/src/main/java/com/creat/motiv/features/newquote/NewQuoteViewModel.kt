package com.creat.motiv.features.newquote

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.service.FontsService
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.launch

class NewQuoteViewModel(application: Application) : BaseViewModel<Quote>(application) {

    private val fontsService = FontsService(application)
    override val service = QuoteService()
    private val styleService = StyleService()
    val newQuoteViewState = MutableLiveData<NewQuoteViewState>()
    fun getStyles() {
        viewModelScope.launch {
            try {
                val styles = styleService.getAllData().success.data as ArrayList<Style>
                styles.forEachIndexed { index, style ->
                    fontsService.requestDownload(fontsService.getFamilyName(style.font)) { tp, s ->
                        style.typeface = tp
                        if (index == styles.lastIndex) {
                            newQuoteViewState.postValue(NewQuoteViewState.StylesRetrieved(styles = styles))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }
    }

    override fun saveData(data: Quote) {
        data.apply {
            userID = getUser()!!.uid
            if (author.isEmpty()) author = "Autor desconhecido"

        }
        if (data.quote.isNotEmpty()) {
            super.saveData(data)
        } else {
            newQuoteViewState.postValue(NewQuoteViewState.EmptyQuote)
        }
    }


}