package com.example.compensation_app.Navigation

enum class NavigationScreens {
    SplashScreen,
    LoginScreen,
    HomeScreen;
    companion object{
        fun fromRoute(route :String):NavigationScreens=
            when(route.substringBefore("/")){
                SplashScreen.name -> SplashScreen
                LoginScreen.name->LoginScreen
                HomeScreen.name->HomeScreen


                else-> throw IllegalArgumentException("Route $route not found ")

            }

    }
}