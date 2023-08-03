package com.ilustris.motivcompose.features.radio.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ilustris.motiv.base.data.model.Radio
import com.ilustris.motiv.foundation.ui.theme.brushsFromPalette
import com.ilustris.motiv.foundation.ui.theme.grayGradients
import com.ilustris.motiv.foundation.ui.theme.paletteFromBitMap
import com.ilustris.motiv.foundation.ui.theme.radioRadius

@Composable
fun RadioListItem(radio: Radio, onClickRadio: (Radio) -> Unit) {

    var gifBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    var brush =
        if (gifBitmap.value == null) grayGradients() else gifBitmap.value!!.paletteFromBitMap()
            .brushsFromPalette()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(radioRadius))
            .clickable {
                onClickRadio(radio)
            },
    ) {


        Text(
            text = radio.name,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }

}