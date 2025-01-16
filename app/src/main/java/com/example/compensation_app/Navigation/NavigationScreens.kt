package com.example.compensation_app.Navigation

import com.example.compensation_app.ui.theme.LanguageSwitchScreen

enum class NavigationScreens {
    SplashScreen,
    LoginScreen,
    HomeScreen,
    NewApplicationScreen,
    DraftApplicationScreen,
    PrevApplicationScreen,
    CompleteFormScreen,
    LanguageChangeScreen;
    companion object{
        fun fromRoute(route :String):NavigationScreens=
            when(route.substringBefore("/")){
                SplashScreen.name -> SplashScreen
                LoginScreen.name->LoginScreen
                HomeScreen.name->HomeScreen
                NewApplicationScreen.name->NewApplicationScreen
                DraftApplicationScreen.name->DraftApplicationScreen
                PrevApplicationScreen.name->PrevApplicationScreen
                LanguageChangeScreen.name->LanguageChangeScreen
                CompleteFormScreen.name->CompleteFormScreen


                else-> throw IllegalArgumentException("Route $route not found ")

            }

    }
}