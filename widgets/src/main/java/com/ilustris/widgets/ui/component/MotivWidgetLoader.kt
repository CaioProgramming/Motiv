package com.ilustris.widgets.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.color.ColorProvider
import androidx.glance.layout.padding
import androidx.glance.layout.size

@Composable
fun MotivWidgetLoader() {

    CircularProgressIndicator(
        modifier = GlanceModifier.size(50.dp).padding(8.dp),
        color = ColorProvider(
            day = MaterialColor.DeepPurpleA100,
            night = MaterialColor.DeepPurpleA400
        )
    )


}