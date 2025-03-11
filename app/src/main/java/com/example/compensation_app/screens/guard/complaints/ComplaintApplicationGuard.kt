package com.example.compensation_app.screens.guard.complaints

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintApplicationScreen(navController: NavController,guard: String?) {
    //val text="Guard"
    val gson = Gson()
    val gguard = guard?.let {
        gson.fromJson(it, emp::class.java)
    }
    val viewModel: GuardViewModel = hiltViewModel()
//    val auth = FirebaseAuth.getInstance()
//    val mobi  leNumber = auth.currentUser?.phoneNumber
//    val formattedNumber = mobileNumber?.replace("+91", "") ?: ""
//    Log.d("format", "$formattedNumber")
//    Log.d("Ky problem hai", "PrevApplicationScreen: $gguard")
    var Forms by remember {
        mutableStateOf<List<UserComplaintRetrievalForm>>(emptyList())
    }
    if (gguard!=null)
        if (gguard.emp_id.isNotEmpty()) {
            viewModel.fetchGuardComplaints(guardId = gguard.emp_id){ forms,status->
                if (forms != null && forms.isNotEmpty()) {
                    Log.d("Initiasl", "PrevApplicationScreen: $forms")
                    Forms = forms
                    Log.d("Forms", "Fetched Forms: $Forms")
                } else {
                    Log.d("Forms", "No forms found or error: $status")
                }
            }
//            viewModel.getFormsByID(GuardId = gguard.emp_id) { forms, message ->
//                if (forms != null && forms.isNotEmpty()) {
//                    Log.d("Initiasl", "PrevApplicationScreen: $forms")
//                    Forms = forms
//                    Log.d("Forms", "Fetched Forms: $Forms")
//                } else {
//                    Log.d("Forms", "No forms found or error: $message")
//                }
//            }
        }
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
                        text = "Complaint Applications",
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
            navigationIcon = { /* Navigation icon is handled within title */ }
        )

        // Display the Forms list
        LazyColumn {
            if (Forms.isNotEmpty()) {
                items(Forms) { form ->
                    ApplicationItem(form=form, navController = navController, guard = guard)
                }
            } else {
                item {
                    Text(text = "No applications found", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}


@Composable
fun ApplicationItem(navController: NavController, form: UserComplaintRetrievalForm,guard: String?) {
    val gson = Gson()
    val jsonForm = gson.toJson(form)
    val encodedForm = URLEncoder.encode(jsonForm, StandardCharsets.UTF_8.toString())

    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Applicant Name: ${form.name}",
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
            Button(
                onClick = {
                    navController.navigate(NavigationScreens.CompleteComplaintGuardScreen.name + "/$encodedForm"+"/$guard")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "View Full Application")
            }
        }
    }
}
