package com.behraz.fastermixer.batch.respository.sharedprefrence

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

object PrefsRepo {
    private lateinit var prefs: SharedPreferences

    fun setContext(context: Context) {
        if (!this::prefs.isInitialized) {
            prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
        }
    }

    private const val MY_PREFS_NAME = "prefs"


    fun flush() {
        prefs.edit().clear().apply()
    }

}