package com.example.compensation_app.screens.deputyRanger

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.screens.guard.ApplicationItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(navController: NavController, name:String, emp: emp?, forms:List<RetrivalFormShort>?){
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
                        text = "$name ",
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
            if (forms != null) {
                if (forms.isNotEmpty()) {
                    items(forms) { form ->
                        ApplicationItem(form = form, navController = navController, text = name)
                    }
                } else {
                    item {
                        Text(text = "No applications found", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }


}