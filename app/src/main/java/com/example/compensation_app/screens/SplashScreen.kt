package com.example.compensation_app.screens


import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.draw.blur
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
import com.example.compensation_app.Navigation.clearLoginStatus
import com.example.compensation_app.sqlite.MainViewModel

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current

    // Controls the start of the animation
    var startAnimation by remember { mutableStateOf(false) }

    // Animate logo scale (0.8f -> 1f) for a smooth zoom-in effect
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    // Animate logo alpha (fade-in)
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    // Animate background blur (from 16.dp blurred to clear)
    val blurRadius by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 16f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    // Retrieve shared preferences and login state
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
    val mainViewModel: MainViewModel = hiltViewModel()
    var emp by remember {
        mutableStateOf(
            emp(
                emp_id = "",
                mobile_number = "",
                Name = "",
                Circle_CG = "",
                Circle1 = "",
                roll = "guard",
                subdivision = "",
                division = "",
                range_ = "",
                beat = ""
            )
        )
    }
    mainViewModel.GetGuard {
        if (it != null) {
            emp = it
        }
    }

    // Start animations and navigate after delay
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1500) // Allow animation to complete before navigating

        if (isLoggedIn) {
            when (emp.roll) {
                "forestguard" -> navController.navigate(NavigationScreens.HomeScreen.name) {
                    popUpTo(NavigationScreens.AppHome.name) { inclusive = true }
                }
                "deputyranger" -> navController.navigate(NavigationScreens.DeputyHomeScreen.name) {
                    popUpTo(NavigationScreens.AppHome.name) { inclusive = true }
                }
                else -> {
                    clearLoginStatus(context)
                    navController.navigate(NavigationScreens.AppHome.name)
                }
            }
        } else {
            navController.navigate(NavigationScreens.AppHome.name)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Optional background image with animated blur for a premium effect.
        // Replace R.drawable.background with your background image resource.


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Animated App Logo
            Image(
                painter = painterResource(id = com.example.compensation_app.R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(240.dp)
                    .graphicsLayer {
                        scaleX = logoScale
                        scaleY = logoScale
                        alpha = logoAlpha
                    }
                    .padding(bottom = 16.dp)
            )
        }
    }
}
