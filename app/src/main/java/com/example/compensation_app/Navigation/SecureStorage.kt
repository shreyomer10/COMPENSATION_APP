package com.example.compensation_app.Navigation

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
object SecureStorage {

    private const val PREFS_NAME = "secure_prefs"
    private const val TOKEN_KEY = "auth_token"
    private const val TOKEN_TIME_KEY = "token_saved_time" // New key to store token saved timestamp

    private fun getEncryptedPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun saveToken(context: Context, token: String) {
        val prefs = getEncryptedPrefs(context)
        val currentTimeMillis = System.currentTimeMillis()  // Save current time in millis
        prefs.edit().apply {
            putString(TOKEN_KEY, token)
            putLong(TOKEN_TIME_KEY, currentTimeMillis)
        }.apply()
    }

    fun getToken(context: Context): String? {
        return getEncryptedPrefs(context).getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        getEncryptedPrefs(context).edit().apply {
            remove(TOKEN_KEY)
            remove(TOKEN_TIME_KEY)
        }.apply()
    }

    // Returns the time (in millis) when the token was saved, or null if not set
    fun getTokenSavedTime(context: Context): Long? {
        return if (getEncryptedPrefs(context).contains(TOKEN_TIME_KEY)) {
            getEncryptedPrefs(context).getLong(TOKEN_TIME_KEY, 0)
        } else {
            null
        }
    }

    // Returns the expiration time (in millis) for the token based on a session duration of 2 hours
    fun getTokenExpirationTime(context: Context, sessionDurationMillis: Long = 2 * 60 * 60 * 1000): Long? {
        val savedTime = getTokenSavedTime(context)
        return savedTime?.let { it + sessionDurationMillis }
    }
}
