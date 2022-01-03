package com.creat.motiv.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.motiv.base.beans.*
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.StyleService
import com.ilustris.motiv.base.service.UserService
import com.silent.ilustriscore.core.model.BaseViewModel
import kotlinx.coroutines.launch

class QuoteListViewModel : BaseViewModel<Quote>() {
    override val service = QuoteService()
    val styleService = StyleService()
    val userService = UserService()

    val quoteDataListViewState = MutableLiveData<QuoteDataListViewState>()

    fun getQuotes() {
        viewModelScope.launch {
            val quotes = service.getAllData() as quoteList
            val quoteDatas = ArrayList<QuoteData>()
            quotes.forEach {
                val style = styleService.getSingleData(it.style).success.data as Style
                val quoteUser = if (it.userID == currentUser?.uid) {
                    User.fromFirebase(currentUser!!)
                } else {
                    userService.getSingleData(it.userID).success.data as User
                }
                quoteDatas.add(QuoteData(it, style, quoteUser))
            }
            quoteDataListViewState.postValue(QuoteDataListViewState.QuotesRetrieve(quoteDatas))
        }

    }

}