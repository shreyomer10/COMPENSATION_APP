package com.example.compensation_app.ui.theme
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.compensation_app.R

@Composable
fun LanguageSwitchScreen() {
    val context = LocalContext.current
    val isLanguageHindi = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Language Switcher Header
        Text(
            text  = "Change Language",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Switch to toggle between languages
        Row(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "English", modifier = Modifier.padding(end = 8.dp))
            Switch(
                checked = isLanguageHindi.value,
                onCheckedChange = { checked ->
                    isLanguageHindi.value = checked
                    val languageCode = if (checked) "en" else "hi"
                    // Change language based on switch state
                    LanguageManager.switchLanguage(context, languageCode)
                    // Optionally show a message
                    Toast.makeText(context, "Language changed to ${if (checked) "English" else "Hindi"}", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(text = "Hindi", modifier = Modifier.padding(start = 8.dp))
        }
    }
}
