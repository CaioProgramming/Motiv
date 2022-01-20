package com.ilustris.motiv.base.beans.quote

import com.ilustris.motiv.base.dialog.listdialog.dialogItems

sealed class QuoteListViewState {
    data class RequestDelete(val quote: Quote) : QuoteListViewState()
    data class RequestReport(val quote: Quote) : QuoteListViewState()
    data class RequestEdit(val quote: Quote) : QuoteListViewState()
    data class RequestShare(val quoteShareData: QuoteShareData) : QuoteListViewState()
    data class QuoteDataRetrieve(val quotedata: QuoteAdapterData) : QuoteListViewState()
    data class QuoteOptionsRetrieve(val dialogItems: dialogItems) : QuoteListViewState()

}