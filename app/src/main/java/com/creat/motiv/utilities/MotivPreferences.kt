package com.creat.motiv.utilities

import android.content.Context
import android.content.SharedPreferences


const val HOME_TUTORIAL = "Home Tutorial"
const val PROFILE_TUTORIAL = "Profile Tutorial"
const val NEW_QUOTE_TUTORIAL = "New Quote Tutorial"

class MotivPreferences(val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy { context.getSharedPreferences("motiv", Context.MODE_PRIVATE) }

    fun updateTutorial(tutorial: String) {
        sharedPreferences.edit().run {
            putBoolean(tutorial, true)
            apply()
        }
    }

    fun checkTutorial(tutorial: String) = sharedPreferences.getBoolean(tutorial, false)

}