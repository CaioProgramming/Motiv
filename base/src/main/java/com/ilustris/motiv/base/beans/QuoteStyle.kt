package com.ilustris.motiv.base.beans

import com.ilustris.motiv.base.R
import com.silent.ilustriscore.core.bean.BaseBean

private const val DEFAULT_BACKGROUND_URL = "https://media.giphy.com/media/RJy4FQlLbxDz4kJ6GF/giphy.gif"
const val DEFAULT_STYLE_ID = "DEFAULT_STYLE"
const val SPLASH_STYLE_ID = "SPLASH_STYLE"
const val ADMIN_STYLE_ID = "ADMIN_STYLE"
const val COMPANY_STYLE_ID = "COMPANY_STYLE"
const val SEARCH_STYLE_ID = "SEARCH_STYLE"
const val EMPTY_STYLE_ID = "EMPTY_STYLE"
const val FAVORITES_STYLE_ID = "FAVORITES_STYLE"
const val NEW_STYLE_ID = "NEW_STYLE"
const val DEFAULT_TEXT_COLOR = "#ffffff"
const val DEFAULT_SHADOW_COLOR = "#000000"

enum class TextSize(val sizeResource: Int) {
    DEFAULT(R.dimen.default_quote_size), BIG(R.dimen.big_quote_size), SMALL(R.dimen.low_quote_size), LOWEST(R.dimen.min_quote_size)
}

enum class TextAlignment {
    CENTER, START, END
}

data class QuoteStyle(override var id: String = "",
                      var font: Int = 0,
                      var textSize: TextSize = TextSize.DEFAULT,
                      var textAlignment: TextAlignment = TextAlignment.CENTER,
                      var textColor: String = DEFAULT_TEXT_COLOR,
                      var shadowColor: String = DEFAULT_SHADOW_COLOR,
                      var backgroundURL: String = DEFAULT_BACKGROUND_URL) : BaseBean(id) {
    companion object {
        val defaultStyle = QuoteStyle(id = DEFAULT_STYLE_ID)
        val favoriteStyle = QuoteStyle(
                id = FAVORITES_STYLE_ID,
                font = 4,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/3o7TKoWXm3okO1kgHC/giphy.gif"
        )
        val emptyStyle = QuoteStyle(
                id = EMPTY_STYLE_ID,
                textSize = TextSize.LOWEST,
                font = 3,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/PnIpBoEJl7aaoBDxHt/giphy.gif"
        )
        val searchStyle = QuoteStyle(
                id = SEARCH_STYLE_ID,
                font = 7,
                textColor = DEFAULT_TEXT_COLOR,
                textSize = TextSize.SMALL,
                backgroundURL = "https://media.giphy.com/media/3HG8cKDPyl9QoG2KvW/giphy.gif"
        )
        val splashStyle = QuoteStyle(
                id = SPLASH_STYLE_ID,
                font = 9,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/ij1WvlilscRFoIRn7u/giphy.gif"
        )
        val adminStyle = QuoteStyle(
                id = ADMIN_STYLE_ID,
                font = 12,
                backgroundURL = "https://media.giphy.com/media/xUOwGcu6wd0cXBj5n2/giphy.gif"
        )
        val newStyle = QuoteStyle(
                id = NEW_STYLE_ID,
                font = 6,
                backgroundURL = "https://media.giphy.com/media/bLdgTj2jCKe9Wf94Km/giphy.gif"
        )
        val companyStyle = QuoteStyle(
                id = COMPANY_STYLE_ID,
                font = 3,
                backgroundURL = "https://media.giphy.com/media/3ohzdVKKcOOUQfcQWk/giphy.gif"
        )

    }
}

