package com.example.compensation_app.screens.user
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.R

@Composable
fun CompensationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 20.dp)
        )

        Text(
            text = "Compensation App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ButtonComponent("Apply for Compensation"){
            navController.navigate(NavigationScreens.ComplaintScreen.name)
        }
        ButtonComponent("Check Application Status"){
            navController.navigate(NavigationScreens.SearchComplaintForm.name)

        }
        ButtonComponent("Official Login"){
            navController.navigate(NavigationScreens.LoginScreen.name)
        }

        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun ButtonComponent(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(Color.Blue)
    ) {
        Text(text, color = Color.White)
    }
}
