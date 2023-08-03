package com.ilustris.motiv.base.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.base.data.model.Cover
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun CoverView(cover: Cover, onSelection: (Cover) -> Unit) {
    GlideImage(
        imageModel = { cover.url },
        glideRequestType = GlideRequestType.GIF,
        modifier = Modifier
            .size(200.dp)
            .border(color = MaterialTheme.colorScheme.onBackground, width = 2.dp)
            .clickable {
                onSelection(cover)
            }
    )
}