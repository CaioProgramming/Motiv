@file:OptIn(ExperimentalMaterial3Api::class)

package com.ilustris.motiv.foundation.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.creat.motiv.base.R
import com.ilustris.motiv.foundation.ui.theme.defaultRadius

@Composable
fun ReportDialog(visible: Boolean, reportFeedback: (String) -> Unit, dismissRequest: () -> Unit) {


    var reportReason by remember { mutableStateOf("") }

    val moonAnimation by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.moon)
    )

    val moonProgress by animateLottieCompositionAsState(
        moonAnimation,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )



    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = scaleOut()
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
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        LottieAnimation(
                            moonAnimation,
                            moonProgress,
                            modifier = Modifier
                                .matchParentSize()
                                .align(Alignment.Center)
                        )

                    }

                    Text(
                        text = "Encontrou algo inapropriado?",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )

                }
            },
            text = {
                LazyColumn {

                    item {
                        Text(
                            text = "Você pode reportar esse conteúdo para que nossa equipe avalie e tome as devidas providências.",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        TextField(
                            value = reportReason, onValueChange = {
                                if (it.length < 100) {
                                    reportReason = it
                                }
                            },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal
                            ),
                            placeholder = {
                                Text(
                                    text = "Comentários adicionais",
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                placeholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                containerColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                        defaultRadius
                                    )
                                )
                                .padding(8.dp)
                        )
                    }


                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        dismissRequest()
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
                        reportFeedback(reportReason)
                        dismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(defaultRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialColor.Red500,
                        contentColor = MaterialColor.White
                    )
                ) {
                    Text(
                        text = "Reportar", textAlign = TextAlign.Center, modifier = Modifier
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
                dismissRequest()
            })
    }

}