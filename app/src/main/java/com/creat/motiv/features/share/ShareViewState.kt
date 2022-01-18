package com.creat.motiv.features.share

sealed class ShareViewState {

    data class ShareQuoteRetrieved(val quoteShareData: QuoteShareData) : ShareViewState()

}
