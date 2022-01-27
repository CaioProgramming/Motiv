package com.ilustris.motiv.base.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import java.lang.Math.abs


private const val DEFAULT_TRANSLATION_X = .0f
private const val DEFAULT_TRANSLATION_FACTOR = 1.2f

private const val SCALE_FACTOR = .14f
private const val DEFAULT_SCALE = 1f

private const val ALPHA_FACTOR = .3f
private const val DEFAULT_ALPHA = 1f

class PagerStackTransformer(private val offscreenPageLimit: Int) : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {

            ViewCompat.setElevation(page, -abs(position))

            val scaleFactor = -SCALE_FACTOR * position + DEFAULT_SCALE
            val alphaFactor = -ALPHA_FACTOR * position + DEFAULT_ALPHA

            when {
                position <= 0f -> {
                    translationX = DEFAULT_TRANSLATION_X
                    scaleX = DEFAULT_SCALE
                    scaleY = DEFAULT_SCALE
                    alpha = DEFAULT_ALPHA + position
                }
                position <= offscreenPageLimit - 1 -> {
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    translationX = -(width / DEFAULT_TRANSLATION_FACTOR) * position
                    alpha = alphaFactor
                }
                else -> {
                    translationX = DEFAULT_TRANSLATION_X
                    scaleX = DEFAULT_SCALE
                    scaleY = DEFAULT_SCALE
                    alpha = DEFAULT_ALPHA
                }
            }
        }
    }
}