package com.creat.motiv.features.home.presentation

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Report
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.helper.QuoteHelper
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    override val service: QuoteService,
    private val quoteHelper: QuoteHelper
) : BaseViewModel<Quote>(application) {


    val quotes = MutableLiveData<List<QuoteDataModel>>(emptyList())
    var dataQuotes: List<Quote> = emptyList()
    var shareState = MutableLiveData<ShareState>(null)


    override fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            updateViewState(ViewModelBaseState.LoadingState)
            val result = service.getAllData(orderBy = "data", limit = 20)
            if (result.isSuccess) {
                val list = result.success.data as List<Quote>
                dataQuotes = list
                loadQuoteListExtras(list)
            } else sendErrorState(result.error.errorException)
        }
    }

    private suspend fun loadQuoteListExtras(quotesDataList: List<Quote>) {
        try {
            quoteHelper.mapQuoteToQuoteDataModel(quotesDataList).run {
                if (this.isSuccess) {
                    quotes.postValue(this.success.data)
                } else {
                    sendErrorState(this.error.errorException)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendErrorState(DataException.UNKNOWN)
        }
    }


    fun searchQuote(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isNotEmpty()) {
                val queryQuotes = dataQuotes.filter {
                    it.quote.contains(query, true) || it.author.contains(query, true)
                }
                loadQuoteListExtras(queryQuotes)
            }
        }
    }

    fun deleteQuote(quoteDataModel: QuoteDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = service.deleteData(quoteDataModel.quoteBean.id)
            if (result.isSuccess) {
                getAllData()
            } else {
                sendErrorState(result.error.errorException)
            }
        }
    }

    fun likeQuote(quoteDataModel: QuoteDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val quote = quoteDataModel.quoteBean
            getUser()?.let {
                if (quote.likes.contains(it.uid)) {
                    quote.likes.remove(it.uid)
                } else {
                    quote.likes.add(it.uid)
                }
                val result = service.editData(quote)
                if (result.isSuccess) {
                    getAllData()
                } else {
                    sendErrorState(result.error.errorException)
                }
            }
        }
    }

    fun reportQuote(quote: Quote, reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUser()?.let {
                quote.reports.add(Report(it.uid, reason, Timestamp.now()))
                super.editData(quote)
            }
        }
    }

    fun handleShare(quote: Quote, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            quoteHelper.generateQuoteImage(
                getApplication<Application>().applicationContext,
                quote,
                bitmap
            ).run {
                if (isSuccess) {
                    shareState.postValue(ShareState.ShareSuccess(this.success.data, quote))
                } else {
                    shareState.postValue(ShareState.ShareError)
                }
            }
        }
    }
}

sealed class ShareState {
    object ShareError : ShareState()
    data class ShareSuccess(val uri: Uri, val quote: Quote) : ShareState()
}