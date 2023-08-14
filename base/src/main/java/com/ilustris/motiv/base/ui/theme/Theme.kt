package com.ilustris.motiv.foundation.ui.theme

import ai.atick.material.MaterialColor
import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.palette.graphics.Palette
import com.creat.motiv.base.R
import com.ilustris.motiv.base.utils.FontUtils

private val DarkColorScheme = darkColorScheme(
    primary = MaterialColor.Purple500,
    secondary = MaterialColor.DeepPurple500,
    tertiary = MaterialColor.PurpleA700,
    background = MaterialColor.Black,
    surface = MaterialColor.Gray900
)

private val LightColorScheme = lightColorScheme(
    primary = MaterialColor.Purple500,
    secondary = MaterialColor.DeepPurple500,
    tertiary = MaterialColor.PurpleA700,
    background = MaterialColor.White,
    surface = MaterialColor.Gray200

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

const val MOTIV_FONT = "Josefin Sans"

@Composable
fun MotivTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor =
                if (darkTheme) MaterialColor.Black.toArgb() else MaterialColor.White.toArgb()
            window.navigationBarColor =
                if (darkTheme) MaterialColor.Black.toArgb() else MaterialColor.White.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val defaultRadius = 10.dp
val radioRadius = 30.dp

val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@Composable
fun motivBrushes() = listOf(
    MaterialTheme.colorScheme.primary,
    MaterialTheme.colorScheme.secondary,
    MaterialTheme.colorScheme.tertiary,
)

@Composable
fun grayBrushes() = listOf(
    MaterialColor.Gray500,
    MaterialColor.Gray700,
    MaterialColor.Gray900,
    MaterialColor.Black
)

@Composable
fun managerBrushes() = listOf(
    MaterialColor.Gray300,
    MaterialColor.Pink100,
    MaterialColor.Purple200
)

@Composable
fun motivGradient() = Brush.linearGradient(colors = motivBrushes())

@Composable
fun managerGradient() = Brush.linearGradient(colors = managerBrushes(), tileMode = TileMode.Clamp)

@Composable
fun grayGradients() = Brush.linearGradient(
    colors = grayBrushes()
)

@Composable
fun textColorGradient() = Brush.verticalGradient(
    colors = listOf(
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.onBackground
    ),
    tileMode = TileMode.Clamp
)

@Composable
fun getDeviceWidth() = LocalConfiguration.current.screenWidthDp

@Composable
fun getDeviceHeight() = LocalConfiguration.current.screenHeightDp

@Composable
fun isPreviewMode() = LocalInspectionMode.current

@Composable
fun Bitmap.paletteFromBitMap() = Palette.from(this).generate()

fun Palette.brushsFromPalette(): Brush {
    val dominantSwatch = try {
        Color(this.dominantSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray900
    }

    val vibrantSwatch = try {
        Color(this.vibrantSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray500
    }

    val mutedSwatch = try {
        Color(this.mutedSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray300
    }

    return Brush.linearGradient(listOf(dominantSwatch, vibrantSwatch, mutedSwatch))
}

fun Palette.palleteColors(): List<Color> {
    val dominantSwatch = try {
        Color(this.dominantSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray900
    }

    val vibrantSwatch = try {
        Color(this.vibrantSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray500
    }

    val mutedSwatch = try {
        Color(this.mutedSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray300
    }
    return listOf(dominantSwatch, vibrantSwatch, mutedSwatch)
}

fun Palette.colorsFromPalette(): List<Color> {
    val dominantSwatch = try {
        Color(this.dominantSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray900
    }

    val vibrantSwatch = try {
        Color(this.vibrantSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray500
    }

    val mutedSwatch = try {
        Color(this.mutedSwatch!!.rgb)
    } catch (e: Exception) {
        MaterialColor.Gray300
    }

    return listOf(dominantSwatch, vibrantSwatch, mutedSwatch)
}

@Composable
fun MotivTitle() {
    val font = FontUtils.getFontFamily(MOTIV_FONT)

    Text(
        text = "Motiv",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.SemiBold,
        fontFamily = font,
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp)
            .gradientFill(gradientAnimation())
    )
}

fun Modifier.quoteCardModifier() = clip(RoundedCornerShape(defaultRadius))

@Composable
fun gradientAnimation(gradientColors: List<Color> = motivBrushes()): Brush {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = EaseInCubic),
            repeatMode = RepeatMode.Reverse,
        ), label = "gradientAnimation"
    )
    return Brush.linearGradient(
        gradientColors,
        tileMode = TileMode.Clamp,
        start = Offset.Zero,
        end = Offset(x = offsetAnimation.value * 2, y = offsetAnimation.value * 3)
    )
}

fun Modifier.gradientFill(brush: Brush) =
    graphicsLayer(alpha = 0.99f)
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                drawRect(brush, blendMode = BlendMode.SrcAtop)
            }
        }

fun Modifier.colorFill(color: Color) =
    graphicsLayer(alpha = 0.95f)
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                drawRect(color, blendMode = BlendMode.Darken)
            }
        }

fun Modifier.gradientOverlay(brush: Brush) =
    graphicsLayer()
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                drawRect(brush, blendMode = BlendMode.Darken)
            }
        }


@Composable
fun Modifier.radioIconModifier(
    rotationValue: Float,
    sizeValue: Dp,
    brush: Brush,
    borderWidth: Dp = 3.dp,
) =
    border(
        borderWidth,
        brush = brush,
        CircleShape
    )
        .padding(4.dp)
        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), CircleShape)
        .clip(CircleShape)
        .size(sizeValue)
        .rotate(rotationValue)


fun String.isGifUrl() = this.contains(".gif")

