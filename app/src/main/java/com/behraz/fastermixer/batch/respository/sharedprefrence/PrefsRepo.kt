package com.behraz.fastermixer.batch.respository.sharedprefrence

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object PrefsRepo {
    private const val PREF_CREDENTIAL_USER = "username"
    private const val PREF_CREDENTIAL_PASSWORD = "password"
    private const val PREF_CREDENTIAL_FACTORY_ID = "factoryId"
    private const val PREF_CREDENTIAL_REMEMBERED = "remembered"


    private lateinit var prefs: SharedPreferences


    fun setContext(context: Context) {
        if (!this::prefs.isInitialized) {
            prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
        }
    }

    private const val MY_PREFS_NAME = "prefs"


    fun getUserCredentials(response: (factoryId: String, username: String, password: String) -> Unit) {
        val remembered = prefs.getBoolean(PREF_CREDENTIAL_REMEMBERED, false)
        if (remembered) {
            val factoryCode = prefs.getString(PREF_CREDENTIAL_FACTORY_ID, "")!!
            val username = prefs.getString(PREF_CREDENTIAL_USER, "")!!
            val password = prefs.getString(PREF_CREDENTIAL_PASSWORD, "")!!
            response(factoryCode, username, password)
        }
    }

    fun saveUserCredentials(factoryCode: String, username: String, password: String) {
        prefs.edit()
            .putBoolean(PREF_CREDENTIAL_REMEMBERED, true)
            .putString(PREF_CREDENTIAL_FACTORY_ID, factoryCode)
            .putString(PREF_CREDENTIAL_USER, username)
            .putString(PREF_CREDENTIAL_PASSWORD, password)
            .apply()
    }

    fun clearUserCredentials() {
        prefs.edit()
            .remove(PREF_CREDENTIAL_FACTORY_ID)
            .remove(PREF_CREDENTIAL_USER)
            .remove(PREF_CREDENTIAL_PASSWORD)
            .putBoolean(PREF_CREDENTIAL_REMEMBERED, false)
            .apply()
    }

    fun flush() {
        prefs.edit().clear().apply()
    }

}