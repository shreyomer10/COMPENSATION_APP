package com.example.compensation_app.Navigation

import android.content.Context
import android.content.SharedPreferences

fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("is_logged_in", isLoggedIn)
    editor.apply()
}

fun clearLoginStatus(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("is_logged_in", false)
    editor.apply()
}