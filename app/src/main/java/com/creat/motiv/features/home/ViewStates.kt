package com.creat.motiv.features.home

import com.creat.motiv.features.share.QuoteShareData
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.QuoteAdapterData
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.dialog.listdialog.dialogItems

sealed class QuoteListViewState {
    data class RequestDelete(val quote: Quote) : QuoteListViewState()
    data class RequestReport(val quote: Quote) : QuoteListViewState()
    data class RequestEdit(val quote: Quote) : QuoteListViewState()
    data class RequestShare(val quoteShareData: QuoteShareData) : QuoteListViewState()
    data class QuoteDataRetrieve(val quotedata: QuoteAdapterData) : QuoteListViewState()
    data class QuoteOptionsRetrieve(val dialogItems: dialogItems) : QuoteListViewState()

}

sealed class HomeViewState {
    data class UserRetrieved(val user: User) : HomeViewState()
    data class UsersRetrieved(val users: List<User>) : HomeViewState()
}