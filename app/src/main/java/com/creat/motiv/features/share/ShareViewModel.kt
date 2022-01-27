package com.creat.motiv.features.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.beans.quote.QuoteShareData
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.StyleService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.launch
import java.lang.Exception

class ShareViewModel : BaseViewModel<Quote>() {

    override val service = QuoteService()
    val styleService = StyleService()
    val shareViewState = MutableLiveData<ShareViewState>()

    fun fetchQuoteStyle(quote: Quote) {
        viewModelState.value = ViewModelBaseState.LoadingState
        viewModelScope.launch {
            try {
                val stylerequest = styleService.getSingleData(quote.style)
                if (stylerequest.isSuccess) {
                    shareViewState.postValue(
                        ShareViewState.ShareQuoteRetrieved(
                            QuoteShareData(
                                quote,
                                stylerequest.success.data as Style
                            )
                        )
                    )
                } else {
                    throw Exception()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelState.postValue(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }

    }

}