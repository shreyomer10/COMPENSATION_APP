package com.example.compensation_app.screens


import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.R
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.sqlite.MainViewModel

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(navController: NavController) {
    val context= LocalContext.current
    // Scale animation for the logo
    val infiniteTransition = rememberInfiniteTransition()
    val imageScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Image Scale Animation"
    )

    // Alpha animation for the blur effect
    var startAnimation by remember { mutableStateOf(false) }
    val blurEffect by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f, // Adjust alpha for overlay
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
    val mainViewModel: MainViewModel = hiltViewModel()
    var emp by remember {
        mutableStateOf<emp>(
            emp(emp_id = "",
            mobile_number = "",
            Name = "",
            Circle_CG = "",
            Circle1 = "",
            roll = "guard",
            subdivision = "",
            division = "", range_ = "", beat = 0)
        )
    }
    mainViewModel.GetGuard {
        if (it != null) {
            emp=it
        }
    }
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1000)
        if (isLoggedIn) {
            if(emp.roll=="forestguard"){
                navController.navigate(NavigationScreens.HomeScreen.name) {
                    popUpTo(NavigationScreens.AppHome.name) { inclusive = true }
                }
            }
            else if(emp.roll=="deputyranger"){
                navController.navigate(NavigationScreens.DeputyHomeScreen.name) {
                    popUpTo(NavigationScreens.AppHome.name) { inclusive = true }
                }
            }
            // Navigate to the HomeScreen if user is logged in

        } else {
            // Navigate to the LoginScreen if user is not logged in
            navController.navigate(NavigationScreens.AppHome.name)
        }
    }



    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Base background color
    ) {
        // Background overlay to simulate blur
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = blurEffect)) // Adjust alpha to simulate blur
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = com.example.compensation_app.R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180.dp)
                    .graphicsLayer(
                        scaleX = imageScale,
                        scaleY = imageScale
                    )
                    .padding(bottom = 16.dp)
            )
        }
    }
}
