@file:OptIn(ExperimentalAnimationApi::class)

package com.ilustris.motiv.base.ui.component

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.ilustris.motiv.base.data.model.AnimationOptions
import com.ilustris.motiv.base.data.model.AnimationTransition
import com.ilustris.motiv.base.utils.getDelay
import com.silent.ilustriscore.core.utilities.delayedFunction
import kotlin.random.Random

@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier,
    textStyle: TextStyle,
    animationEnabled: Boolean = true,
    animationOption: AnimationOptions? = null,
    transitionMethod: AnimationTransition? = null,
    onCompleteType: (() -> Unit)? = null
) {

    val animation = animationOption ?: AnimationOptions.TYPE
    val transition = transitionMethod ?: AnimationTransition.LETTERS

    fun indexMultiplier(): List<Int> {
        return when (transition) {
            AnimationTransition.LETTERS -> {
                text.toCharArray().mapIndexed { index, c -> index }
            }

            AnimationTransition.WORDS -> {
                text.split("\\s+".toRegex()).mapIndexed { index, s -> index }
            }

            AnimationTransition.LINES -> {
                text.split("\r?\n|\r".toRegex()).mapIndexed { index, s -> index }
            }
        }.plus(text.length)
    }


    var style by remember { mutableStateOf(textStyle) }

    var textIndex by remember { mutableIntStateOf(0) }
    var listIndex by remember { mutableIntStateOf(0) }
    val indexList = indexMultiplier()

    val textPart = try {
        if (!animationEnabled) text else text.substring(0, textIndex)
    } catch (e: Exception) {
        text
    }

    fun isAnimationComplete() = textPart == text || !animationEnabled

    LaunchedEffect(textIndex) {
        Log.i("AnimatedText", "AnimatedText: current value $textIndex from $indexList")
        val delay = (animation.getDelay() * Random.nextInt(1, 3)).toLong()
        delayedFunction(delay) {
            listIndex++
            if (textPart == text) {
                onCompleteType?.invoke()
            }
        }
    }

    LaunchedEffect(listIndex) {
        Log.i("AnimatedText", "AnimatedText: current list index $listIndex from $indexList")
        textIndex = try {
            indexList[listIndex]
        } catch (e: Exception) {
            e.printStackTrace()
            indexList.last()
        }
    }


    LaunchedEffect(Unit) {
        Log.i("AnimatedText", "AnimatedText: indexes => $indexList")
        delayedFunction(500) {
            if (animationEnabled) listIndex++
        }
    }

    Text(
        text = textPart,
        style = textStyle,
        modifier = modifier.animateContentSize(tween(1000, easing = EaseIn)),
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowHeight && isAnimationComplete()) {
                style = textStyle.copy(fontSize = textStyle.fontSize * 0.7)
            }
        }
    )

    LaunchedEffect(text) {
        if (animationEnabled) {
            textIndex = 0
        }
    }
}