package com.creat.motiv.quote.beans

import com.silent.ilustriscore.core.bean.BaseBean

private const val DEFAULT_BACKGROUND_URL = "https://media.giphy.com/media/jaOXKCxtBPLieRLI0c/giphy.gif"
const val DEFAULT_STYLE_ID = "DEFAULT_STYLE"
const val SPLASH_STYLE_ID = "SPLASH_STYLE"
const val SEARCH_STYLE_ID = "SEARCH_STYLE"
const val EMPTY_STYLE_ID = "EMPTY_STYLE"
const val FAVORITES_STYLE_ID = "FAVORITES_STYLE"
const val DEFAULT_TEXT_COLOR = "#ffffff"
const val DEFAULT_SHADOW_COLOR = "#00ffffff"

enum class TextSize {
    DEFAULT, BIG, SMALL, EXTRASMALL
}

data class QuoteStyle(override var id: String = "",
                      val font: Int = 0,
                      val textSize: TextSize = TextSize.BIG,
                      val textColor: String = DEFAULT_TEXT_COLOR,
                      val shadowColor: String = DEFAULT_SHADOW_COLOR,
                      val backgroundURL: String = DEFAULT_BACKGROUND_URL) : BaseBean(id) {
    companion object {
        val defaultStyle = QuoteStyle(
                id = DEFAULT_STYLE_ID,
                font = 0,
                backgroundURL = "https://media.giphy.com/media/jaOXKCxtBPLieRLI0c/giphy.gif",
                textColor = DEFAULT_TEXT_COLOR
        )
        val favoriteStyle = QuoteStyle(
                id = FAVORITES_STYLE_ID,
                font = 4,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/3o7TKoWXm3okO1kgHC/giphy.gif"
        )
        val emptyStyle = QuoteStyle(
                id = EMPTY_STYLE_ID,
                font = 3,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/4P9cnXkNigoYo/giphy.gif"
        )
        val searchStyle = QuoteStyle(
                id = SEARCH_STYLE_ID,
                font = 7,
                textColor = DEFAULT_TEXT_COLOR,
                textSize = TextSize.SMALL,
                backgroundURL = "https://media.giphy.com/media/MaThe6p8WAKbf9NDDM/giphy.gif"
        )
        val splashStyle = QuoteStyle(
                id = SPLASH_STYLE_ID,
                font = 9,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/3o7aDcrsww5Ybp18hq/giphy.gif"
        )

    }
}

