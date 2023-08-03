package com.ilustris.motiv.base.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.silent.ilustriscore.core.bean.BaseBean


const val DEFAULT_TEXT_COLOR = "#ffffff"
const val DEFAULT_SHADOW_COLOR = "#000000"
private const val DEFAULT_BACKGROUND_URL =
    "https://media.giphy.com/media/3og0INtldac8gncQO4/giphy-downsized-large.gif"
const val NEW_STYLE_BACKGROUND = "https://media.giphy.com/media/bLdgTj2jCKe9Wf94Km/giphy.gif"
const val DEFAULT_FONT_FAMILY = "Roboto"


@IgnoreExtraProperties
data class Style(
    override var id: String = "",
    var font: Int = 0,
    var textAlignment: TextAlignment = TextAlignment.CENTER,
    var fontStyle: FontStyle = FontStyle.REGULAR,
    var textColor: String = DEFAULT_TEXT_COLOR,
    var backgroundURL: String = DEFAULT_BACKGROUND_URL,
    var animationProperties: AnimationProperties? = null,
    var textProperties: TextProperties? = null,
    var shadowStyle: ShadowStyle? = null,
    var styleProperties: StyleProperties? = null,
) : BaseBean(id)

enum class TextAlignment {
    CENTER, START, END, JUSTIFY
}

enum class FontStyle {
    REGULAR, ITALIC, BOLD, BLACK
}


data class ShadowStyle(
    var radius: Float = 0f,
    var dx: Float = 0f,
    var dy: Float = 0f,
    var shadowColor: String = DEFAULT_SHADOW_COLOR,
    var strokeColor: String = DEFAULT_SHADOW_COLOR
)

data class StyleProperties(
    val clipMask: Boolean = false,
    val backgroundColor: String? = null,
    val customWindow: Window = Window.MODERN,
)

data class TextProperties(
    var fontFamily: String = "Roboto",
    var textColor: String = DEFAULT_TEXT_COLOR,
    var textAlignment: TextAlignment = TextAlignment.CENTER,
    var fontStyle: FontStyle = FontStyle.REGULAR,
)

data class AnimationProperties(
    val animation: AnimationOptions = AnimationOptions.TYPE,
    val transition: AnimationTransition = AnimationTransition.LETTERS,
)

enum class TextOptions {
    HIGHLIGHT
}

enum class BlendMode {
    NORMAL, DARKEN, LIGHTEN, OVERLAY, SCREEN
}

enum class AnimationOptions {
    TYPE, FADE, SCALE
}

enum class AnimationTransition(val title: String) {
    WORDS("Palavras"), LETTERS("Letras"), LINES("Linhas")
}

enum class Window {
    CLASSIC, MODERN
}

