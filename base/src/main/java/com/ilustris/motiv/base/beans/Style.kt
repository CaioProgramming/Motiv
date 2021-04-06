package com.ilustris.motiv.base.beans

import com.silent.ilustriscore.core.bean.BaseBean

private const val DEFAULT_BACKGROUND_URL = "https://media.giphy.com/media/RJy4FQlLbxDz4kJ6GF/giphy.gif"
const val DEFAULT_STYLE_ID = "DEFAULT_STYLE"
const val VIEW_USERS_STYLE = "USERS_STYLE"
const val SPLASH_STYLE_ID = "SPLASH_STYLE"
const val ADMIN_STYLE_ID = "ADMIN_STYLE"
const val SEARCH_STYLE_ID = "SEARCH_STYLE"
const val EMPTY_STYLE_ID = "EMPTY_STYLE"
const val FAVORITES_STYLE_ID = "FAVORITES_STYLE"
const val NEW_STYLE_ID = "NEW_STYLE"
const val DEFAULT_TEXT_COLOR = "#ffffff"
const val DEFAULT_SHADOW_COLOR = "#000000"

enum class TextAlignment {
    CENTER, START, END
}

data class Style(override var id: String = DEFAULT_STYLE_ID,
                 var font: Int = 0,
                 var textAlignment: TextAlignment = TextAlignment.CENTER,
                 var textColor: String = DEFAULT_TEXT_COLOR,
                 var backgroundURL: String = DEFAULT_BACKGROUND_URL,
                 var shadowStyle: ShadowStyle = ShadowStyle()
) : BaseBean(id) {

    fun isStoredStyle(): Boolean = id != DEFAULT_STYLE_ID && id != VIEW_USERS_STYLE && id != SPLASH_STYLE_ID && id != ADMIN_STYLE_ID && id != EMPTY_STYLE_ID && id != FAVORITES_STYLE_ID && id != NEW_STYLE_ID

    companion object {
        val defaultStyle = Style()
        val favoriteStyle = Style(
                id = FAVORITES_STYLE_ID,
                font = 4,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/3o7TKoWXm3okO1kgHC/giphy.gif"
        )
        val emptyStyle = Style(
                id = EMPTY_STYLE_ID,
                font = 3,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/PnIpBoEJl7aaoBDxHt/giphy.gif"
        )
        val searchStyle = Style(
                id = SEARCH_STYLE_ID,
                font = 7,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/3HG8cKDPyl9QoG2KvW/giphy.gif"
        )
        val splashStyle = Style(
                id = SPLASH_STYLE_ID,
                font = 9,
                textColor = DEFAULT_TEXT_COLOR,
                backgroundURL = "https://media.giphy.com/media/ij1WvlilscRFoIRn7u/giphy.gif"
        )
        val adminStyle = Style(
                id = ADMIN_STYLE_ID,
                font = 12,
                backgroundURL = "https://media.giphy.com/media/xUOwGcu6wd0cXBj5n2/giphy.gif"
        )
        val newStyle = Style(
                id = NEW_STYLE_ID,
                font = 6,
                backgroundURL = "https://media.giphy.com/media/bLdgTj2jCKe9Wf94Km/giphy.gif"
        )


        val usersStyle = Style(
                id = VIEW_USERS_STYLE,
                font = 6,
                backgroundURL = "https://media.giphy.com/media/U3qYN8S0j3bpK/giphy.gif"
        )

    }
}

data class ShadowStyle(var radius: Float = 0f, var dx: Float = 0f, var dy: Float = 0f, var shadowColor: String = DEFAULT_SHADOW_COLOR)

