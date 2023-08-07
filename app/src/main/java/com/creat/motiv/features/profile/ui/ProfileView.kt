@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)

package com.ilustris.motivcompose.features.profile.ui

import ai.atick.material.MaterialColor
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.creat.motiv.features.home.presentation.ShareState
import com.creat.motiv.features.profile.presentation.ProfileViewModel
import com.creat.motiv.features.profile.ui.component.CounterLabel
import com.creat.motiv.features.profile.ui.component.ProfileTab
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.navigation.AppNavigation
import com.ilustris.motiv.base.ui.component.QuoteCard
import com.ilustris.motiv.foundation.ui.component.CardBackground
import com.ilustris.motiv.foundation.ui.component.ReportDialog
import com.ilustris.motiv.foundation.ui.presentation.QuoteActions
import com.ilustris.motiv.foundation.ui.theme.brushsFromPalette
import com.ilustris.motiv.foundation.ui.theme.colorFill
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.grayGradients
import com.ilustris.motiv.foundation.ui.theme.motivBrushes
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.ilustris.motiv.foundation.ui.theme.quoteCardModifier
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState

@Composable
fun ProfileView(userID: String? = null, navController: NavController) {

    val viewModel = hiltViewModel<ProfileViewModel>()
    val shareState = viewModel.shareState.observeAsState().value
    val context = LocalContext.current
    val user = viewModel.user.observeAsState().value
    val reportVisibility = remember {
        mutableStateOf(false)
    }
    val reportedQuote = remember {
        mutableStateOf<Quote?>(null)
    }
    var currentFilter by remember {
        mutableStateOf(ProfileFilter.POSTS)
    }
    val quoteActions = object : QuoteActions {
        override fun onClickUser(uid: String) {
            navController.navigate(AppNavigation.PROFILE.route.replace("{userId}", uid))
        }

        override fun onLike(dataModel: QuoteDataModel) {
            viewModel.likeQuote(dataModel, filter = currentFilter)
        }

        override fun onShare(dataModel: QuoteDataModel, bitmap: Bitmap) {
            viewModel.shareQuote(dataModel.quoteBean, bitmap)
        }

        override fun onDelete(dataModel: QuoteDataModel) {
            viewModel.deleteQuote(dataModel, currentFilter)
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

    val isOwnUser = viewModel.isOwnUser.observeAsState()
    val userQuotes =
        if (currentFilter == ProfileFilter.POSTS) viewModel.userQuotes else viewModel.userFavorites
    val listState = rememberLazyListState()
    var profileBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    var coverBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    fun canShowData() = userQuotes.isNotEmpty()

    val postsCount = viewModel.postsCount.value ?: 0
    val favoriteCount = viewModel.favoriteCount.value ?: 0
    val coverAlpha = animateFloatAsState(
        targetValue = if (canShowData()) 1f else 0f,
        tween(2000, delayMillis = 1500, easing = EaseIn)
    )
    val coverBackColor = animateColorAsState(
        animationSpec = tween(1000),
        targetValue = coverBitmap?.asAndroidBitmap()?.paletteFromBitMap()?.colorsFromPalette()
            ?.first() ?: MaterialTheme.colorScheme.surface
    )


    fun showUserTitle() = listState.firstVisibleItemIndex > 1

    val borderBrush = profileBitmap?.asAndroidBitmap()?.paletteFromBitMap()?.colorsFromPalette()
        ?: motivBrushes()

    val topBackColor = animateColorAsState(
        targetValue = if (showUserTitle()) MaterialTheme.colorScheme.background else Color.Transparent,
        tween(1000, easing = EaseIn, delayMillis = 100),
        label = "tabBackColors"
    )


    LaunchedEffect(userQuotes) {
        Log.i("Profile view", "ProfileView: showing ${userQuotes.size} quotes")
    }

    LaunchedEffect(Unit) {
        viewModel.fetchUser(userID?.replace("{userId}", ""))
    }

    LaunchedEffect(user) {
        if (user != null && postsCount == 0) {
            viewModel.getUserQuotes(user.uid)
            viewModel.getUserFavorites(user.uid)
        }
    }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                coverBitmap
                    ?.asAndroidBitmap()
                    ?.paletteFromBitMap()
                    ?.brushsFromPalette() ?: grayGradients()
            )
            .colorFill(MaterialColor.Black.copy(alpha = 0.8f))
            .alpha(coverAlpha.value)
    )
    LazyColumn(
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(tween(1500, easing = FastOutSlowInEasing))
    ) {

        if (user != null) {
            item {
                Box {
                    val avatarSize = 150.dp
                    CardBackground(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(avatarSize)
                            .alpha(coverAlpha.value),
                        backgroundImage = user.cover
                    ) {
                        if (coverBitmap == null) {
                            coverBitmap = it
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 16.dp, end = 16.dp, top = 75.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        GlideImage(
                            imageModel = { user.picurl },
                            onImageStateChanged = {
                                if (it is GlideImageState.Success) {
                                    profileBitmap = it.imageBitmap
                                }
                            },
                            modifier = Modifier
                                .radioIconModifier(
                                    0f,
                                    avatarSize,
                                    gradientAnimation(borderBrush),
                                    3.dp
                                )
                        )

                        Text(
                            text = user.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .alpha(coverAlpha.value)
                        )

                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(2.dp)
                                .alpha(coverAlpha.value),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            CounterLabel(label = "Publicações", count = postsCount)

                            Divider(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp)
                                    .clip(RoundedCornerShape(defaultRadius))
                                    .padding(horizontal = 4.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                thickness = 1.dp
                            )

                            CounterLabel(label = "Favoritos", count = favoriteCount)
                        }
                    }

                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .alpha(coverAlpha.value)
                    ) {
                        Icon(Icons.Rounded.KeyboardArrowLeft, contentDescription = "Voltar")
                    }

                    AnimatedVisibility(
                        visible = isOwnUser.value == true,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .alpha(coverAlpha.value)
                    ) {
                        IconButton(onClick = {
                            navController.navigate(AppNavigation.SETTINGS.route)
                        }) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Configurações")
                        }
                    }

                }
            }

            stickyHeader {

                AnimatedVisibility(
                    visible = canShowData() && showUserTitle(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = user.name, style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(topBackColor.value)
                            .padding(16.dp)
                            .gradientFill(gradientAnimation(borderBrush))
                    )
                }

                AnimatedVisibility(
                    visible = canShowData(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {


                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(topBackColor.value)

                    ) {

                        ProfileFilter.values().forEach {
                            ProfileTab(
                                buttonText = it.title,
                                rowColor = coverBackColor.value,
                                isSelected = it == currentFilter
                            ) {
                                currentFilter = it
                            }
                        }
                    }
                }

                Divider(
                    Modifier
                        .fillMaxWidth()
                        .alpha(coverAlpha.value),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    thickness = 1.dp
                )

            }
        }

        items(userQuotes.size) {
            AnimatedVisibility(
                visible = true,
                enter = scaleIn() + fadeIn(
                    tween(1500)
                ),
                exit = fadeOut()
            ) {
                QuoteCard(
                    loadAsGif = true,
                    animationEnabled = false,
                    quoteDataModel = userQuotes[it],
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .wrapContentSize()
                        .quoteCardModifier(),
                    quoteActions = quoteActions
                )
            }

        }

    }

    LaunchedEffect(shareState) {
        shareState.let {
            if (it is ShareState.ShareSuccess) {
                launchShareActivity(it.uri, it.quote)
            }
        }
    }

    ReportDialog(visible = reportVisibility.value, reportFeedback = {
        reportedQuote.value?.let { quote ->
            viewModel.reportQuote(quote, it)
        }
        reportVisibility.value = false
    }, dismissRequest = {
        reportVisibility.value = false
    })


    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect {
                Log.i("ListState", "ProfileView: first visible item $it")
            }
    }


}

enum class ProfileSheet {
    ICONS, COVERS
}

enum class ProfileFilter(val title: String) {
    POSTS("Posts"), FAVORITES("Favoritos")
}
