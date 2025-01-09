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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compensation_app.Navigation.Navigation
import com.example.compensation_app.ui.theme.COMPENSATION_APPTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.appcheck.FirebaseAppCheck


val LocalNavController = compositionLocalOf<NavHostController> {
    error("No NavController provided")
}
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAppCheck.getInstance()
                .installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
            COMPENSATION_APPTheme {
                navController= rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    Navigation()
                }
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
