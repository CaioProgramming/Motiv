package com.ilustris.motiv.base.service.helper

import android.content.Context
import android.util.Log
import java.io.File
import javax.inject.Inject


class RadioHelper @Inject constructor(private val context: Context) {

    private fun getCacheDir(): String {
        val cachePath = context.cacheDir.path + "/radios/"
        val cacheFile = File(cachePath)
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
        }
        return cachePath
    }


    fun createRadioFile(radioName: String): File {
        val cachePath = getCacheDir()
        return File(cachePath, "$radioName.mp3")
    }

    fun getRadioFile(url: String?): File? {
        return try {
            File(url)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.simpleName, "getRadioFile: Error finding file for $url")
            null
        }
    }
}