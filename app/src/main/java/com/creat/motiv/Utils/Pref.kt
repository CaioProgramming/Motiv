package com.creat.motiv.Utils

import android.content.Context
import android.content.SharedPreferences

import androidx.appcompat.app.AppCompatDelegate

import com.creat.motiv.R

class Pref(internal var context: Context) {
    internal var mySharedPreferences: SharedPreferences

    init {
        mySharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
    }

    fun setNight(state: Boolean) {

        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //context.setTheme(R.style.AppTheme_Night);
            val editor = mySharedPreferences.edit()
            editor.putBoolean("NightMode", state)
            editor.commit()

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            context.setTheme(R.style.AppTheme)
            val editor = mySharedPreferences.edit()
            editor.putBoolean("NightMode", state)
            editor.commit()
        }
    }

    fun nightmodestate(): Boolean {

        return mySharedPreferences.getBoolean("NightMode", false)

    }

    fun setAgree(state: Boolean) {
        val editor = mySharedPreferences.edit()
        editor.putBoolean("Terms", state)
        editor.commit()
    }

    fun agreestate(): Boolean {
        return mySharedPreferences.getBoolean("Terms", false)
    }


    fun setHomeTutorial(state: Boolean) {
        val editor = mySharedPreferences.edit()
        editor.putBoolean("Home", state)
        editor.commit()
    }

    fun hometutorialstate(): Boolean {
        return mySharedPreferences.getBoolean("Home", false)
    }

    fun setWriteTutorial(state: Boolean) {
        val editor = mySharedPreferences.edit()
        editor.putBoolean("Write", state)
        editor.commit()
    }

    fun writetutorialstate(): Boolean {
        return mySharedPreferences.getBoolean("Write", false)
    }

    fun setProfileTutorial(state: Boolean) {
        val editor = mySharedPreferences.edit()
        editor.putBoolean("Profile", state)
        editor.commit()
    }

    fun profiletutorialstate(): Boolean {
        return mySharedPreferences.getBoolean("Profile", false)
    }

    fun setAlarm(state: Boolean) {
        val editor = mySharedPreferences.edit()
        editor.putBoolean("Alarm", state)
        editor.commit()
    }


    fun alarm(): Boolean {
        return mySharedPreferences.getBoolean("Alarm", true)
    }


}
