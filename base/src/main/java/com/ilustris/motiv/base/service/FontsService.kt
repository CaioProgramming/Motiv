package com.ilustris.motiv.base.service

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import com.creat.motiv.base.R
import kotlinx.coroutines.coroutineScope

class FontsService(val context: Context) {

    suspend fun requestDownload(
        familyName: String,
        retrieveTypeface: (Typeface?, String?) -> Unit
    ) {
        coroutineScope {
            val handlerThread = HandlerThread("fonts")
            handlerThread.start()
            val mHandler = Handler(handlerThread.looper)
            Log.i(javaClass.simpleName, "requestDownload: $familyName")
            val request = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                familyName,
                R.array.com_google_android_gms_fonts_certs
            )
            val callback = object : FontsContractCompat.FontRequestCallback() {
                override fun onTypefaceRetrieved(typeface: Typeface) {
                    retrieveTypeface(typeface, null)
                }

                override fun onTypefaceRequestFailed(reason: Int) {
                    retrieveTypeface(null, "Erro ao obter fonte($reason)")
                    Log.e(
                        javaClass.simpleName,
                        "onTypefaceRequestFailed: Error requesting typeface reason : $reason",
                    )
                }
            }
            FontsContractCompat
                .requestFont(context, request, callback, mHandler)
        }
    }

    fun getFamilyName(index: Int): String {
        val fontArray = context.resources.getStringArray(R.array.family_names)
        return fontArray[index]
    }

    fun getFamilies() = context.resources.getStringArray(R.array.family_names)

}