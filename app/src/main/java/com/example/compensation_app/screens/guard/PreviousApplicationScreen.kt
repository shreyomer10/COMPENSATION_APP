package com.example.compensation_app.screens.guard

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.components.getStatusLabel
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrevApplicationScreen(navController: NavController,guard: String?) {
    val text="Guard"
    val gson = Gson()
    val gguard = guard?.let {
        gson.fromJson(it, emp::class.java)
    }
    var isLoading by remember { mutableStateOf(true) }  // Track loading state

    val viewModel: GuardViewModel = hiltViewModel()
    val auth = FirebaseAuth.getInstance()
    val mobileNumber = auth.currentUser?.phoneNumber
    val formattedNumber = mobileNumber?.replace("+91", "") ?: ""
    Log.d("format", "$formattedNumber")
    Log.d("Ky problem hai", "PrevApplicationScreen: $gguard")
    var Forms by remember {
        mutableStateOf<List<RetrivalForm>>(emptyList())
    }

    var PendingForYou by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var Pending by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var Accepted by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var Rejected by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    if (gguard!=null)
    if (gguard.emp_id.isNotEmpty()) {
        viewModel.getFormsByID(GuardId = gguard.emp_id) { forms, message ->
            isLoading=false
            if (forms != null && forms.isNotEmpty()) {
                Log.d("Initiasl", "PrevApplicationScreen: $forms")
                Forms = forms
                PendingForYou = forms.filter { it.status == "1" }
                Pending = forms.filter { it.status in arrayOf("2","3", "4", "5") }
                Accepted = forms.filter { it.status == "6" }
                Rejected = forms.filter { it.status == "-1" }
                Log.d("Forms", "Fetched Forms: $Forms")
            } else {
                Log.d("Forms", "No forms found or error: $message")
            }
        }
    }

    val encodedPending = URLEncoder.encode(gson.toJson(Pending), StandardCharsets.UTF_8.toString())
    val encodedPendingForYou = URLEncoder.encode(gson.toJson(PendingForYou), StandardCharsets.UTF_8.toString())
    val encodedAccepted = URLEncoder.encode(gson.toJson(Accepted), StandardCharsets.UTF_8.toString())
    val encodedRejected = URLEncoder.encode(gson.toJson(Rejected), StandardCharsets.UTF_8.toString())
    val encodedEmpJson = URLEncoder.encode(gson.toJson(gguard), StandardCharsets.UTF_8.toString())

    Column {
        androidx.compose.material3.TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack() // Handle back navigation
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                    Text(
                        text = "Previous Applications",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = TopAppBarDefaults.topAppBarColors(Color(0xFFFFFFFF)),
        )
        if(isLoading){
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
        else{
            Spacer(modifier = Modifier.height(32.dp))

            // Center the buttons vertically
            // Center the buttons vertically
            Button(
                onClick = { navController.navigate(NavigationScreens.PendingForYouScreen.name + "/$encodedEmpJson/$encodedPendingForYou") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
            ) {
                Text(text = "Pending (For You)", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(NavigationScreens.PendingScreen.name + "/$encodedEmpJson/$encodedPending") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
            ) {
                Text(text = "Pending", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(NavigationScreens.AcceptedScreen.name + "/$encodedEmpJson/$encodedAccepted") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
            ) {
                Text(text = "Accepted", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(NavigationScreens.RejectedScreen.name + "/$encodedEmpJson/$encodedRejected") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFFF4C4C))
            ) {
                Text(text = "Rejected", color = Color.White)
            }
        }


    }
}


@Composable
fun ApplicationItem(navController: NavController, form: RetrivalForm,text:String) {
    val gson = Gson()
    val jsonForm = gson.toJson(form)
    val encodedForm = URLEncoder.encode(jsonForm, StandardCharsets.UTF_8.toString())
    val statusString = form.status?.let { getStatusLabel(it) }
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(150.dp),
        //colors = CardDefaults.cardColors(Color(0xFF9AA6B2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Application ID: ${form.formID}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Applicant Name: ${form.applicantName}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Mobile: ${form.mobile}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Status: $statusString",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Button(
                onClick = {
                    navController.navigate(NavigationScreens.CompleteFormScreen.name + "/$encodedForm" +"/$text")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
            ) {
                Text(text = "View Full Application", color = Color.White)
            }
        }
    }
}
