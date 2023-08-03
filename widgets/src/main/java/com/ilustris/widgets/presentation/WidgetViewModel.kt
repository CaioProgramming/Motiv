package com.ilustris.widgets.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.helper.QuoteHelper
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetViewModel @Inject constructor(
    override val service: QuoteService,
    val quoteHelper: QuoteHelper,
    application: Application
) : BaseViewModel<Quote>(application) {


    val randomQuote = MutableLiveData<QuoteDataModel?>(null)

    override fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            service.getAllData(orderBy = "data", limit = 10).let {
                if (it.isSuccess) {
                    val quoteList = it.success.data as List<Quote>
                    quoteHelper.mapQuoteToQuoteDataModel(quoteList.random(), false).run {
                        if (isSuccess) {
                            randomQuote.postValue(success.data)
                        }
                    }
                } else {
                    sendErrorState(it.error.errorException)
                }
            }
        }
    }
}