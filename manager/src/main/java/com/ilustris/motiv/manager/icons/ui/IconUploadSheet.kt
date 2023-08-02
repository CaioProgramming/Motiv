@file:OptIn(ExperimentalFoundationApi::class)

package com.ilustris.manager.feature.icons.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.gradientAnimation
import com.ilustris.motiv.foundation.ui.theme.managerBrushes
import com.ilustris.motiv.foundation.ui.theme.radioIconModifier
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState

@Composable
fun IconUploadSheet(saveIcon: (Uri) -> Unit) {

    var iconImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            iconImage = it
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Faça upload de um novo ícone",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        AnimatedVisibility(visible = iconImage == null) {
            IconButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .radioIconModifier(0f, 64.dp, gradientAnimation(managerBrushes()))
                    .padding(16.dp)
            ) {
                val infiniteTransition = rememberInfiniteTransition()
                val scaleAnimation = infiniteTransition.animateFloat(
                    initialValue = 0.8f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        tween(1000, easing = EaseInBounce),
                        RepeatMode.Reverse
                    )
                )
                Icon(
                    Icons.Rounded.KeyboardArrowUp,
                    contentDescription = "enviar novo icone",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.scale(scaleAnimation.value)
                )
            }
        }

        AnimatedVisibility(visible = iconImage != null, enter = scaleIn(), exit = fadeOut()) {
            val loadedImage = remember { mutableStateOf(false) }
            val alphaAnimation = animateFloatAsState(
                targetValue = if (loadedImage.value) 1f else 0f,
                tween(1000)
            )
            GlideImage(
                imageModel = { iconImage },
                onImageStateChanged = {
                    loadedImage.value = it is GlideImageState.Success
                },
                modifier = Modifier
                    .padding(8.dp)
                    .alpha(alphaAnimation.value)
                    .radioIconModifier(
                        0f,
                        200.dp,
                        gradientAnimation(managerBrushes()),
                        2.dp
                    )
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            iconImage = null
                        }
                    )
            )
        }

        AnimatedVisibility(
            visible = iconImage != null,
            enter = slideInVertically(),
            exit = fadeOut()
        ) {
            Button(
                onClick = {
                    iconImage?.let {
                        saveIcon(it)
                        iconImage = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(defaultRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Salvar",
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }


    }
}