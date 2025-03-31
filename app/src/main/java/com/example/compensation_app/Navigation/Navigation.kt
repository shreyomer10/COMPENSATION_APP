package com.example.compensation_app.Navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Backend.UserComplaintForm
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.LocalNavController
import com.example.compensation_app.screens.AboutUsScreen
import com.example.compensation_app.screens.ContactUs
import com.example.compensation_app.screens.guard.DraftApplication
import com.example.compensation_app.screens.guard.EditDraftApplication
import com.example.compensation_app.screens.guard.HomeScreen
import com.example.compensation_app.screens.LoginScreen
import com.example.compensation_app.screens.SignUpScreen
import com.example.compensation_app.screens.guard.NewApplication
import com.example.compensation_app.screens.guard.PrevApplicationScreen
import com.example.compensation_app.screens.guard.RetrivalFormDetailsScreen
import com.example.compensation_app.screens.SplashScreen
import com.example.compensation_app.screens.UpdateChangePass
import com.example.compensation_app.screens.deputyRanger.DeputyHomeScreen

import com.example.compensation_app.screens.deputyRanger.FormScreen

import com.example.compensation_app.screens.guard.ProfileScreem
import com.example.compensation_app.screens.guard.complaints.ComplaintApplicationScreen
import com.example.compensation_app.screens.guard.complaints.CompleteComplaintFormScreen
import com.example.compensation_app.screens.user.CompensationScreen
import com.example.compensation_app.screens.user.ComplaintForm
import com.example.compensation_app.screens.user.DisplaySuccessScreen
import com.example.compensation_app.screens.user.RetrivalComplaintDisplayScreen
import com.example.compensation_app.screens.user.SearchComplaint
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(){
    val navController= LocalNavController.current
    val gson = Gson()
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
        composable(route = NavigationScreens.AboutUs.name) {
            AboutUsScreen(navController = navController)
        }
        composable(route = NavigationScreens.ContactUs.name) {
            ContactUs(navController = navController)
        }

        composable(route = NavigationScreens.LoginScreen.name){
            AnimatedScreenTransition {
                LoginScreen(navController)

            }
        }
        composable(route = NavigationScreens.UpdatePass.name){
            AnimatedScreenTransition {
                UpdateChangePass(navController)

            }
        }
        composable(route = NavigationScreens.SignUpScreen.name){
            AnimatedScreenTransition {
                SignUpScreen(navController)

            }
        }
        composable(route = NavigationScreens.AppHome.name){
            AnimatedScreenTransition {
                CompensationScreen(navController)

            }
        }
        composable(
            route = "${NavigationScreens.DisplaySuccessScreen.name}/{formDataJson}/{complaintId}",
            arguments = listOf(
                navArgument("formDataJson") { type = NavType.StringType },
                navArgument("complaintId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val jsonData = backStackEntry.arguments?.getString("formDataJson") ?: ""
            val complaintId = backStackEntry.arguments?.getInt("complaintId") ?: 0

            // Decode JSON to Object
            val formData: UserComplaintForm = Gson().fromJson(jsonData, UserComplaintForm::class.java)

            DisplaySuccessScreen(navController,formData, complaintId)
        }

        composable(
            route = NavigationScreens.RetrivalComplaintDisplayScreen.name + "/{encodedForm}",
            arguments = listOf(navArgument("encodedForm") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedForm = backStackEntry.arguments?.getString("encodedForm")
            val decodedForm = encodedForm?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }

            AnimatedScreenTransition {
                RetrivalComplaintDisplayScreen(navController = navController, encodedForm = decodedForm)
            }
        }
        composable(route = NavigationScreens.ComplaintScreen.name){
            AnimatedScreenTransition {
                ComplaintForm(navController)

            }
        }
        composable(route = NavigationScreens.SearchComplaintForm.name){
            AnimatedScreenTransition {
                SearchComplaint(navController)

            }
        }

        composable(
            route = NavigationScreens.HomeScreen.name,

        ) { backStackEntry ->
            AnimatedScreenTransition {
                HomeScreen(navController)
            }
        }
        composable(
            route = NavigationScreens.DeputyHomeScreen.name,

            ) { backStackEntry ->
            AnimatedScreenTransition {
                DeputyHomeScreen(navController)
            }
        }
        composable(route = NavigationScreens.NewApplicationScreen.name+"/{encodedGuardJson}",
            arguments = listOf(navArgument("encodedGuardJson") { type = NavType.StringType } )){
            val encodedGuard = it.arguments?.getString("encodedGuardJson")
            val guard = encodedGuard?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            AnimatedScreenTransition {
                NewApplication(navController,guard=guard)

            }
        }
        composable(route = NavigationScreens.ComplaintApplicationGuard.name+"/{encodedGuardJson}",
            arguments = listOf(navArgument("encodedGuardJson") { type = NavType.StringType } )){
            val encodedGuard = it.arguments?.getString("encodedGuardJson")
            val guard = encodedGuard?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            AnimatedScreenTransition {
                ComplaintApplicationScreen(navController,guard=guard)

            }
        }
        composable(route = NavigationScreens.PendingForYouScreenGuard.name+"/{encodedGuardJson}",
            arguments = listOf(navArgument("encodedGuardJson") { type = NavType.StringType } )){
            val encodedGuard = it.arguments?.getString("encodedGuardJson")
            val guard = encodedGuard?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            AnimatedScreenTransition {
                NewApplication(navController,guard=guard)

            }
        }
        composable(
            route = NavigationScreens.EditDraftScreen.name + "/{guard}"+"/{encodedFormJson}",
            arguments = listOf(
                navArgument("guard") { type = NavType.StringType },
                navArgument("encodedFormJson") { type = NavType.StringType }
            )
        ) {
            val guard = it.arguments?.getString("guard")
            val encodedForm = it.arguments?.getString("encodedFormJson")

            //val guard = encodedGuard?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            val form = encodedForm?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }

            AnimatedScreenTransition {
                EditDraftApplication(navController, guard = guard, draftForm = form)
            }
        }
        composable(
            route = NavigationScreens.CompleteComplaintGuardScreen.name + "/{encodedForm}"+ "/{guard}",
            arguments = listOf(navArgument("encodedForm") { type = NavType.StringType },
                navArgument("guard") { type = NavType.StringType },)
        ) { backStackEntry ->
            val encodedForm = backStackEntry.arguments?.getString("encodedForm")
            val guard = backStackEntry.arguments?.getString("guard")
            // val text=backStackEntry.arguments?.getString("text")
            val decodedForm = encodedForm?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }

            AnimatedScreenTransition {
                CompleteComplaintFormScreen(navController=navController,encodedFormComplaint=decodedForm,guard=guard)
                // RetrivalFormDetailsScreen(navController = navController, encodedForm = decodedForm,text=text)
            }
        }

        composable(route = NavigationScreens.DraftApplicationScreen.name+"/{encodedGuardJson}",
            arguments = listOf(navArgument("encodedGuardJson") { type = NavType.StringType } )){
            val encodedGuard = it.arguments?.getString("encodedGuardJson")
            val guard = encodedGuard?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            AnimatedScreenTransition {
                DraftApplication(navController,guard=guard)
                //NewApplication(navController)

            }
        }
        composable(route = NavigationScreens.ProfileScreen.name){
            AnimatedScreenTransition {
                ProfileScreem(navController)

            }
        }
        composable(
            route = NavigationScreens.PendingScreen.name + "/{encodedEmpJson}/{encodedPending}",
            arguments = listOf(
                navArgument("encodedEmpJson") { type = NavType.StringType },
                navArgument("encodedPending") { type = NavType.StringType }
            )
        ) {
            val gson = Gson()

            val encodedEmp = it.arguments?.getString("encodedEmpJson")
            val encodedForms = it.arguments?.getString("encodedPending") // Adjust for each screen

            val emp: emp? = encodedEmp?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), emp::class.java) }
            val forms: List<RetrivalForm> = encodedForms?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), object : TypeToken<List<RetrivalForm>>() {}.type) } ?: emptyList()




            AnimatedScreenTransition {
                FormScreen(navController = navController,name="Pending", emp = emp, forms = forms)
            }
        }

        composable(
            route = NavigationScreens.PendingForYouScreen.name + "/{encodedEmpJson}/{encodedPendingForYou}",
            arguments = listOf(
                navArgument("encodedEmpJson") { type = NavType.StringType },
                navArgument("encodedPendingForYou") { type = NavType.StringType }
            )
        ) {
            val gson = Gson()

            val encodedEmp = it.arguments?.getString("encodedEmpJson")
            val encodedForms = it.arguments?.getString("encodedPendingForYou") // Adjust for each screen
            Log.d("TAG", "Navigation: $encodedForms")

            val emp: emp? = encodedEmp?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), emp::class.java) }
            val forms: List<RetrivalForm> = encodedForms?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), object : TypeToken<List<RetrivalForm>>() {}.type) } ?: emptyList()


            AnimatedScreenTransition {
                FormScreen(navController = navController, emp = emp, name = "PendingForYou", forms = forms)
            }
        }

        composable(
            route = NavigationScreens.AcceptedScreen.name + "/{encodedEmpJson}/{encodedAccepted}",
            arguments = listOf(
                navArgument("encodedEmpJson") { type = NavType.StringType },
                navArgument("encodedAccepted") { type = NavType.StringType }
            )
        ) {
            val gson = Gson()

            val encodedEmp = it.arguments?.getString("encodedEmpJson")
            val encodedForms = it.arguments?.getString("encodedAccepted") // Adjust for each screen

            val emp: emp? = encodedEmp?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), emp::class.java) }
            val forms: List<RetrivalForm> = encodedForms?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), object : TypeToken<List<RetrivalForm>>() {}.type) } ?: emptyList()


            AnimatedScreenTransition {
                FormScreen(navController = navController, emp = emp, name = "Accepted", forms = forms)
            }
        }

        composable(
            route = NavigationScreens.RejectedScreen.name + "/{encodedEmpJson}/{encodedRejected}",
            arguments = listOf(
                navArgument("encodedEmpJson") { type = NavType.StringType },
                navArgument("encodedRejected") { type = NavType.StringType }
            )
        ) {
            val gson = Gson()

            val encodedEmp = it.arguments?.getString("encodedEmpJson")
            val encodedForms = it.arguments?.getString("encodedRejected") // Adjust for each screen

            val emp: emp? = encodedEmp?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), emp::class.java) }
            val forms: List<RetrivalForm> = encodedForms?.let { json -> gson.fromJson(URLDecoder.decode(json, StandardCharsets.UTF_8.toString()), object : TypeToken<List<RetrivalForm>>() {}.type) } ?: emptyList()



            AnimatedScreenTransition {
                FormScreen(navController = navController, emp = emp, name = "Rejected", forms = forms)
            }
        }

        composable(route = NavigationScreens.PrevApplicationScreen.name+"/{encodedGuardJson}",
            arguments = listOf(navArgument("encodedGuardJson") { type = NavType.StringType } )){
            val encodedGuard = it.arguments?.getString("encodedGuardJson")
            val guard = encodedGuard?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            AnimatedScreenTransition {
                PrevApplicationScreen(navController = navController, guard = guard)
                //HomeScreen(navController)

            }
        }
        composable(
            route = NavigationScreens.CompleteFormScreen.name + "/{encodedForm}" +"/{text}",
            arguments = listOf(navArgument("encodedForm") { type = NavType.StringType },
                navArgument("text") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedForm = backStackEntry.arguments?.getString("encodedForm")
            val text=backStackEntry.arguments?.getString("text")
            val decodedForm = encodedForm?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }

            AnimatedScreenTransition {
                RetrivalFormDetailsScreen(navController = navController, encodedForm = decodedForm,text=text)
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
