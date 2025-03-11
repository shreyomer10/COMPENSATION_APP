package com.example.compensation_app.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
            division = "", range_ = "", beat = 0))
    }

    Log.d("Final", "NewApplication:${emp.emp_id} ")
    // State to handle OTP resend and countdown
    var isOtpSent by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(60) } // Time left for resend in seconds
    val context = LocalContext.current
    val gson = Gson()
    var guardVerified by remember {
        mutableStateOf(false)
    }

    var selectedRole by remember { mutableStateOf("forestguard") }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("forestguard", "deputyranger")
    var GuardGson:String=""

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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text = "Official Login",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Role (अपनी भूमिका चुनें)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = true },
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role) },
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
            label = { Text("Enter your mobile number (अपना मोबाइल नंबर दर्ज करें)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guardId,
            onValueChange = { guardId = it },
            label = { Text("Enter your Officer ID (अपना गार्ड आईडी दर्ज करें)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(guardVerified){
            Text(text = "*Officer is verified (गार्ड की पुष्टि हो गई है)*", color = Color.Green)
        }
        Button(
            onClick = {
                viewModel.verifyGuard(
                    empId = guardId,
                    mobileNumber = mobileNumber,
                    roll =selectedRole
                ) { message, guard ->
                    Log.d("VERIFIATION", "LoginScreen: $message , $guard")

                    if (message == "Verified" && guard!=null) {
                        Log.d("actual", "LoginScreen: $guard")
                        guardVerified = true
                        emp=guard
                        GuardGson = URLEncoder.encode(gson.toJson(emp), "UTF-8")
                        Log.d("Guard", "LoginScreen: $emp")
                        Log.d("Guard GSON", "LoginScreen: $GuardGson")

                    } else {
                        guardVerified = false
                        showToast = "Please enter correct mobile number and guard ID (कृपया सही मोबाइल नंबर और गार्ड आईडी दर्ज करें)"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        ) {
            Text(text = "Verify Officer (गार्ड की पुष्टि करें)", color = Color.White)
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
                } else {
                    showToast = "Please enter mobile number and guard ID (कृपया मोबाइल नंबर और गार्ड आईडी दर्ज करें)"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            enabled = !isOtpSent // Disable button after OTP is sent
        ) {
            Text(text = "Send OTP (ओटीपी भेजें)", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("Enter received OTP (प्राप्त ओटीपी दर्ज करें)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (verificationId != null && otp.isNotEmpty()) {
                    verifyOTP(auth, verificationId!!, otp, context) {
                        isOTPVerified = true
                        showToast = "OTP Verified (ओटीपी सत्यापित किया गया)"
                        Log.d("Guard Id", "LoginScreen: $emp")
                        mainViewModel.GuardDetails(emp = emp)
                        if(emp.roll=="forestguard"){
                            navController.navigate(NavigationScreens.HomeScreen.name)

                        }
                        else if(emp.roll=="deputyranger"){
                            navController.navigate(NavigationScreens.DeputyHomeScreen.name)
                        }
                        //mainViewModel.GuardDetails(guard = gguard)
                    }
                } else {
                    showToast = "Enter OTP to proceed (आगे बढ़ने के लिए ओटीपी दर्ज करें)"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Proceed (आगे बढ़ें)", color = Color.White)
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
                Text(text = if (isOtpSent) "Resend in $timeLeft sec (पुनः भेजें $timeLeft सेकंड में)" else "Resend OTP (ओटीपी पुनः भेजें)", color = Color.Blue)
            }

            TextButton(onClick = { navController.navigate(NavigationScreens.AppHome.name)
            }) {
                Text(text = "Dashboard", color = Color.Blue)
            }
        }

    }
}
