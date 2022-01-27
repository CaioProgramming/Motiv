package com.creat.motiv.features.newquote

import com.ilustris.motiv.base.beans.Style

sealed class NewQuoteViewState {

    data class StylesRetrieved(val styles: List<Style>) : NewQuoteViewState()
    companion object EmptyQuote : NewQuoteViewState()
}