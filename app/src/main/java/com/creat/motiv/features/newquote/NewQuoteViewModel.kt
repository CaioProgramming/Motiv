package com.creat.motiv.features.newquote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.launch

class NewQuoteViewModel : BaseViewModel<Quote>() {

    override val service = QuoteService()
    private val styleService = StyleService()
    val newQuoteViewState = MutableLiveData<NewQuoteViewState>()
    fun getStyles() {
        viewModelScope.launch {
            try {
                val styles = styleService.getAllData().success.data
                newQuoteViewState.postValue(NewQuoteViewState.StylesRetrieved(styles = styles as ArrayList<Style>))
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }
    }

    override fun saveData(data: Quote) {
        data.apply {
            userID = currentUser!!.uid
            if (author.isEmpty()) author = "Autor desconhecido"

        }
        if (data.quote.isNotEmpty()) {
            super.saveData(data)
        } else {
            newQuoteViewState.postValue(NewQuoteViewState.EmptyQuote)
        }
    }


}