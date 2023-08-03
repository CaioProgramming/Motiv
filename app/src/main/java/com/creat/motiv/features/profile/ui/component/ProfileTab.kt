package com.creat.motiv.features.profile.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ilustris.motiv.foundation.ui.theme.defaultRadius

@Composable
fun ProfileTab(
    buttonText: String,
    isSelected: Boolean,
    rowColor: Color = MaterialTheme.colorScheme.primary,
    onclick: () -> Unit
) {
    ConstraintLayout {
        val (button, divider) = createRefs()
        val scaleAnimation = animateFloatAsState(targetValue = if (isSelected) 1f else 0.5f)
        val colorAnimation =
            animateColorAsState(targetValue = if (isSelected) rowColor else Color.Transparent)
        Text(
            text = buttonText,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .constrainAs(button) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable { onclick() }
                .clip(RoundedCornerShape(defaultRadius))
                .padding(16.dp)
                .alpha(scaleAnimation.value)

        )
        Divider(
            Modifier
                .constrainAs(divider) {
                    start.linkTo(button.start)
                    end.linkTo(button.end)
                    top.linkTo(button.bottom)
                    width = Dimension.fillToConstraints
                }
                .clip(RoundedCornerShape(defaultRadius)),
            color = colorAnimation.value,
            thickness = 2.dp
        )
    }

}