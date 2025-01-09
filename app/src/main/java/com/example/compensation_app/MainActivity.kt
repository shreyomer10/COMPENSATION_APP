package com.example.compensation_app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compensation_app.ui.theme.COMPENSATION_APPTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.appcheck.FirebaseAppCheck


class MainActivity : ComponentActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAppCheck.getInstance()
                .installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
            COMPENSATION_APPTheme {
                ForestGuardLoginScreen(auth = auth)
            }
        }
    }


}

@Composable
fun ForestGuardLoginScreen(auth: FirebaseAuth) {
    var mobileNumber by remember { mutableStateOf("") }
    var guardId by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isOTPVerified by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(showToast) {
        showToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showToast = null
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
            text = "Forest Guard Login",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = mobileNumber,
            onValueChange = {
                if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                    mobileNumber = it
                }
            },
            label = { Text("Enter your mobile number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guardId,
            onValueChange = { guardId = it },
            label = { Text("Enter your guard ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (mobileNumber.isNotEmpty() && guardId.isNotEmpty()) {
                    val formattedMobileNumber = "+91$mobileNumber"
                    sendOTP(auth, formattedMobileNumber, context) { id ->
                        verificationId = id
                    }
                } else {
                    showToast = "Please enter mobile number and guard ID"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Send OTP", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("Enter received OTP") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (verificationId != null && otp.isNotEmpty()) {
                    verifyOTP(auth, verificationId!!, otp, context) {
                        isOTPVerified = true
                    }
                } else {
                    showToast = "Enter OTP to proceed"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Proceed", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isOTPVerified) {
            showToast = "OTP Verified"
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { /* TODO: Resend OTP */ }) {
                Text(text = "Resend OTP", color = Color.Blue)
            }

            TextButton(onClick = { /* TODO: Need Help */ }) {
                Text(text = "Need Help?", color = Color.Blue)
            }
        }
    }
}

fun sendOTP(
    auth: FirebaseAuth,
    mobileNumber: String,
    context: Context,
    onVerificationIdReceived: (String) -> Unit
) {
    val activity = context as? android.app.Activity

    if (activity != null) {
        val phoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobileNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    auth.signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Handle success
                            } else {
                                // Handle failure
                            }
                        }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(activity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onVerificationIdReceived(verificationId)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    } else {
        Toast.makeText(activity, "Context is not an activity", Toast.LENGTH_SHORT).show()
    }
}

fun verifyOTP(
    auth: FirebaseAuth,
    verificationId: String,
    otp: String,
    context: Context,
    onVerified: () -> Unit
) {
    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onVerified()
            } else {
                Toast.makeText(context, "OTP Verification Failed", Toast.LENGTH_SHORT).show()
            }
        }
}
