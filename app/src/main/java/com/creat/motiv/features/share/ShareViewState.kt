package com.creat.motiv.features.share

import com.ilustris.motiv.base.beans.quote.QuoteShareData

sealed class ShareViewState {

    data class ShareQuoteRetrieved(val quoteShareData: QuoteShareData) : ShareViewState()

}
