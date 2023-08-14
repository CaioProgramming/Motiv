@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)

package com.creat.motiv.features.post.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import com.creat.motiv.features.post.presentation.NewQuoteViewModel
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.data.model.User
import com.ilustris.motiv.base.ui.component.StyleIcon
import com.ilustris.motiv.base.ui.component.window.StyleCardAction
import com.ilustris.motiv.base.utils.BuildQuoteWindow
import com.ilustris.motiv.base.utils.buildTextColor
import com.ilustris.motiv.foundation.ui.theme.radioRadius
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuotePostScreen(quoteId: String? = null, navController: NavController) {


    val newQuoteViewModel = hiltViewModel<NewQuoteViewModel>()
    val state = newQuoteViewModel.viewModelState.observeAsState().value

    fun isSaving() = state == ViewModelBaseState.LoadingState

    val quote = newQuoteViewModel.newQuote.observeAsState().value
    val user = newQuoteViewModel.user.observeAsState().value
    val styles = newQuoteViewModel.styles.observeAsState().value
    val currentStyle = newQuoteViewModel.currentStyle.observeAsState().value


    val isFocusing = remember { mutableStateOf(false) }
    var backgroundBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    var palette by remember {
        mutableStateOf<Palette?>(null)
    }

    var quoteText by remember {
        mutableStateOf("")
    }

    var authorText by remember {
        mutableStateOf("")
    }


    val rowState = rememberPagerState(pageCount = { styles?.size ?: 0 })
    val scope = rememberCoroutineScope()


    LaunchedEffect(rowState.currentPage) {
        styles?.let {
            if (it.isNotEmpty()) {
                newQuoteViewModel.updateStyle(it[rowState.currentPage].id)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (styles == null) {
            newQuoteViewModel.getStyles(quoteId != null)
        }
    }

    LaunchedEffect(backgroundBitmap) {
        backgroundBitmap?.let {
            palette = Palette.Builder(it.asAndroidBitmap()).generate()
        }
    }

    fun scrollToStyle(index: Int) {
        scope.launch {
            rowState.animateScrollToPage(index)
        }
    }

    LaunchedEffect(currentStyle) {
        backgroundBitmap = null
    }

    LaunchedEffect(state) {
        if (state is ViewModelBaseState.DataSavedState || state is ViewModelBaseState.DataUpdateState) {
            delayedFunction(2000) {
                navController.popBackStack()
            }
        } else if (state is ViewModelBaseState.DataRetrievedState) {
            val quote = state.data as Quote
            newQuoteViewModel.updateQuote(quote)
            delayedFunction(500) {
                styles?.let {
                    scrollToStyle(styles.indexOfFirst { it.id == quote.style })
                }
            }
        }
    }

    LaunchedEffect(quoteText) {
        if (quoteText.isNotEmpty()) {
            newQuoteViewModel.updateQuoteText(quoteText)
        }
    }

    LaunchedEffect(authorText) {
        if (authorText.isNotEmpty()) {
            newQuoteViewModel.updateQuoteAuthor(authorText)
        }
    }

    LaunchedEffect(quote) {
        quote?.let {
            if (it.id.isNotEmpty()) {
                quoteText = it.quote
                authorText = it.author
            }

        }
    }

    LaunchedEffect(styles) {
        styles?.let {
            Log.i("StylesList", "QuotePostView: ${styles.size} styles updated")
            if (styles.isNotEmpty()) {
                if (quoteId?.contains("{quoteId}") == true) {
                    scrollToStyle(Random.nextInt(0, styles.size))
                } else {
                    newQuoteViewModel.getSingleData(quoteId!!)
                }
            }
        }

    }


    val styleCardAction = object : StyleCardAction {
        override fun updateQuoteText(newText: String) {
            newQuoteViewModel.updateQuoteText(newText)
        }

        override fun updateQuoteAuthor(newAuthor: String) {
            newQuoteViewModel.updateQuoteAuthor(newAuthor)
        }

    }


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (quoteCard, stylesRow, message, saveButton) = createRefs()


        Box(modifier = Modifier
            .padding(16.dp)
            .constrainAs(quoteCard) {
                top.linkTo(parent.top)
                bottom.linkTo(saveButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }) {

            val quoteDataModel = QuoteDataModel(
                quoteBean = quote ?: Quote(),
                style = currentStyle ?: Style.fallbackStyle,
                user = User(user?.displayName ?: "", user?.photoUrl.toString()),
            )

            BuildQuoteWindow(
                quoteDataModel = quoteDataModel,
                loadAsGif = true,
                animationEnabled = false,
                action = styleCardAction
            )

        }


        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(stylesRow) {
                    bottom.linkTo(quoteCard.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(16.dp)
                .animateContentSize(tween(1500))
                .size(80.dp)
                .clip(CircleShape),
            visible = !styles.isNullOrEmpty(),
            enter = scaleIn(), exit = shrinkOut()
        ) {
            HorizontalPager(
                state = rowState,
                userScrollEnabled = !isSaving(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .animateContentSize(tween(1500)),
                pageContent = {
                    styles?.run {
                        val style = get(it)
                        StyleIcon(
                            style = style,
                            isSaving = state == ViewModelBaseState.LoadingState,
                        ) { selectedStyle ->
                            quote?.let { quote ->
                                newQuoteViewModel.saveData(quote)
                            }
                        }
                    }

                })
        }


        AnimatedVisibility(visible = state is ViewModelBaseState.DataSavedState || isSaving(),
            modifier = Modifier
                .constrainAs(message) {
                    bottom.linkTo(stylesRow.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(16.dp), enter = fadeIn(), exit = fadeOut()) {
            Text(
                "Salvando post...",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(currentStyle?.textColor.buildTextColor())
            )
        }



        Button(
            onClick = { quote?.let { newQuoteViewModel.saveData(it) } },
            modifier = Modifier
                .constrainAs(saveButton) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            contentPadding = PaddingValues(16.dp),
            shape = RoundedCornerShape(radioRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                "Publicar",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }

    }


}
