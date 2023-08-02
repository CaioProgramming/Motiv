package com.ilustris.motiv.manager.styles.ui

import ai.atick.material.MaterialColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.creat.motiv.manager.R
import com.ilustris.motiv.base.data.model.NEW_STYLE_BACKGROUND
import com.ilustris.motiv.base.data.model.Style
import com.ilustris.motiv.base.navigation.AppNavigation
import com.ilustris.motiv.base.ui.component.MotivLoader
import com.ilustris.motiv.manager.styles.presentation.StylesViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun StylesScreen(navController: NavController) {
    val viewmodel = hiltViewModel<StylesViewModel>()
    val state = viewmodel.viewModelState.observeAsState().value
    val columns = 2
    var selectedStyle by remember {
        mutableStateOf<Style?>(null)
    }

    fun saveNewStyle() {
        navController.navigate(AppNavigation.NEWSTYLE.route)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns), modifier = Modifier.animateContentSize(
            tween(1500)
        )
    ) {
        if (state == ViewModelBaseState.LoadingState || state is ViewModelBaseState.ErrorState) {
            item(span = { GridItemSpan(columns) }) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    MotivLoader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        message = if (state is ViewModelBaseState.ErrorState) state.dataException.code.message else null
                    )
                }
            }
        }
        if (state is ViewModelBaseState.DataListRetrievedState) {
            val styles = state.dataList as List<Style>

            item(span = { GridItemSpan(columns) }) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estilos",
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${styles.size} Estilos disponíveis para o app, adicione ou exclua estilos.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }

            item {
                Box {
                    GlideImage(
                        imageModel = { NEW_STYLE_BACKGROUND },
                        glideRequestType = GlideRequestType.GIF,
                        modifier = Modifier
                            .size(200.dp)
                            .border(
                                color = Color.Transparent,
                                width = 2.dp,
                            )
                            .clickable {
                                saveNewStyle()
                            }
                    )

                    Text(
                        text = "Criar novo estilo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialColor.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }

            }

            items(styles.size) {
                StyleCard(style = styles[it], onSelectStyle = { styleSelection ->
                    selectedStyle = styleSelection
                })

            }

        }
    }

    LaunchedEffect(Unit) {
        viewmodel.getAllData()
    }

    AnimatedVisibility(
        visible = selectedStyle != null,
        enter = scaleIn(tween(1500)),
        exit = fadeOut(tween(1000))
    ) {
        AlertDialog(modifier = Modifier.animateContentSize(),
            onDismissRequest = { selectedStyle = null },
            confirmButton = {
                Button(
                    onClick = {
                        selectedStyle?.id?.let {
                            viewmodel.deleteData(it)
                            selectedStyle = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = "Excluir")
                }
            }, dismissButton = {
                Button(
                    onClick = { selectedStyle = null },
                    colors = ButtonDefaults.textButtonColors()
                ) {
                    Text(text = "Cancelar")
                }
            }, title = {
                Text(
                    text = "Excluir estilo",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }, text = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.delete_style_message),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    selectedStyle?.let {
                        StyleCard(style = it, onSelectStyle = {})
                    } ?: MotivLoader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            })
    }

    LaunchedEffect(state) {
        if (state is ViewModelBaseState.DataDeletedState) {
            viewmodel.getAllData()
        }
    }
}