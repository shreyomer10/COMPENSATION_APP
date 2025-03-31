package com.example.compensation_app.screens.user
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.UserComplaintForm
import com.example.compensation_app.FireStorage.ImageRow
import com.example.compensation_app.FireStorage.openPdfWithIntent
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.R
import com.example.compensation_app.components.CompleteFormSectionCard
import com.example.compensation_app.components.DetailRow
import com.example.compensation_app.components.InputField
import com.example.compensation_app.components.getStatusLabel
import com.example.compensation_app.screens.guard.showDownloadConfirmationDialog
import com.example.compensation_app.viewmodel.GuardViewModel
import showDownloadConfirmationDialogPDF

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySuccessScreen(navController: NavController,formData: UserComplaintForm, complaintId: Int) {
    val viewModel: GuardViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.sendEmail(
            formData.email,
            "Your compensation complaint is successfully submitted FormId:$complaintId and mobile number:${formData.mobile}."
        ) { result ->
            result.onSuccess {
                Log.d("Email", "Email sent successfully")
            }.onFailure {
                Log.e("Email", "Failed: ${it.message}")
            }
        }
    }

    val context= LocalContext.current
    val isLoading = remember { mutableStateOf(false) }
    Column(

    ) {
        androidx.compose.material3.TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.navigate(NavigationScreens.AppHome.name)
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                    Text(
                        text = "Submitted",
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
            navigationIcon = { }
        )



    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Complaint Submitted Successfully!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Complaint ID: $complaintId",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Note: Please save this Complaint ID or take a screenshot for future reference.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))
        if (isLoading.value) {
            CircularProgressIndicator() // Show loading indicator
        }
        else{
            Button(onClick = {
                showDownloadConfirmationDialogPDF(context,null, complaint = formData, isLoading = isLoading)
            }) {
                Text("Print Application Form")
            }
        }


    }
}
