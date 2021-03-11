package com.creat.motiv.quote.beans

import com.silent.ilustriscore.core.bean.BaseBean
import java.util.*
import kotlin.collections.ArrayList

const val AD_QUOTE = "ADVERTISEMENT_QUOTE"
const val PROFILE_QUOTE = "PROFILE_QUOTE"
const val NO_RESULTS_QUOTE = "NO_RESULTS_QUOTE"
const val FAVORITE_QUOTE = "FAVORITE_QUOTE"
const val SEARCH_QUOTE = "SEARCH_QUOTE"
const val SPLASH_QUOTE = "SPLASH_QUOTE"

data class Quote(
        var quote: String = "",
        var author: String = "",
        var style: String = DEFAULT_STYLE_ID,
        val data: Date = Date(),
        var userID: String = "",
        var isReport: Boolean = false,
        var likes: ArrayList<String> = ArrayList(),
        override var id: String = "") : BaseBean(id) {

    fun isUserQuote(): Boolean {
        return id != AD_QUOTE && id != PROFILE_QUOTE && id != NO_RESULTS_QUOTE && id != FAVORITE_QUOTE && id != SEARCH_QUOTE && id != SPLASH_QUOTE
    }

    companion object {
        fun advertiseQuote(): Quote = Quote(id = AD_QUOTE)
        fun profileQuote(): Quote = Quote(id = PROFILE_QUOTE)
        fun noResultsQuote(): Quote = Quote(id = NO_RESULTS_QUOTE, style = EMPTY_STYLE_ID, quote = "Nenhum resultado encontrado\n:(")
        fun favoritesQuote(): Quote = Quote(id = FAVORITE_QUOTE, style = FAVORITES_STYLE_ID, quote = "Favoritos")
        fun searchQuote(): Quote = Quote(id = SEARCH_QUOTE, style = SEARCH_STYLE_ID, quote = "Hora de buscar coisas milaborantes...")
        fun splashQuote(): Quote = Quote(id = SPLASH_QUOTE, style = SPLASH_STYLE_ID, quote = "Motiv", author = "O melhor app do mundo...")
    }
}
