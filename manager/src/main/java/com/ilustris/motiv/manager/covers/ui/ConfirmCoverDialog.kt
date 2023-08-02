package com.ilustris.manager.feature.covers.ui

import ai.atick.material.MaterialColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.creat.motiv.manager.R
import com.ilustris.motiv.base.data.model.Cover
import com.ilustris.motiv.base.data.model.DEFAULT_USER_BACKGROUND
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun ConfirmCoverDialog(
    gifUrl: String? = null,
    saveNewCover: (String) -> Unit,
    dismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = gifUrl != null,
        enter = scaleIn(tween(1000)),
        exit = fadeOut(tween(500))
    ) {
        AlertDialog(modifier = Modifier
            .wrapContentSize()
            .background(
                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                    defaultRadius
                )
            ),
            shape = RoundedCornerShape(defaultRadius),
            title = {
                Text(
                    text = "Tem certeza?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )

            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlideImage(
                        imageModel = { gifUrl ?: DEFAULT_USER_BACKGROUND },
                        glideRequestType = GlideRequestType.GIF,
                        modifier = Modifier
                            .size(350.dp)
                            .border(
                                color = MaterialTheme.colorScheme.onBackground,
                                width = 2.dp
                            )
                    )
                    Text(
                        text = stringResource(R.string.save_cover_message),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        dismiss()
                    }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(defaultRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = "Cancelar", textAlign = TextAlign.Center, modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(defaultRadius))
                            .padding(8.dp)
                    )
                }

            },
            confirmButton = {
                Button(
                    onClick = {
                        gifUrl?.let { saveNewCover(it) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(defaultRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialColor.White
                    )
                ) {
                    Text(
                        text = "Salvar", textAlign = TextAlign.Center, modifier = Modifier
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
                dismiss()
            })
    }
}

@Composable
fun DeleteCoverDialog(cover: Cover? = null, deleteCover: (Cover) -> Unit, dismiss: () -> Unit) {
    AnimatedVisibility(
        visible = cover != null,
        enter = scaleIn(tween(1000)),
        exit = fadeOut(tween(500))
    ) {
        AlertDialog(modifier = Modifier
            .wrapContentSize()
            .background(
                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                    defaultRadius
                )
            ),
            shape = RoundedCornerShape(defaultRadius),
            title = {
                Text(
                    text = "Tem certeza?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )

            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlideImage(
                        imageModel = { cover?.url ?: DEFAULT_USER_BACKGROUND },
                        glideRequestType = GlideRequestType.GIF,
                        modifier = Modifier
                            .size(350.dp)
                            .border(
                                color = MaterialTheme.colorScheme.errorContainer,
                                width = 2.dp
                            )
                    )

                    Text(
                        text = stringResource(R.string.delete_cover_message),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        dismiss()
                    }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(defaultRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = "Cancelar", textAlign = TextAlign.Center, modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(defaultRadius))
                            .padding(8.dp)
                    )
                }

            },
            confirmButton = {
                Button(
                    onClick = {
                        cover?.let { deleteCover(it) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(defaultRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onError
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
                dismiss()
            })
    }
}