package com.example.compensation_app.Navigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.ui.theme.MyApplication
import com.example.compensation_app.viewmodel.GuardViewModel

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

fun logoutUser(navController: NavController,mainViewModel: MainViewModel) {
    // Clear user session

    clearLoginStatus(context = MyApplication.appContext)
    SecureStorage.clearToken(MyApplication.appContext)
    // Navigate to login screen
    navController.navigate(NavigationScreens.LoginScreen.name){
        popUpTo(NavigationScreens.AppHome.name) { inclusive = true } // Remove all previous screens from stack
    }
}
