package com.creat.motiv.features.profile.ui.component

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CounterLabel(label: String, count: Int) {

    val countAnimation =
        animateIntAsState(targetValue = count, tween(1500, easing = FastOutLinearInEasing))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .graphicsLayer(alpha = 0.6f)
        )

        Text(
            text = countAnimation.value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black
        )

    }
}