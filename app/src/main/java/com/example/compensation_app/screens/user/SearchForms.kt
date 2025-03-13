package com.example.compensation_app.screens.user
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.screens.guard.ApplicationItem
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComplaint(navController: NavController) {
    var complaintId by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var complaint by remember { mutableStateOf<UserComplaintRetrievalForm?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val viewModel: GuardViewModel = hiltViewModel()
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
                        text = "Check Status",
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
            //navigationIcon = { /* Navigation icon is handled within title */ }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Complaint ID Input
            OutlinedTextField(
                value = complaintId,
                onValueChange = { complaintId = it },
                label = { Text("Complaint ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mobile Number Input
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                label = { Text("Mobile Number") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }

            // Confirmation Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Submission") },
                    text = { Text("Do you want to fetch this complaint?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                isLoading = true
                                viewModel.getComplaint(complaintId, mobileNumber) { success, retrievedComplaint, error ->
                                    isLoading = false
                                    if (success && retrievedComplaint != null) {
                                        complaint = retrievedComplaint
                                        errorMessage = null
                                    } else {
                                        complaint = null
                                        errorMessage = error ?: "Unknown Error"
                                    }
                                }
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show Loading Indicator
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            // Show Error Message if any
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Show Complaint Details if found
            complaint?.let {
                ComplaintDetails(it,navController)
            }
        }

        // Display the Forms lis
    }


}

@Composable
fun ComplaintDetails(complaint: UserComplaintRetrievalForm,navController: NavController) {
    val gson = Gson()
    val jsonForm = gson.toJson(complaint)
    val encodedForm = URLEncoder.encode(jsonForm, StandardCharsets.UTF_8.toString())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate(NavigationScreens.RetrivalComplaintDisplayScreen.name+"/$encodedForm") },
        elevation =CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Complaint ID: ${complaint.complaint_id}", fontWeight = FontWeight.Bold)
            Text(text = "Name: ${complaint.name}", color = Color.Gray)
            Text(text = "Age: ${complaint.age}")
            Text(text = "Mobile: ${complaint.mobile}")
            Text(text = "Damage Date: ${complaint.damageDate}")
            Text(text = "Additional Details: ${complaint.additionalDetails}")
            // Add more fields as required
        }
    }
}
