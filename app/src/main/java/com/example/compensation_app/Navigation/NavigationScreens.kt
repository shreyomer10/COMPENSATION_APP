package com.example.compensation_app.Navigation



enum class NavigationScreens {
    SplashScreen,
    AppHome,
    ComplaintScreen,
    DisplaySuccessScreen,
    SearchComplaintForm,
    RetrivalComplaintDisplayScreen,
    LoginScreen,
    SignUpScreen,
    UpdatePass,
    HomeScreen,
    NewApplicationScreen,
    ComplaintApplicationGuard,
    PendingForYouScreenGuard,
    DraftApplicationScreen,
    PrevApplicationScreen,
    CompleteFormScreen,
    CompleteComplaintGuardScreen,
    EditDraftScreen,
    DeputyHomeScreen,
    PendingForYouScreen,
    PendingScreen,
    AcceptedScreen,
    RejectedScreen,
    ProfileScreen,
    AboutUs,
    ContactUs;
    companion object{
        fun fromRoute(route :String):NavigationScreens=
            when(route.substringBefore("/")){
                SplashScreen.name -> SplashScreen
                AppHome.name->AppHome
                ComplaintScreen.name->ComplaintScreen
                DisplaySuccessScreen.name->DisplaySuccessScreen
                SearchComplaintForm.name->SearchComplaintForm
                RetrivalComplaintDisplayScreen.name->RetrivalComplaintDisplayScreen
                LoginScreen.name->LoginScreen
                SignUpScreen.name->SignUpScreen
                UpdatePass.name->UpdatePass
                HomeScreen.name->HomeScreen
                NewApplicationScreen.name->NewApplicationScreen
                ComplaintApplicationGuard.name->ComplaintApplicationGuard
                PendingForYouScreenGuard.name->PendingForYouScreenGuard
                DraftApplicationScreen.name->DraftApplicationScreen
                PrevApplicationScreen.name->PrevApplicationScreen
                ProfileScreen.name->ProfileScreen
                CompleteFormScreen.name->CompleteFormScreen
                CompleteComplaintGuardScreen.name->CompleteComplaintGuardScreen
                EditDraftScreen.name->EditDraftScreen
                DeputyHomeScreen.name->DeputyHomeScreen
                PendingScreen.name->PendingScreen
                PendingForYouScreen.name->PendingForYouScreen
                AcceptedScreen.name->AcceptedScreen
                RejectedScreen.name->RejectedScreen
                AboutUs.name->AboutUs
                ContactUs.name->ContactUs

                else-> throw IllegalArgumentException("Route $route not found ")

            }

    }
}