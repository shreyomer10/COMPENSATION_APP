package com.example.compensation_app.screens.user
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.viewmodel.GuardViewModel


@Composable
fun SearchComplaint(navController: NavController) {
    var complaintId by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var complaint by remember { mutableStateOf<UserComplaintRetrievalForm?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val viewModel: GuardViewModel = hiltViewModel()

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
            ComplaintDetails(it)
        }
    }
}

@Composable
fun ComplaintDetails(complaint: UserComplaintRetrievalForm) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
