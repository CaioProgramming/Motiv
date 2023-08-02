@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)

package com.creat.motiv.features.home.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.creat.motiv.features.home.presentation.HomeViewModel
import com.creat.motiv.features.home.presentation.ShareState
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.navigation.AppNavigation
import com.ilustris.motiv.base.ui.component.QuoteCard
import com.ilustris.motiv.foundation.ui.component.ReportDialog
import com.ilustris.motiv.foundation.ui.presentation.QuoteActions
import com.ilustris.motiv.foundation.ui.theme.MotivTitle
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.quoteCardModifier

@Composable
fun HomeView(navController: NavController) {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val quotes = homeViewModel.quotes
    var query by remember {
        mutableStateOf("")
    }
    val shareState = homeViewModel.shareState.observeAsState().value
    val reportVisibility = remember {
        mutableStateOf(false)
    }
    val reportedQuote = remember {
        mutableStateOf<Quote?>(null)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val quoteActions = object : QuoteActions {
        override fun onClickUser(uid: String) {
            navController.navigate(AppNavigation.PROFILE.route.replace("{userId}", uid))
        }

        override fun onLike(dataModel: QuoteDataModel) {
            homeViewModel.likeQuote(dataModel)
        }

        override fun onShare(dataModel: QuoteDataModel, bitmap: Bitmap) {
            homeViewModel.handleShare(dataModel.quoteBean, bitmap)
        }

        override fun onDelete(dataModel: QuoteDataModel) {
            homeViewModel.deleteQuote(dataModel)
        }

        override fun onEdit(dataModel: QuoteDataModel) {
            navController.navigate(
                AppNavigation.POST.route.replace(
                    "{quoteId}",
                    dataModel.quoteBean.id
                )
            )
        }

        override fun onReport(dataModel: QuoteDataModel) {
            reportVisibility.value = true
            reportedQuote.value = dataModel.quoteBean
        }
    }
    var expandedSearch by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val pagerState = rememberPagerState {
            quotes.size
        }

        AnimatedContent(targetState = expandedSearch, label = "titleContent", transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .animateContentSize(tween(1000, easing = LinearOutSlowInEasing))
            ) {
                if (it) {
                    IconButton(onClick = {
                        expandedSearch = false
                    }) {
                        Icon(Icons.Rounded.Close, contentDescription = "fechar")
                    }
                    TextField(
                        value = query,
                        leadingIcon = {
                            Icon(Icons.Rounded.Search, contentDescription = "Pesquisar")
                        },
                        trailingIcon = {
                            this@Column.AnimatedVisibility(
                                visible = query.isNotEmpty(),
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut()
                            ) {
                                Icon(
                                    Icons.Sharp.Close,
                                    contentDescription = "fechar",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                            query = ""
                                            homeViewModel.getAllData()
                                        }
                                )
                            }

                        },
                        onValueChange = {
                            query = it
                        },
                        placeholder = {
                            Text(
                                text = "Busque inspirações...",
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        shape = RoundedCornerShape(defaultRadius),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search,
                            autoCorrect = true
                        ),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            homeViewModel.searchQuote(query)
                        }),
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )

                } else {
                    MotivTitle()
                    IconButton(onClick = {
                        expandedSearch = !expandedSearch
                    }) {
                        Icon(Icons.Rounded.Search, contentDescription = "Pesquisar")
                    }
                }
            }
        }

        AnimatedContent(
            targetState = quotes.isNotEmpty(),
            label = "quoteContent",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }) {
            if (it) {
                VerticalPager(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .animateContentSize(tween(1500, easing = LinearOutSlowInEasing)),
                    state = pagerState,
                    pageSpacing = 8.dp,
                    userScrollEnabled = true,
                    pageContent = { index ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            QuoteCard(
                                quotes[index],
                                modifier = Modifier
                                    .wrapContentSize()
                                    .quoteCardModifier()
                                    .align(Alignment.Center),
                                quoteActions = quoteActions,
                            )
                        }


                    }
                )
            }

        }

        ReportDialog(visible = reportVisibility.value, reportFeedback = {
            reportedQuote.value?.let { quote ->
                homeViewModel.reportQuote(quote, it)
            }
            reportVisibility.value = false
        }) {
            reportVisibility.value = false
        }
    }

    val context = LocalContext.current

    fun launchShareActivity(uri: Uri, quote: Quote) {

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, context.contentResolver.getType(uri))
            putExtra(
                Intent.EXTRA_SUBJECT,
                context.resources.getString(com.creat.motiv.base.R.string.app_name)
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "${quote.quote}\n - ${quote.author}"
            )
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "Compartilhar post em..."
            )
        )
    }

    LaunchedEffect(Unit) {
        homeViewModel.getAllData()
    }

    LaunchedEffect(shareState) {
        shareState?.let {
            if (it is ShareState.ShareSuccess) {
                launchShareActivity(it.uri, it.quote)
            }
        }
    }

}