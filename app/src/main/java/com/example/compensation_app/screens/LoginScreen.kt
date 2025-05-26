package com.example.compensation_app.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.Navigation.SecureStorage
import com.example.compensation_app.Navigation.saveLoginStatus
import com.example.compensation_app.R
import com.example.compensation_app.sendOTP
import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.verifyOTP
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(navController: NavController) {
    BackHandler {
        navController.navigate(NavigationScreens.AppHome.name) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
    val viewModel:GuardViewModel= hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    var mobileNumber by remember { mutableStateOf("") }
    var guardId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    var showToast by remember { mutableStateOf<String?>(null) }

    var emp by remember {
        mutableStateOf<emp>(emp(emp_id = "",
            mobile_number = "",
            Name = "",
            Circle_CG = "",
            Circle1 = "",
            roll = "guard",
            subdivision = "",
            division = "", range_ = "", beat = ""))
    }

    Log.d("Final", "NewApplication:${emp.emp_id} ")
    // State to handle OTP resend and countdown
    var isOtpSent by remember { mutableStateOf(false) }
    val context = LocalContext.current


    var selectedRole by remember { mutableStateOf("forestguard") }

    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("forestguard", "deputyranger")
    val rolesMap = roles.associateWith { it.replaceFirstChar { ch -> ch.uppercase() } }


    var isLoading by remember { mutableStateOf(false) } // Loading state


    LaunchedEffect(showToast) {
        showToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showToast = null
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(170.dp)
                .padding(bottom = 10.dp)
        )

        Text(
            text = "ANUGRAH - अनुग्रह",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Official Login",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded } // Toggle dropdown
        ) {
            OutlinedTextField(
                value = rolesMap[selectedRole] ?: "Select Role", // Show Capitalized Role
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Role") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = !expanded }, // Toggle on click
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) { // Toggle dropdown
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                },
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Text color black
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(rolesMap[role] ?: role) }, // Capitalized text
                        onClick = {
                            selectedRole = role
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = mobileNumber,
            onValueChange = {
                if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                    mobileNumber = it
                }
            },
            label = { Text("Enter your mobile number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set text color to black

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guardId,
            onValueChange = { guardId = it },
            label = { Text("Enter your Officer ID ") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set text color to black

        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter your Password") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set text color to black

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading=true
                viewModel.login(
                    empId = guardId,
                    mobileNumber = mobileNumber,
                    roll =selectedRole,
                    password=password
                ){response, error ->
                    isLoading=false
                    if (response != null) {
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        Log.d("API EMPLOYEE", "LoginScreen: ${response.employee}")
                        emp= response.employee!!
                        mainViewModel.GuardDetails(emp)             //insert in sqlite
                        saveLoginStatus(context,true)
                        response.token?.let { SecureStorage.saveToken(context, it) } //token in sharedpref
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

                    } else {
                        Log.d("ERROR IN LOGIN", "LoginScreen: $error")
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A66C2)),
            //colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        ) {
            Text(text = "Login", color = Color.White)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { navController.navigate(NavigationScreens.UpdatePass.name)
                },
                enabled = !isOtpSent // Disable the button if OTP is already sent
            ) {
                Text(text = "Forget Password?", color = Color(0xFF0A66C2))
            }

            TextButton(onClick = { navController.navigate(NavigationScreens.SignUpScreen.name)
            }) {
                Text(text = "SignUp", color = Color(0xFF0A66C2))
            }
        }

    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color.Blue)

            }
        }
    }

}
