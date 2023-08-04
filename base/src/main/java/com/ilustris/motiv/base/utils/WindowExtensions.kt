package com.ilustris.motiv.base.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creat.motiv.base.R
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.data.model.Window
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.motivBrushes
import com.ilustris.motiv.foundation.ui.theme.motivGradient
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun Window.CustomWindow(
    modifier: Modifier,
    brush: Brush,
    author: String? = null,
    user: User? = null
) {
    val arrangement = Arrangement.SpaceBetween
    Column(
        modifier
            .fillMaxWidth()
            .background(brush, RoundedCornerShape(topEnd = defaultRadius, topStart = defaultRadius))
            .animateContentSize(tween(1000, easing = EaseIn))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = arrangement
        ) {
            when (this@CustomWindow) {
                Window.MODERN -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.gradientFill(brush)
                    ) {
                        motivBrushes().forEach {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(12.dp)
                                    .background(it, CircleShape)
                                    .border(
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                        width = 1.dp,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                    val fontFamily = FontUtils.getFontFamily("Lato")

                    val title = "${author ?: stringResource(id = R.string.app_name)}.app"
                    Text(
                        text = title,
                        modifier = Modifier
                            .padding()
                            .graphicsLayer(alpha = 0.6f),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light,
                        fontFamily = fontFamily,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.graphicsLayer(alpha = 0f)
                    ) {
                        motivBrushes().forEach {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(12.dp)
                                    .background(it, CircleShape)
                                    .border(
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                        width = 1.dp,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }

                Window.CLASSIC -> {
                    val fontFamily = FontUtils.getFontFamily("VT323")
                    val title = "${author ?: stringResource(id = R.string.app_name)}.exe"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        user?.let {
                            GlideImage(
                                imageModel = {
                                    it.picurl
                                },
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center,
                                ),
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(
                                        1.dp,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        CircleShape
                                    )

                            )
                        }
                        Text(
                            text = title,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = fontFamily,
                        )
                    }


                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.app_name),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }
    }

}

@Preview
@Composable
fun PreviewWindow() {
    Column {
        Window.values().forEach {
            it.CustomWindow(
                modifier = Modifier.fillMaxWidth(),
                motivGradient(),
            )
        }
    }

}



fun Modifier.borderForWindow(window: Window?, brush: Brush): Modifier = composed {

    val shapeForWindows = RoundedCornerShape(defaultRadius)

    when (window) {
        Window.CLASSIC -> background(MaterialTheme.colorScheme.background, shapeForWindows).border(
            width = 2.dp,
            brush = brush,
            shape = shapeForWindows
        )

        Window.MODERN, null -> background(
            MaterialTheme.colorScheme.surface,
            shape = shapeForWindows
        ).border(
            width = 1.dp,
            brush = brush,
            shape = shapeForWindows
        )

    }
}

fun Window?.getBorder(): RoundedCornerShape =
    if (this == null) RoundedCornerShape(defaultRadius) else RoundedCornerShape(
        bottomEnd = defaultRadius,
        bottomStart = defaultRadius
    )
