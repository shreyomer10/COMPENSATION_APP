package com.example.compensation_app.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.CheckUserRequest
import com.example.compensation_app.Backend.CheckUserResponse
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.R
import com.example.compensation_app.viewmodel.GuardViewModel

@Composable
fun CompensationUserLoginScreen(navController: NavController) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val viewModel: GuardViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(false) }
    var Response by remember {
        mutableStateOf<CheckUserResponse?>(null)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 30.dp)
        )

        Text(
            text = "Compensation App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "User Login",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        // User ID Input
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("Enter your User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    errorMessage = validateInputs2(userId, password)
                    if (errorMessage == null) {
                        isLoading = true
                        val request = CheckUserRequest(
                            UserId = userId,
                            Password = password
                        )
                        viewModel.verifyUser(request = request) { response, message ->
                            isLoading = false // Stop loading when response is received

                            if (response==null) {
                                errorMessage = message
                            }
                            else{
                                Response=response
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text(text = "Login")
            }
        }


        // Login Button
//        Button(
//            onClick = {  },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(8.dp),
//            colors = ButtonDefaults.buttonColors(Color.Blue)
//        ) {
//            Text(text = "Login", color = Color.White)
//        }

        Spacer(modifier = Modifier.height(20.dp))

        // Footer Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {navController.navigate(NavigationScreens.UserSignUpScreen.name)
            }) {
                Text(text = "New User? Register", color = Color.Blue)
            }
            TextButton(onClick = { navController.navigate(NavigationScreens.LoginScreen.name)
            }) {
                Text(text = "Official Login", color = Color.Blue)
            }
        }
    }
}
fun validateInputs2(userId: String, password: String): String? {
    if (userId.isEmpty() || password.isEmpty()) {
        return "All fields are required."
    }
    if (!userId.matches(Regex("^[a-zA-Z0-9]+$"))) {
        return "User ID must contain only alphabets and digits."
    }
    return null
}
