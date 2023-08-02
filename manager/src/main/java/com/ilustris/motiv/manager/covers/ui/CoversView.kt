@file:OptIn(ExperimentalMaterialApi::class)

package com.ilustris.manager.feature.covers.ui

import ai.atick.material.MaterialColor
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.creat.motiv.manager.R
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.ilustris.motiv.base.data.model.Cover
import com.ilustris.motiv.base.data.model.DEFAULT_USER_BACKGROUND
import com.ilustris.motiv.base.ui.component.CoverView
import com.ilustris.motiv.base.ui.component.MotivLoader
import com.ilustris.motiv.manager.covers.presentation.CoversViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun CoversScreen() {
    val context = LocalContext.current
    val coversViewModel = hiltViewModel<CoversViewModel>()
    val state = coversViewModel.viewModelState.observeAsState().value
    val columnsCount = 2
    var savedGif by remember {
        mutableStateOf<String?>(null)
    }
    var selectedCover by remember {
        mutableStateOf<Cover?>(null)
    }

    fun showGifDialog() {
        val activity = context as AppCompatActivity
        val fragmentManager = activity.supportFragmentManager
        val giphyKey = context.getString(R.string.giphy_api)
        Giphy.configure(context, context.getString(R.string.giphy_api), true)
        val settings = GPHSettings(
            theme = GPHTheme.Automatic,
            mediaTypeConfig = arrayOf(GPHContentType.gif),
            stickerColumnCount = 2,
            selectedContentType = GPHContentType.gif
        )
        GiphyDialogFragment.newInstance(settings, giphyKey)
            .apply {
                gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                    override fun didSearchTerm(term: String) {}

                    override fun onDismissed(selectedContentType: GPHContentType) {}

                    override fun onGifSelected(
                        media: Media,
                        searchTerm: String?,
                        selectedContentType: GPHContentType
                    ) {
                        media.images.downsizedMedium?.gifUrl?.let { url ->
                            savedGif = url
                        }
                    }

                }
            }
            .show(fragmentManager, GiphyDialogFragment::class.java.simpleName)
    }

    fun saveNewCover() {
        showGifDialog()
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.animateContentSize(tween(1000, easing = EaseIn))
    ) {

        item(span = { GridItemSpan(columnsCount) }) {
            AnimatedVisibility(
                visible = state == ViewModelBaseState.LoadingState, enter = fadeIn(
                    tween(1000)
                ), exit = scaleOut(tween(1000))
            ) {
                MotivLoader(modifier = Modifier.size(100.dp))
            }
        }

        if (state is ViewModelBaseState.DataListRetrievedState) {
            val covers = state.dataList as List<Cover>

            item(span = { GridItemSpan(columnsCount) }) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Capas de perfil",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(text = "${covers.size} capas disponíveis para os usuários, adicione ou remova capas para que os usuários possam escolher.")
                }
            }

            item {
                Box {
                    GlideImage(
                        imageModel = { DEFAULT_USER_BACKGROUND },
                        glideRequestType = GlideRequestType.GIF,
                        modifier = Modifier
                            .size(200.dp)
                            .border(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                                width = 2.dp
                            )
                            .clickable {
                                saveNewCover()
                            }
                    )

                    Text(
                        text = "Adicionar nova capa",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialColor.White.copy(alpha = 0.5f),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            items(state.dataList.size) { index ->
                CoverView(covers[index]) {
                    selectedCover = it
                }
            }
        }
    }

    ConfirmCoverDialog(savedGif, saveNewCover = {
        coversViewModel.saveData(Cover(url = it))
    }) {
        savedGif = null
    }

    DeleteCoverDialog(selectedCover, deleteCover = {
        coversViewModel.deleteData(it.id)
    }) {
        selectedCover = null
    }

    LaunchedEffect(Unit) {
        coversViewModel.getAllData()
    }

    LaunchedEffect(state) {
        when (state) {
            is ViewModelBaseState.DataSavedState,
            is ViewModelBaseState.ErrorState,
            ViewModelBaseState.DataDeletedState,
            -> {
                coversViewModel.getAllData()
                savedGif = null
                selectedCover = null
            }

            else -> {}
        }
    }

}