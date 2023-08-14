@file:OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)

package com.ilustris.manager.feature.styles.ui.form.ui

import ai.atick.material.MaterialColor
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.creat.motiv.manager.R
import com.ilustris.motiv.base.data.model.AnimationOptions
import com.ilustris.motiv.base.data.model.AnimationProperties
import com.ilustris.motiv.base.data.model.AnimationTransition
import com.ilustris.motiv.base.data.model.BlendMode
import com.ilustris.motiv.base.data.model.FontStyle
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.ShadowStyle
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.data.model.StyleProperties
import com.ilustris.motiv.base.data.model.TextAlignment
import com.ilustris.motiv.base.data.model.TextProperties
import com.ilustris.motiv.base.data.model.Window
import com.ilustris.motiv.base.utils.BuildQuoteWindow
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.grayGradients
import com.ilustris.motiv.foundation.ui.theme.managerGradient
import com.ilustris.motiv.foundation.ui.theme.textColorGradient
import com.ilustris.motiv.foundation.utils.ColorUtils
import com.ilustris.motiv.manager.giphy.GiphySelectDialog
import com.ilustris.motiv.manager.styles.ui.form.presentation.NewStyleViewModel
import com.ilustris.motiv.manager.styles.utils.StyleUtils
import com.silent.ilustriscore.core.model.ViewModelBaseState

@Composable
fun NewStyleScreen(navController: NavController) {
    val viewModel = hiltViewModel<NewStyleViewModel>()
    val state = viewModel.viewModelState.observeAsState().value
    val style = viewModel.newStyle.observeAsState().value

    var gifBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    var currentOption by remember {
        mutableStateOf(FormOptions.values().first())
    }

    val context = LocalContext.current


    fun isAnimationEnabled() = currentOption == FormOptions.STYLE


    fun showGifDialog() {
        GiphySelectDialog(context) {
            viewModel.updateStyleBackground(it)
            gifBitmap = null
        }.show()
    }

    @Composable
    fun getOptionsView() {
        when (currentOption) {
            FormOptions.COLORS -> {
                ColorsOptions(
                    textColor = style?.textColor,
                    backgroundColor = style?.styleProperties?.backgroundColor,
                    updateTextColor = { viewModel.updateStyleColor(it) },
                    updateBackColor = { viewModel.updateStyleBackColor(it) }
                )
            }

            FormOptions.SHADOW -> {
                ShadowOptions(shadowStyle = style?.shadowStyle) {
                    viewModel.updateShadowStyle(it)
                }
            }

            FormOptions.TEXT -> {
                TextOptions(textProperties = style?.textProperties) {
                    viewModel.updateTextProperties(it)
                }
            }

            FormOptions.STYLE -> {
                StyleOptions(
                    animationProperties = style?.animationProperties,
                    styleProperties = style?.styleProperties,
                    {
                        viewModel.updateAnimationProperties(it)
                    }) {
                    viewModel.updateStyleProperties(it)
                }
            }
        }
    }

    LazyColumn {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "Novo estilo",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = { style?.let { viewModel.saveData(it) } },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialColor.Blue500)
                ) {
                    Text(text = "Salvar", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .clickable {
                        showGifDialog()
                    }
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            ) {
                AnimatedContent(targetState = style, transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }, label = "Style animation") {
                    val quoteDataModel = QuoteDataModel(
                        quoteBean = Quote(
                            author = "Sunny",
                            quote = StyleUtils.getExampleQuote()
                        ),
                        user = null,
                        style = it ?: Style.fallbackStyle
                    )
                    BuildQuoteWindow(
                        quoteDataModel = quoteDataModel,
                        animationEnabled = isAnimationEnabled()
                    )
                }
            }
        }

        stickyHeader {
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                items(FormOptions.values().size) {
                    val option = FormOptions.values()[it]
                    val isSelected = option == currentOption

                    IconButton(
                        onClick = {
                            currentOption = option
                        }, modifier = Modifier
                            .padding(8.dp)
                            .background(
                                brush = if (isSelected) managerGradient() else grayGradients(),
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = CircleShape
                            )
                    ) {
                        val itemColor =
                            animateColorAsState(targetValue = if (isSelected) MaterialColor.White else MaterialTheme.colorScheme.onBackground)
                        Icon(
                            painter = painterResource(id = option.icon),
                            contentDescription = null,
                            tint = itemColor.value
                        )
                    }
                }
            }
        }

        item {
            getOptionsView()
        }
    }

    LaunchedEffect(state) {
        if (state is ViewModelBaseState.DataSavedState) {
            navController.popBackStack()
        }
    }

}


@Composable
fun ColorsOptions(
    textColor: String?,
    backgroundColor: String?, updateTextColor: (String) -> Unit, updateBackColor: (String) -> Unit
) {
    val context = LocalContext.current
    val colors = ColorUtils.getMaterialColors(context)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Ajustes de cor", style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Text(
            text = "Cor do texto",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        ColorList(
            colors = colors.filter { it != backgroundColor },
            currentColor = textColor,
            onPickColor = updateTextColor
        )
        Text(
            text = "Cor de fundo",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        ColorList(
            colors = colors.filter { it != textColor },
            currentColor = backgroundColor,
            onPickColor = updateBackColor
        )

    }

}

@Composable
fun ColorList(colors: List<String>, currentColor: String?, onPickColor: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .animateContentSize(tween(1000, delayMillis = 500, easing = EaseIn))
    ) {
        items(colors.size) {
            ColorIcon(
                color = colors[it],
                isSelected = currentColor == colors[it],
                onSelectColor = { selectedColor ->
                    onPickColor(selectedColor)
                })
        }
    }
}

@Composable
fun ColorIcon(color: String, isSelected: Boolean, onSelectColor: (String) -> Unit) {
    val borderColor =
        animateColorAsState(
            animationSpec = tween(1000, easing = EaseIn), targetValue =
            if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
        )
    val intColor = Color(android.graphics.Color.parseColor(color))
    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(width = 1.dp, shape = CircleShape, color = borderColor.value)
            .clickable {
                onSelectColor(color)
            }
            .padding(4.dp)
            .background(intColor, shape = CircleShape)
            .size(24.dp)
            .padding(4.dp)
    )
}

@Composable
fun ShadowOptions(shadowStyle: ShadowStyle?, updateShadowStyle: (ShadowStyle) -> Unit) {
    val context = LocalContext.current
    val colors = ColorUtils.getMaterialColors(context)
    var sliderX by remember { mutableFloatStateOf(shadowStyle?.dx ?: 0f) }
    var sliderY by remember { mutableFloatStateOf(shadowStyle?.dy ?: 0f) }
    var sliderBlur by remember { mutableFloatStateOf(shadowStyle?.radius ?: 0f) }
    val positionRange = -100f..100f
    val blurRange = 0f..50f

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Ajustes de sombra",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text(
            text = "Cor da sombra",
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light
        )

        ColorList(colors = colors, currentColor = shadowStyle?.shadowColor, onPickColor = {
            updateShadowStyle(shadowStyle?.copy(shadowColor = it) ?: ShadowStyle(shadowColor = it))
        })

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Horizontal",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light
            )

            Slider(
                value = sliderX,
                onValueChange = {
                    sliderX = it
                },
                onValueChangeFinished = {
                    updateShadowStyle(shadowStyle?.copy(dx = sliderX) ?: ShadowStyle(dx = sliderX))
                },
                valueRange = positionRange,
                steps = 100,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .gradientFill(managerGradient())
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Vertical",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light
            )

            Slider(
                value = sliderY,
                onValueChange = {
                    sliderY = it
                },
                onValueChangeFinished = {
                    updateShadowStyle(shadowStyle?.copy(dy = sliderY) ?: ShadowStyle(dy = sliderY))
                },
                valueRange = positionRange,
                steps = 100,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .gradientFill(managerGradient())
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Desfoque",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light
            )

            Slider(
                value = sliderBlur,
                onValueChange = {
                    sliderBlur = it
                },
                onValueChangeFinished = {
                    updateShadowStyle(
                        shadowStyle?.copy(radius = sliderBlur) ?: ShadowStyle(radius = sliderBlur)
                    )
                },
                valueRange = blurRange,
                steps = 100,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .gradientFill(managerGradient())
            )
        }
    }
}

@Composable
fun TextOptions(textProperties: TextProperties?, updateTextProperties: (TextProperties) -> Unit) {
    val context = LocalContext.current
    val fonts = FontUtils.getFamilies(context)
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Configurações de texto", style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text(
            text = "Ajustes de fonte", style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            val styles = FontStyle.values()
            items(styles.size) {
                val isSelected = textProperties?.fontStyle == styles[it]
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape),
                    onClick = {
                        updateTextProperties(
                            textProperties?.copy(fontStyle = styles[it]) ?: TextProperties(
                                fontStyle = styles[it]
                            )
                        )
                    }) {

                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = styles[it].getIconForFontStyle()),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .gradientFill(
                                if (isSelected) managerGradient() else textColorGradient()
                            )
                    )

                }
            }
        }

        Text(
            text = "Ajustes de alinhamento", style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            val alignments = TextAlignment.values()
            items(alignments.size) {
                val isSelected = textProperties?.textAlignment == alignments[it]
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape),
                    onClick = {
                        updateTextProperties(
                            textProperties?.copy(textAlignment = alignments[it]) ?: TextProperties(
                                textAlignment = alignments[it]
                            )
                        )
                    }) {

                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = alignments[it].getIconForAlignment()),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .gradientFill(
                                if (isSelected) managerGradient() else textColorGradient()
                            )
                    )

                }
            }
        }

        Text(
            "Fontes",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            items(fonts.size) {
                val font = fonts[it]
                FontCard(fontName = font, textProperties?.fontFamily == font) {
                    updateTextProperties(
                        textProperties?.copy(fontFamily = font) ?: TextProperties(fontFamily = font)
                    )
                }
            }
        }
    }

}

@Composable
fun StyleOptions(
    animationProperties: AnimationProperties?,
    styleProperties: StyleProperties?,
    updateAnimationProperties: (AnimationProperties) -> Unit,
    updateStyleProperties: (StyleProperties) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Configurações de estilo", style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text(
            text = "Animação do texto", modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        LazyRow {
            items(AnimationOptions.values().size) {
                val animation = AnimationOptions.values()[it]
                val isSelected = animationProperties?.animation == animation
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape),
                    onClick = {
                        updateAnimationProperties(
                            animationProperties?.copy(animation = animation) ?: AnimationProperties(
                                animation = animation
                            )
                        )
                    }) {

                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = animation.getIconForAnimation()),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .gradientFill(
                                if (isSelected) managerGradient() else textColorGradient()
                            )
                    )
                }
            }
        }

        Text(
            text = "Tipo de animação", modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        LazyRow {
            items(AnimationTransition.values().size) {
                val transition = AnimationTransition.values()[it]
                val isSelected = animationProperties?.transition == transition
                Text(
                    text = transition.title,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(
                            MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                defaultRadius
                            )
                        )
                        .padding(8.dp)
                        .gradientFill(if (isSelected) managerGradient() else textColorGradient())
                        .clickable {
                            updateAnimationProperties(
                                animationProperties?.copy(transition = transition)
                                    ?: AnimationProperties(
                                        transition = transition
                                    )
                            )
                        }
                )
            }
        }


        Text(
            text = "Ajuste de bordas", modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        LazyRow {
            items(Window.values().size) {
                val window = Window.values()[it]
                val isSelected = styleProperties?.customWindow == window
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape),
                    onClick = {
                        updateStyleProperties(
                            styleProperties?.copy(customWindow = window) ?: StyleProperties(
                                customWindow = window
                            )
                        )
                    }) {

                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = window.getIconForWindow()),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .gradientFill(
                                if (isSelected) managerGradient() else textColorGradient()
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun FontCard(fontName: String, isSelected: Boolean, onSelectFont: (String) -> Unit) {
    val font = FontUtils.getFontFamily(fontName)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(0.dp)
            )
            .gradientFill(if (isSelected) managerGradient() else textColorGradient())
            .padding(8.dp)
            .clickable {
                onSelectFont(fontName)
            }) {
        Text(
            text = "Mm",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = font,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = fontName,
            modifier = Modifier.padding(vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }

}

enum class FormOptions(val icon: Int = com.creat.motiv.base.R.drawable.ic_saturn_and_other_planets_primary) {
    COLORS(R.drawable.ic_color_pallete_24),
    SHADOW(R.drawable.ic_shadow_24),
    TEXT(R.drawable.ic_text_selection_24),
    STYLE(R.drawable.ic_style_24)
}

fun FontStyle.getIconForFontStyle(): Int {
    return when (this) {
        FontStyle.REGULAR -> R.drawable.ic_text_24
        FontStyle.ITALIC -> R.drawable.ic_italic_24
        FontStyle.BOLD -> R.drawable.ic_bold_24
        FontStyle.BLACK -> R.drawable.ic_extra_bold_24
    }
}

fun TextAlignment.getIconForAlignment(): Int {
    return when (this) {
        TextAlignment.CENTER -> R.drawable.ic_round_format_align_center_24
        TextAlignment.START -> R.drawable.ic_round_format_align_left_24
        TextAlignment.END -> R.drawable.ic_round_format_align_right_24
        TextAlignment.JUSTIFY -> R.drawable.ic_round_format_align_justify_24
    }
}

fun AnimationOptions.getIconForAnimation(): Int {
    return when (this) {
        AnimationOptions.TYPE -> R.drawable.ic_text_input
        AnimationOptions.FADE -> R.drawable.ic_shadows
        AnimationOptions.SCALE -> R.drawable.ic_scale_24
    }
}

fun Window.getIconForWindow(): Int {
    return when (this) {
        Window.CLASSIC -> R.drawable.ic_retro
        Window.MODERN -> R.drawable.ic_modern
        else -> com.creat.motiv.base.R.drawable.ic_saturn_and_other_planets_primary
    }
}

fun BlendMode.getIconForBlend(): Int {
    return when (this) {
        BlendMode.NORMAL -> R.drawable.ic_normal_24
        BlendMode.DARKEN -> R.drawable.ic_darken_24
        BlendMode.LIGHTEN -> R.drawable.ic_lighten_24
        BlendMode.OVERLAY -> R.drawable.ic_overlay_24
        BlendMode.SCREEN -> R.drawable.ic_screen_24
    }
}

