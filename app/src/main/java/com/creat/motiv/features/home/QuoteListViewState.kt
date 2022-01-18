package com.creat.motiv.features.home

import com.creat.motiv.features.share.QuoteShareData
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.QuoteAdapterData
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.dialog.listdialog.dialogItems

sealed class HomeViewState {
    data class RequestDelete(val quote: Quote) : HomeViewState()
    data class RequestReport(val quote: Quote) : HomeViewState()
    data class RequestEdit(val quote: Quote) : HomeViewState()
    data class RequestShare(val quoteShareData: QuoteShareData) : HomeViewState()
    data class QuoteDataRetrieve(val quotedata: QuoteAdapterData) : HomeViewState()
    data class QuoteOptionsRetrieve(val dialogItems: dialogItems) : HomeViewState()
    data class UserRetrieved(val user: User) : HomeViewState()
    data class UsersRetrieved(val users: List<User>) : HomeViewState()
}