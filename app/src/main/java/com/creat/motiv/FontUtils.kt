package com.creat.motiv

import com.creat.motiv.model.beans.FontObject

object FontUtils {
    fun fonts(): List<FontObject> {
        return listOf(
                FontObject("Arvo", "fonts/Arvo-Regular_201.ttf"),
                FontObject("Audrey", "fonts/Audrey-Normal.otf"),
                FontObject("Cornerstone", "fonts/Cornerstone.ttf"),
                FontObject("Times", "fonts/times.ttf"),
                FontObject("Mightype", "fonts/MightypeScript.otf"),
                FontObject("Amatic", "fonts/AmaticSC-Regular.ttf"),
                FontObject("BlackHan", "fonts/BlackHanSans-Regular.ttf"),
                FontObject("Cabin", "fonts/Cabin-Regular.ttf"),
                FontObject("Cinzel regular", "fonts/Cinzel-Regular.ttf"),
                FontObject("Farsan", "fonts/Farsan-Regular.ttf"),
                FontObject("FingerPaint", "fonts/FingerPaint-Regular.ttf"),
                FontObject("Fredoka One", "fonts/FredokaOne-Regular.ttf"),
                FontObject("Incosolata", "fonts/Inconsolata-Regular.ttf"),
                FontObject("Lalezar", "fonts/Lalezar-Regular.ttf"),
                FontObject("Lobster", "fonts/Lobster-Regular.ttf"),
                FontObject("Mogra", "fonts/Mogra-Regular.ttf"),
                FontObject("Nunito", "fonts/Nunito-Regular.ttf"),
                FontObject("Pacifico", "fonts/Pacifico-Regular.ttf"),
                FontObject("Quicksand", "fonts/Quicksand-Regular.ttf"),
                FontObject("Rakkas", "fonts/Rakkas-Regular.ttf"),
                FontObject("Ranga", "fonts/Ranga-Regular.ttf"),
                FontObject("Rasa", "fonts/Rasa-Regular.ttf")
        )
    }
}