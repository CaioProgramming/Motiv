@file:OptIn(
    ExperimentalAnimationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package com.ilustris.motiv.manager.radios.ui

import ai.atick.material.MaterialColor
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.creat.motiv.manager.R
import com.ilustris.motiv.base.data.model.Radio
import com.ilustris.motiv.base.ui.component.MotivLoader
import com.ilustris.motiv.foundation.ui.component.CardBackground
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.managerBrushes
import com.ilustris.motiv.foundation.ui.theme.managerGradient
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.ilustris.motiv.manager.giphy.GiphySelectDialog
import com.ilustris.motiv.manager.radios.presentation.RadioManagerViewModel
import com.silent.ilustriscore.core.contract.DataError
import com.silent.ilustriscore.core.model.ViewModelBaseState
import java.io.File

@Composable
fun RadiosScreen() {
    val radioViewModel = hiltViewModel<RadioManagerViewModel>()
    val radioViewModelState = radioViewModel.viewModelState.observeAsState().value
    var context = LocalContext.current

    val saveNewRadio = remember {
        mutableStateOf(false)
    }

    var selectedRadio by remember {
        mutableStateOf<Radio?>(null)
    }

    fun addNewRadio() {
        saveNewRadio.value = true
    }

    fun deleteRadio(radio: Radio) {
        selectedRadio = radio
    }


    AnimatedContent(targetState = radioViewModelState, transitionSpec = {
        fadeIn() togetherWith fadeOut()
    }, label = "RadioContent") { state ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            if (state == ViewModelBaseState.LoadingState) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    MotivLoader(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            if (state is ViewModelBaseState.DataListRetrievedState) {
                val radioList = state.dataList as List<Radio>
                val pagerState = rememberPagerState {
                    radioList.size
                }



                Text(
                    text = "Radios",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${radioList.size} radios disponíveis no app, adicione ou exclua-os.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (pager, actions) = createRefs()
                    HorizontalPager(state = pagerState, modifier = Modifier.constrainAs(pager) {
                        top.linkTo(parent.top)
                        bottom.linkTo(actions.top)
                        width = Dimension.matchParent
                        height = Dimension.fillToConstraints
                    }) {
                        var radioBitmap by remember {
                            mutableStateOf<ImageBitmap?>(null)
                        }
                        val radioColors =
                            radioBitmap?.asAndroidBitmap()?.paletteFromBitMap()?.colorsFromPalette()
                        val radioBrush = gradientAnimation(radioColors ?: managerBrushes())

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val radio = radioList[it]
                            CardBackground(
                                modifier = Modifier.radioIconModifier(0f, 300.dp, radioBrush, 4.dp),
                                backgroundImage = radio.visualizer,
                                loadedBitmap = {
                                    if (radioBitmap == null) {
                                        radioBitmap = it
                                    }
                                })

                        }
                    }
                    val currentRadio = radioList[pagerState.currentPage]

                    Row(
                        modifier = Modifier.constrainAs(actions) {
                            bottom.linkTo(parent.bottom)
                            width = Dimension.matchParent
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            deleteRadio(radioList[pagerState.currentPage])
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete_round_outline_24),
                                contentDescription = "excluir radio",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Text(
                            text = currentRadio.name,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.gradientFill(gradientAnimation(managerBrushes()))
                        )

                        IconButton(
                            onClick = {
                                addNewRadio()
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = "Adicionar radio",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }


                    }

                }

            }

            if (state is ViewModelBaseState.ErrorState) {
                MotivLoader(modifier = Modifier.size(100.dp))
                val stateMessage = state.dataException.message

                Text(
                    text = stateMessage,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Light
                )

                if (state.dataException == DataError.NotFound) {

                    Button(
                        onClick = {
                            addNewRadio()
                        },
                        shape = RoundedCornerShape(defaultRadius),
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(text = "Adicionar nova radio")
                    }
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        radioViewModel.getAllData()
    }

    LaunchedEffect(radioViewModelState) {
        when (radioViewModelState) {
            is ViewModelBaseState.DataSavedState, ViewModelBaseState.DataDeletedState -> {
                radioViewModel.getAllData()
            }

            ViewModelBaseState.LoadingState -> {
                selectedRadio = null
                saveNewRadio.value = false
            }

            else -> {

            }
        }
    }

    AnimatedVisibility(visible = saveNewRadio.value, enter = fadeIn(), exit = fadeOut()) {

        var radioName by remember {
            mutableStateOf("")
        }
        var selectedGif by remember {
            mutableStateOf("")
        }
        var musicFile by remember {
            mutableStateOf<Uri?>(null)
        }


        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            musicFile = uri
        }


        fun showGiphyDialog() {
            GiphySelectDialog(context) {
                selectedGif = it
            }.show()
        }


        AlertDialog(onDismissRequest = { saveNewRadio.value = false },
            confirmButton = {
                Button(onClick = {
                    musicFile?.let {
                        radioViewModel.saveData(
                            Radio(
                                name = radioName,
                                visualizer = selectedGif,
                                url = musicFile.toString()
                            )
                        )
                        saveNewRadio.value = false
                        musicFile = null
                        selectedGif = ""
                    }

                }, enabled = musicFile != null) {
                    Text(text = "Salvar")
                }

            },
            title = {
                Text(text = "Adicionar nova radio")
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    CardBackground(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(CircleShape)
                            .radioIconModifier(0f, 100.dp, managerGradient(), 4.dp)
                            .clickable {
                                showGiphyDialog()
                            },
                        backgroundImage = selectedGif,
                        loadedBitmap = {}
                    )

                    TextField(
                        value = radioName,
                        onValueChange = {
                            radioName = it
                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(defaultRadius),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        )
                    )

                    AnimatedContent(targetState = musicFile, transitionSpec = {
                        scaleIn() togetherWith scaleOut()
                    }, label = "MusicContent") {
                        Button(
                            onClick = {
                                galleryLauncher.launch("audio/*")
                            },
                            shape = RoundedCornerShape(defaultRadius),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (it == null) MaterialTheme.colorScheme.background.copy(
                                    alpha = 0.6f
                                ) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_music_note),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    contentDescription = null
                                )
                                val textButton = it?.getFileName() ?: "Selecione uma música"
                                Text(
                                    text = textButton,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    maxLines = 2
                                )
                            }

                        }
                    }


                }
            })
    }

    AnimatedVisibility(visible = selectedRadio != null, enter = fadeIn(), exit = fadeOut()) {

        AlertDialog(onDismissRequest = {
            selectedRadio = null
        },
            confirmButton = {
                Button(
                    onClick = {
                        selectedRadio?.let {
                            radioViewModel.deleteData(it.id)
                            selectedRadio = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialColor.Red500,
                        contentColor = MaterialColor.White
                    )
                ) {
                    Text(text = "Excluir")
                }
            }, dismissButton = {
                Button(
                    onClick = { selectedRadio = null },
                    colors = ButtonDefaults.textButtonColors()
                ) {
                    Text(text = "Cancelar")
                }
            }, title = { Text(text = "Excluir radio") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {


                    selectedRadio?.let {
                        CardBackground(
                            modifier = Modifier.radioIconModifier(
                                0f,
                                100.dp,
                                managerGradient(),
                                4.dp
                            ),
                            backgroundImage = it.visualizer,
                            loadedBitmap = {
                            })

                        Text(
                            text = it.name, textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Não será possível recuperar esta radio após a exclusão.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } ?: run {
                        MotivLoader(modifier = Modifier.size(100.dp))
                    }

                }
            })
    }

}


private fun Uri.getFileName(): String? {
    return try {
        val file = File(this.path ?: "")
        file.name
    } catch (e: Exception) {
        e.printStackTrace()
        "unknown"
    }

}