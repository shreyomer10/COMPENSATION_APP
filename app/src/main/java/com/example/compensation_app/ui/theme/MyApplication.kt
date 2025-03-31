package com.example.compensation_app.ui.theme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import android.content.Context
import android.content.SharedPreferences
@HiltAndroidApp
class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext // Store application context globally
    }

    companion object {
        lateinit var appContext: Context
            private set
    }


    // Function to save login status



}