package com.example.compensation_app.screens.guard.complaints

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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.compensation_app.Backend.UserComplaintRetrievalFormShort
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.Navigation.logoutUser
import com.example.compensation_app.components.getStatusLabel
import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintApplicationScreen(navController: NavController,guard: String?) {
    //val text="Guard"
    val gson = Gson()

    val mainViewModel: MainViewModel = hiltViewModel()
    var gguard by remember {
        mutableStateOf<emp>(emp.default())
    }
    LaunchedEffect (Unit){
        mainViewModel.GetGuard {
            if (it != null) {
                gguard=it
            }
        }

    }
    val viewModel: GuardViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(true) }  // Track loading state
    var forms by remember { mutableStateOf<List<UserComplaintRetrievalFormShort>>(emptyList()) }

    LaunchedEffect(gguard) {
        if (gguard != null && gguard.emp_id.isNotEmpty()) {
            // Step 1: Load cached complaints
            isLoading = true
            mainViewModel.getAllCompelaintShortCache { cachedForms ->
                forms = cachedForms
            }
            // Wait for a moment to ensure cachedForms updated
            // (getAllCompelaintShortCache callback is async, so better to suspend or redesign it)
            // Assuming getAllCompelaintShortCache can be made suspend, or use a suspend version:
            // For example:
            // val cachedForms = mainViewModel.getAllCompelaintShortCacheSuspend()
            // forms = cachedForms

            // Step 2: After cache loading done (simulate with delay if needed)
            // or better: use a state or suspend function to await completion

            // If cache empty, fetch fresh from API
            if (forms.isEmpty()) {
                viewModel.fetchGuardComplaints(gguard.emp_id) { fetchedForms, status, code ->
                    isLoading = false
                    if (!fetchedForms.isNullOrEmpty()) {
                        forms = fetchedForms
                        mainViewModel.addCompelaintShortCache(fetchedForms)
                    } else if (code == 401 || code == 403) {
                        logoutUser(navController, mainViewModel, gguard)
                    }
                }
            } else {
                isLoading = false
            }
        } else {
            isLoading = false
        }
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
            LazyColumn {
                if (forms.isNotEmpty()) {
                    items(forms) { form ->
                        ApplicationItem(form=form, navController = navController, guard = guard)
                    }
                } else {
                    item {
                        Text(text = "No applications found", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }

        // Display the Forms list

    }
}


@Composable
fun ApplicationItem(navController: NavController, form: UserComplaintRetrievalFormShort,guard: String?) {
    val gson = Gson()
    val jsonForm = gson.toJson(form)
    val encodedForm = URLEncoder.encode(jsonForm, StandardCharsets.UTF_8.toString())
    val statusString = form.status?.let { getStatusLabel(it) }

    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Application ID: ${form.complaint_id}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
            )

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
            Text(
                text = "Status: $statusString",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
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
