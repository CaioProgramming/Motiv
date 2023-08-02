@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.creat.motiv.features.settings.ui

import ai.atick.material.MaterialColor
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.creat.motiv.MainActivity
import com.creat.motiv.R
import com.creat.motiv.features.settings.presentation.SettingsViewModel
import com.ilustris.motiv.base.navigation.AppNavigation
import com.ilustris.motiv.base.ui.component.CoverSheet
import com.ilustris.motiv.base.ui.component.IconSheet
import com.ilustris.motiv.foundation.ui.component.CardBackground
import com.ilustris.motiv.foundation.ui.theme.brushsFromPalette
import com.ilustris.motiv.foundation.ui.theme.colorsFromPalette
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.gradientFill
import com.ilustris.motiv.foundation.ui.theme.grayGradients
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.ilustris.motiv.foundation.ui.theme.textColorGradient
import com.ilustris.motivcompose.features.profile.ui.ProfileSheet
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.DateFormats
import com.silent.ilustriscore.core.utilities.format
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.glide.GlideRequestType
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun SettingsView(navController: NavController) {

    val viewModel: SettingsViewModel = hiltViewModel()
    val viewModelState = viewModel.viewModelState.observeAsState()
    val user = viewModel.user.observeAsState()
    val userMetadata = viewModel.userMetadata.observeAsState()
    var profileBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    var managerBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    var coverBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    val usernameText = remember { mutableStateOf(user.value?.name ?: "") }
    val borderBrush = profileBitmap?.asAndroidBitmap()?.paletteFromBitMap()?.brushsFromPalette()
        ?: grayGradients()


    val dialogState = remember {
        mutableStateOf(false)
    }

    val currentSheet = remember {
        mutableStateOf<ProfileSheet?>(null)
    }

    val coverBackColor = animateColorAsState(
        animationSpec = tween(1000),
        targetValue = coverBitmap?.asAndroidBitmap()?.paletteFromBitMap()?.colorsFromPalette()
            ?.first() ?: MaterialTheme.colorScheme.surface
    )

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    fun hideBottomSheet() {
        coroutineScope.launch {
            currentSheet.value = null
            bottomSheetState.hide()
        }
    }

    fun showSheet(sheet: ProfileSheet) {
        coroutineScope.launch {
            currentSheet.value = sheet
            bottomSheetState.show()
        }
    }


    @Composable
    fun getSheet() = currentSheet.value?.let {
        if (currentSheet.value == ProfileSheet.ICONS) IconSheet(user.value?.picurl) {
            viewModel.updateUserIcon(it)
            hideBottomSheet()
        } else {
            CoverSheet {
                viewModel.updateUserCover(it)
                hideBottomSheet()
            }
        }

    }


    AnimatedVisibility(
        visible = user.value != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val avatarSize = 150.dp
        val rowTextStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        val iconModifier = Modifier
            .size(24.dp)
            .alpha(0.6f)
        val textPadding = 8.dp
        val rowPadding = 16.dp

        ModalBottomSheetLayout(
            sheetContent = { getSheet() },
            sheetState = bottomSheetState,
            sheetBackgroundColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .fillMaxSize()
                .background(coverBackColor.value.copy(alpha = 0.7f))
        ) {
            LazyColumn(
                modifier = Modifier.wrapContentSize()
            ) {

                item {


                    Box(modifier = Modifier.fillMaxWidth()) {

                        CardBackground(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(avatarSize)
                                .clickable {
                                    showSheet(ProfileSheet.COVERS)
                                },
                            backgroundImage = user.value?.cover
                        ) {
                            if (coverBitmap == null) {
                                coverBitmap = it
                            }
                        }

                        GlideImage(
                            imageModel = { user.value?.picurl },
                            glideRequestType = GlideRequestType.BITMAP,
                            onImageStateChanged = {
                                if (it is GlideImageState.Success) {
                                    profileBitmap = it.imageBitmap
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 32.dp)
                                .radioIconModifier(0f, avatarSize, borderBrush, 4.dp)
                                .clickable {
                                    showSheet(ProfileSheet.ICONS)
                                }
                        )

                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)

                        ) {
                            Icon(Icons.Rounded.KeyboardArrowLeft, contentDescription = "Voltar")
                        }

                    }


                }

                item {
                    TextField(
                        value = usernameText.value,
                        onValueChange = {
                            usernameText.value = it
                        },
                        placeholder = {
                            Text(
                                text = user.value?.name ?: "Nome de usuário",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        trailingIcon = {
                            val nameText = usernameText.value
                            if (nameText.isNotEmpty() && nameText != user.value?.name) {
                                IconButton(
                                    colors = IconButtonDefaults.outlinedIconButtonColors(
                                        contentColor = MaterialColor.LightBlueA200
                                    ),
                                    onClick = {
                                        viewModel.updateUserName(usernameText.value)
                                    }
                                ) {
                                    Icon(Icons.Rounded.Check, contentDescription = "Salvar")
                                }
                            }

                        },
                        leadingIcon = { Box(modifier = Modifier.size(24.dp)) },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (usernameText.value.isNotEmpty() && usernameText.value != user.value?.name) {
                                    viewModel.updateUserName(usernameText.value)
                                }
                            }
                        ),
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            placeholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            containerColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.onBackground,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    Text(
                        text = userMetadata.value?.email ?: "",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light,
                    )
                    Text(
                        text = user.value?.uid ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Normal,
                    )

                }


                item {
                    userMetadata.value?.let {
                        if (it.admin) {
                            Button(
                                onClick = {
                                    navController.navigate(AppNavigation.MANAGER.route)
                                },
                                shape = RoundedCornerShape(defaultRadius),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                        alpha = 0.8f
                                    ),
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                ),
                                modifier = Modifier.padding(rowPadding)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.saturn_svgrepo_com),
                                        contentDescription = null,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Motiv +",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                shadow = Shadow(
                                                    MaterialTheme.colorScheme.onBackground,
                                                    Offset(0f, 3f),
                                                    10f
                                                )
                                            ),
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.padding(horizontal = textPadding)
                                        )

                                        Text(
                                            text = "Gerencie publicações, estilos e muito mais",
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.padding(horizontal = textPadding)
                                        )
                                    }

                                }
                            }

                        }
                    }

                }

                item {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(defaultRadius)
                            )
                            .background(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(
                                    defaultRadius
                                )
                            )
                            .fillMaxWidth()

                    ) {

                        fun getCreateDate(): String {
                            return try {
                                Calendar.getInstance().apply {
                                    timeInMillis = userMetadata.value!!.createTimeStamp!!
                                }.time.format(DateFormats.DD_OF_MM_FROM_YYYY)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                "-"
                            }
                        }

                        userMetadata.value?.let {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(rowPadding)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calendar_24),
                                    contentDescription = null,
                                    modifier = iconModifier
                                )
                                Text(
                                    text = "Criado em ${getCreateDate()}",
                                    style = rowTextStyle,
                                    modifier = Modifier.padding(horizontal = textPadding)
                                )
                            }

                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                thickness = 1.dp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(rowPadding)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_lock),
                                    contentDescription = null,
                                    modifier = iconModifier
                                )
                                Text(
                                    text = "Conectado via ${it.provider}",
                                    style = rowTextStyle,
                                    modifier = Modifier.padding(horizontal = textPadding),
                                )
                            }
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                thickness = 1.dp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                val icon = if (it.emailVerified) {
                                    R.drawable.ic_check_badge
                                } else {
                                    R.drawable.ic_warning_badge
                                }

                                val text = if (it.emailVerified) {
                                    "Email verificado"
                                } else {
                                    "Email não verificado"
                                }

                                Icon(
                                    painterResource(id = icon),
                                    tint = MaterialColor.Blue400,
                                    contentDescription = null,
                                    modifier = iconModifier
                                )
                                Text(
                                    text = text,
                                    style = rowTextStyle,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }

                }

                item {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        shape = RoundedCornerShape(defaultRadius),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            viewModel.logOut()
                            val activity = (context as? Activity)
                            context.startActivity(Intent(context, MainActivity::class.java))
                            activity?.finish()
                        }) {
                        Text(
                            text = "Desconectar",
                            textAlign = TextAlign.Center,
                            color = MaterialColor.Red800,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
                item {

                    Button(
                        onClick = { dialogState.value = true },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(defaultRadius),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Remover conta",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                item {

                    Image(
                        painter = painterResource(id = R.drawable.giphy_mark),
                        contentDescription = "Powered By Giphy",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .gradientFill(textColorGradient()),
                        contentScale = ContentScale.Inside
                    )

                }

                item {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        val context = LocalContext.current

                        fun openPlayStorePage() {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/dev?id=8106172357045720296")
                            )
                            context.startActivity(intent)
                        }

                        val ilustrisBrush = listOf(
                            MaterialColor.BlueA200,
                            MaterialColor.Blue800,
                            MaterialColor.BlueA700,
                        )

                        val gradientAnimation = gradientAnimation(ilustrisBrush)

                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .graphicsLayer(alpha = 0.99f)
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush = gradientAnimation,
                                            blendMode = BlendMode.SrcAtop
                                        )
                                    }
                                }
                                .clip(CircleShape)
                                .clickable {
                                    openPlayStorePage()
                                }
                        )
                    }
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

                    Text(
                        text = "Desenvolvido por ilustris em 2018 - $currentYear",
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    )
                }
            }

            AnimatedVisibility(
                visible = dialogState.value,
                enter = fadeIn(),
                exit = scaleOut()
            ) {
                AlertDialog(modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface, RoundedCornerShape(
                            defaultRadius
                        )
                    ),
                    shape = RoundedCornerShape(defaultRadius),
                    title = {
                        Text(text = "Tem certeza?")
                    },
                    text = {
                        Text(text = "Você não poderá recuperar sua conta depois de excluí-la.")
                    },
                    dismissButton = {
                        Text(text = "Cancelar", textAlign = TextAlign.Center, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                dialogState.value = false
                            }
                            .clip(RoundedCornerShape(defaultRadius))
                            .padding(8.dp))
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.deleteAccount()
                                dialogState.value = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(defaultRadius),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialColor.Red500,
                                contentColor = MaterialColor.White
                            )
                        ) {
                            Text(
                                text = "Excluir", textAlign = TextAlign.Center, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    ),
                    onDismissRequest = {
                        dialogState.value = false
                    })
            }
        }


    }

    LaunchedEffect(user) {
        usernameText.value = user.value?.name ?: ""
    }

    LaunchedEffect(Unit) {
        viewModel.fetchUser()
    }

    LaunchedEffect(viewModelState.value) {
        Log.i("SettingsState", "SettingsView: state updated -> ${viewModelState.value}")
        if (viewModelState.value is ViewModelBaseState.DataUpdateState) {
            navController.popBackStack()
        } else if (viewModelState.value == ViewModelBaseState.DataDeletedState) {
            val activity = (context as? Activity)
            context.startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }
    }
}