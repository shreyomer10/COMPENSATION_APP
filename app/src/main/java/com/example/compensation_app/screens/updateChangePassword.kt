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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.VerifyGuardRequest
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Navigation.NavigationScreens
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
fun UpdateChangePass(navController: NavController) {

    BackHandler {
        navController.navigate(NavigationScreens.LoginScreen.name) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
    val auth = FirebaseAuth.getInstance()
    val viewModel:GuardViewModel= hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    var mobileNumber by remember { mutableStateOf("") }
    val formattedNumber = mobileNumber.replace("+91", "") ?: ""
    var guardId by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isOTPVerified by remember { mutableStateOf(false) }
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
    var timeLeft by remember { mutableStateOf(60) } // Time left for resend in seconds
    val context = LocalContext.current
    var guardVerified by remember {
        mutableStateOf(false)
    }

    var selectedRole by remember { mutableStateOf("forestguard") }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("forestguard", "deputyranger")
    val rolesMap = roles.associateWith { it.replaceFirstChar { ch -> ch.uppercase() } }

    var isLoading by remember { mutableStateOf(false) } // Loading state

    var showPasswordDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isSubmitEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(showToast) {
        showToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showToast = null
        }
    }

    LaunchedEffect(isOtpSent) {
        if (isOtpSent) {
            // Start the countdown for 1 minute
            for (i in 1..60) {
                kotlinx.coroutines.delay(1000L)
                timeLeft = 60 - i
            }
            // After 1 minute, re-enable the OTP resend button
            isOtpSent = false
            timeLeft = 60
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Text(
            text = "ANUGRAH - अनुग्रह",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Forgot Password",
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

        if(guardVerified){
            Text(text = "*Officer is verified ", color = Color.Green)
        }
        Button(
            onClick = {
                isLoading=true
                val verifyRequest = VerifyGuardRequest(
                    emp_id = guardId,
                    mobile_number = mobileNumber,
                    roll = selectedRole,
                    changePass = "1"
                )


                viewModel.verifyGuard(verifyRequest) { response, error ->
                    isLoading=false
                    if (response != null) {
                        guardVerified = true
                        // Handle success (e.g., update UI with the verification message)
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    } else {
                        guardVerified = false
                        // Handle error (e.g., show error message)
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A66C2)),
            //colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        ) {
            Text(text = "Verify Officer", color = Color.White)
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (mobileNumber.isNotEmpty() && guardId.isNotEmpty() && guardVerified) {
                    val formattedMobileNumber = "+91$mobileNumber"
                    sendOTP(auth, formattedMobileNumber, context) { id ->
                        verificationId = id
                    }
                    isOtpSent = true // Mark OTP as sent
                    showToast = "OTP sent to your registered mobile number"
                } else {
                    showToast = "Please enter mobile number and guard ID"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                //containerColor = Color.Blue, // Normal color
                containerColor = Color(0xFF0A66C2),
                disabledContainerColor = Color.Gray // Gray when disabled
            ),
            enabled = (!isOtpSent && guardVerified) // Disable button after OTP is sent
        ) {
            Text(text = "Send OTP ", color = Color.White)
        }


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("Enter received OTP ") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set text color to black

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                if (verificationId != null && otp.isNotEmpty()) {
                    verifyOTP(auth, verificationId!!, otp, context) {
                        isOTPVerified = true
                        showPasswordDialog = true
                        showToast = "OTP Verified"
                        Log.d("Guard Id", "LoginScreen: $emp")
                    }
                } else {
                    showToast = "Enter OTP to proceed "
                }
            },
            enabled = !isOTPVerified,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black,
                disabledContainerColor = Color.Gray)
        ) {
            Text(text = "Proceed", color = Color.White)
        }
        if (showPasswordDialog) {
            AlertDialog(
                onDismissRequest = { /* Do nothing to force the user to set the password */ },
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                ),
                title = { Text("Create Password") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Create Password") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                isSubmitEnabled = it == password && password.length >= 6
                            },
                            label = { Text("Re-enter Password") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showPasswordDialog = false
                            isLoading = true
                            viewModel.updatePass(
                                empId = guardId,
                                mobileNumber = mobileNumber,
                                roll = selectedRole,
                                password = password
                            ) { response, error ->
                                isLoading = false
                                if (response != null) {
                                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        enabled = isSubmitEnabled
                    ) {
                        Text("Submit")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    if (!isOtpSent) {
                        if (mobileNumber.isNotEmpty() && guardId.isNotEmpty()) {
                            val formattedMobileNumber = "+91$mobileNumber"
                            sendOTP(auth, formattedMobileNumber, context) { id ->
                                verificationId = id
                            }
                            isOtpSent = true // Mark OTP as sent
                        }
                    }
                },
                enabled = !isOtpSent // Disable the button if OTP is already sent
            ) {
                Text(text = if (isOtpSent) "Resend in $timeLeft sec " else "Resend OTP", color = Color(0xFF0A66C2))
            }

            TextButton(onClick = { navController.navigate(NavigationScreens.LoginScreen.name)
            }) {
                Text(text = "Already a user? Signin", color = Color(0xFF0A66C2))
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
