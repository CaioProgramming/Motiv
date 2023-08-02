package com.ilustris.motiv.base.service

import android.content.Context
import android.util.Log
import com.creat.motiv.base.R
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult


class PreferencesService(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name).trim().lowercase(),
        Context.MODE_PRIVATE
    )

    private fun getEditor() = sharedPreferences.edit()

    fun editPreference(key: String, value: String): ServiceResult<DataException, String> {
        return try {
            getEditor().putString(key, value).commit()
            val message = "Preferências atualizadas, $key: $value"
            ServiceResult.Success(message)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.simpleName, "editPreference: Error updating preferences ${e.message}")
            ServiceResult.Error(DataException.SAVE)
        }
    }

    fun getStringValue(key: String) = sharedPreferences.getString(key, null)


}