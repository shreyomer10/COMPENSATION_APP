package com.example.compensation_app.screens.user
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compensation_app.Backend.UserComplaintForm
import com.example.compensation_app.R
@Composable
fun DisplaySuccessScreen(formData: UserComplaintForm, complaintId: Int) {
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

        Button(onClick = {
            // Navigate back to home or another screen
        }) {
            Text("Go to Home")
        }
    }
}
