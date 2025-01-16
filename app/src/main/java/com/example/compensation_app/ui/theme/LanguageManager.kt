package com.example.compensation_app.ui.theme
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.*


object LanguageManager {

    // Switch language function
    fun switchLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        // Update the app's configuration
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Optionally, restart the activity to apply the changes
        if (context is Activity) {
            // Recreate the activity to apply the new language
            context.recreate()
        }
    }
}
