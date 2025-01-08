//package com.example.compensation_app
//
//
//import androidx.compose.animation.animateColor
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.R
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.minor_project.navigation.NavigationScreens
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.coroutines.delay
//
//
//
//@Composable
//fun SplashScreen(navController: NavHostController) {
//    // Scale animation for the logo
//    val infiniteTransition = rememberInfiniteTransition()
//    val imageScale by infiniteTransition.animateFloat(
//        initialValue = 0.8f,
//        targetValue = 1.2f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1200, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "Image Scale Animation"
//    )
//
//    // Alpha animation for the blur effect
//    var startAnimation by remember { mutableStateOf(false) }
//    val blurEffect by animateFloatAsState(
//        targetValue = if (startAnimation) 1f else 0f, // Adjust alpha for overlay
//        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
//    )
//
////    LaunchedEffect(key1 = true) {
////        startAnimation = true
////        delay(1000) // Delay for the splash screen to show
////        val email = FirebaseAuth.getInstance().currentUser?.email
////        if (email?.isNotEmpty() == true && email.any { it.isDigit() }) {
////            // Email is not empty and contains a digit
////            navController.navigate(NavigationScreens.StudentHomeScreen.name)
////
////        }
////        else if (FirebaseAuth.getInstance().currentUser?.email?.isNotEmpty() == true){
////            navController.navigate(NavigationScreens.HomeScreen.name)
////        }
////        else{
////            navController.navigate(NavigationScreens.LoginScreen.name)
////        }
////
////    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White) // Base background color
//    ) {
//        // Background overlay to simulate blur
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White.copy(alpha = blurEffect)) // Adjust alpha to simulate blur
//        )
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Image(
//                painter = painterResource(id = com.example.minor_project.R.drawable.logo),
//                contentDescription = "App Logo",
//                modifier = Modifier
//                    .size(180.dp)
//                    .graphicsLayer(
//                        scaleX = imageScale,
//                        scaleY = imageScale
//                    )
//                    .padding(bottom = 16.dp)
//            )
//        }
//    }
//}
