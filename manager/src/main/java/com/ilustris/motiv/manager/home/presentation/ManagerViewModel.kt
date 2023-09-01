package com.ilustris.motiv.manager.home.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Report
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.helper.QuoteHelper
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.Ordering
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(
    application: Application,
    override val service: QuoteService,
    private val quoteHelper: QuoteHelper
) : BaseViewModel<Quote>(application) {

    val quotes = mutableStateListOf<QuoteDataModel>()
    val quoteCount = MutableLiveData<Int>(0)
    private var dataQuotes: List<Quote> = emptyList()

    fun deleteQuote(quoteDataModel: QuoteDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = service.deleteData(quoteDataModel.quoteBean.id)
            if (result.isSuccess) {
                quotes.remove(quoteDataModel)
            } else {
                sendErrorState(result.error.errorException)
            }
        }
    }

    fun removeReport(quoteData: QuoteDataModel, report: Report) {
        viewModelScope.launch(Dispatchers.IO) {
            val quote = quoteData.quoteBean
            quote.reports.remove(report)
            service.editData(quote).run {
                if (isSuccess) {
                    quotes[quotes.indexOf(quoteData)] = quoteData.copy(quoteBean = quote)
                }
            }
        }
    }


    private suspend fun loadQuoteListExtras(quotesDataList: List<Quote>) {
        quotes.clear()
        quotesDataList.forEach {
            quoteHelper.mapQuoteToQuoteDataModel(it, true).run {
                if (isSuccess) {
                    quotes.add(success.data)
                }
            }
        }
    }

    fun getQuotes() {
        viewModelState.postValue(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            service.getAllData(orderBy = "data", ordering = Ordering.DESCENDING).run {
                if (isSuccess) {
                    val list = (success.data as List<Quote>).sortedBy { it.reports.size }
                    quoteCount.postValue(list.size)
                    dataQuotes = list
                    loadQuoteListExtras(list)
                } else sendErrorState(error.errorException)
            }
        }
    }


}