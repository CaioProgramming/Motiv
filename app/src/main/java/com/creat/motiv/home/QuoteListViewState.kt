package com.creat.motiv.home

import com.ilustris.motiv.base.beans.QuoteData

sealed class QuoteDataListViewState {
    data class QuotesRetrieve(val quotedataList: ArrayList<QuoteData>) : QuoteDataListViewState()
}