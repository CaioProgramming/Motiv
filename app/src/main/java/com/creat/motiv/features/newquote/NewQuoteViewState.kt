package com.creat.motiv.features.newquote

import android.graphics.Typeface
import com.ilustris.motiv.base.beans.Style

sealed class NewQuoteViewState {

    data class StyleTypeFaceRetrieved(val index: Int, val typeface: Typeface?) : NewQuoteViewState()
    data class StylesRetrieved(val styles: List<Style>) : NewQuoteViewState()
    companion object EmptyQuote : NewQuoteViewState()
}