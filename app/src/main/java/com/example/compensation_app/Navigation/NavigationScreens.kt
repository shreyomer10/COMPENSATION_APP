package com.example.compensation_app.Navigation



enum class NavigationScreens {
    SplashScreen,
    LoginScreen,
    HomeScreen,
    NewApplicationScreen,
    DraftApplicationScreen,
    PrevApplicationScreen,
    CompleteFormScreen,
    EditDraftScreen,
    DeputyHomeScreen,
    PendingForYouScreen,
    PendingScreen,
    AcceptedScreen,
    RejectedScreen,
    ProfileScreen;
    companion object{
        fun fromRoute(route :String):NavigationScreens=
            when(route.substringBefore("/")){
                SplashScreen.name -> SplashScreen
                LoginScreen.name->LoginScreen
                HomeScreen.name->HomeScreen
                NewApplicationScreen.name->NewApplicationScreen
                DraftApplicationScreen.name->DraftApplicationScreen
                PrevApplicationScreen.name->PrevApplicationScreen
                ProfileScreen.name->ProfileScreen
                CompleteFormScreen.name->CompleteFormScreen
                EditDraftScreen.name->EditDraftScreen
                DeputyHomeScreen.name->DeputyHomeScreen
                PendingScreen.name->PendingScreen
                PendingForYouScreen.name->PendingForYouScreen
                AcceptedScreen.name->AcceptedScreen
                RejectedScreen.name->RejectedScreen

                else-> throw IllegalArgumentException("Route $route not found ")

            }

    }
}