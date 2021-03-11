package com.ilustris.motiv.base.utils

import android.content.Context
import android.graphics.Typeface

object FontUtils {
    val fonts: List<FontObject> = listOf(
            FontObject("Amatic", "fonts/AmaticSC-Bold.ttf"),
            FontObject("Bangers", "fonts/Bangers-Regular.ttf"),
            FontObject("Bigelow", "fonts/BigelowRules-Regular.ttf"),
            FontObject("Bungee Shade", "fonts/BungeeShade-Regular.ttf"),
            FontObject("Caveat", "fonts/Caveat-Medium.ttf"),
            FontObject("Cormorant", "fonts/CormorantUnicase-Regular.ttf"),
            FontObject("Cutive Mono", "fonts/CutiveMono-Regular.ttf"),
            FontObject("Dot Gothic", "fonts/DotGothic16-Regular.ttf"),
            FontObject("Indie Flower", "fonts/IndieFlower-Regular.ttf"),
            FontObject("Lexend Zetta", "fonts/LexendZetta-Regular.ttf"),
            FontObject("Libre Barcode", "fonts/LibreBarcode39Text-Regular.ttf"),
            FontObject("Lalezar", "fonts/Oswald-Bold.ttf"),
            FontObject("Redressed", "fonts/Redressed-Regular.ttf"),
            FontObject("Righteous", "fonts/Righteous-Regular.ttf"),
            FontObject("Sacramento", "fonts/Sacramento-Regular.ttf"),
            FontObject("Pacifico", "fonts/Pacifico-Regular.ttf"),
            FontObject("Satisfy", "fonts/Satisfy-Regular.ttf")
    )

    fun getTypeFace(context: Context, position: Int): Typeface {
        return loadTypeFace(context, fonts[position].path)
    }

    private fun loadTypeFace(context: Context, path: String): Typeface {
        return Typeface.createFromAsset(context.assets, path)
    }
}
