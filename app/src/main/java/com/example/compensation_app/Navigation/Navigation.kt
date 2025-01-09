package com.example.compensation_app.Navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compensation_app.LocalNavController
import com.example.compensation_app.screens.HomeScreen
import com.example.compensation_app.screens.LoginScreen
import com.example.compensation_app.screens.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun Navigation(){
    val navController= LocalNavController.current
    NavHost(
        navController = navController,
        startDestination = NavigationScreens.SplashScreen.name,
        enterTransition ={ fadeIn() },

        exitTransition = {
            fadeOut()
        }
    ) {
        composable(route = NavigationScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationScreens.LoginScreen.name){
            AnimatedScreenTransition {
                LoginScreen(navController)

            }
        }
        composable(route = NavigationScreens.HomeScreen.name){
            AnimatedScreenTransition {
                HomeScreen(navController)

            }
        }

}

}

@Composable
fun AnimatedScreenTransition(
    duration: Int = 350, // Duration for the animation
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }

    // Create animatable variables for opacity and scale
    val alpha = remember { Animatable(0f) }     // Initially invisible
    val scale = remember { Animatable(0.8f) }   // Start from zoomed-in (smaller) scale

    // Start animations for entering
    LaunchedEffect(isVisible) {
        if (isVisible) {
            // Animate opacity and scale for entering
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = duration)
                )
            }
            launch {
                scale.animateTo(
                    targetValue = 1f, // Zoom in to normal size
                    animationSpec = tween(durationMillis = duration)
                )
            }
        } else {
            // Animate opacity and scale for exiting
            launch {
                alpha.animateTo(
                    targetValue = 0f, // Fade out
                    animationSpec = tween(durationMillis = duration)
                )
            }
            launch {
                scale.animateTo(
                    targetValue = 1.2f, // Zoom out a bit before disappearing
                    animationSpec = tween(durationMillis = duration)
                )
            }
        }
    }

    // Manage visibility state
    DisposableEffect(Unit) {
        onDispose {
            isVisible = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
            .alpha(alpha.value) // Apply fade transition
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value) // Apply zoom effect
    ) {
        content()
    }
}
