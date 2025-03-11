package com.example.compensation_app.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compensation_app.Navigation.NavigationScreens
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopAppBar(
    navController: NavController,
    greetings: String
) {
    val auth = FirebaseAuth.getInstance()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Use padding for the status bar
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    val backgroundColor = Color(0xFF4379FF) // A darker color for contrast
    val contentColor = Color.White // White text and icons for readability

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .systemBarsPadding()
            .padding(statusBarPadding) // Add padding for status bar
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logout button
                IconButton(onClick = { showLogoutDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp, // Changed to logout icon
                        contentDescription = "Logout",
                        tint = contentColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = greetings, // Adjusted text
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.width(40.dp))
            }
        }
    }

    // Logout dialog logic
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    "Are you sure you want to logout?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        SignOut(navController)
                    }
                ) {
                    Text("Yes, Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun SignOut(navController: NavController){

    FirebaseAuth.getInstance().signOut()
    navController.navigate(NavigationScreens.LoginScreen.name){
        popUpTo(0)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}


@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType,enabled:Boolean=true) {
    val textStyle = remember { TextStyle(color = Color.Black) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        textStyle = textStyle,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}
fun getStatusLabel(status: String): String {
    return when (status.toIntOrNull()) {
        1 -> "Forest Guard Level (वन रक्षक स्तर)"
        2 -> "Deputy Ranger Level (उप-रेंजर स्तर)"
        3 -> "Ranger Level (रेंजर स्तर)"
        4 -> "Subdivision Level (उपमंडल स्तर)"
        5 -> "Division Level (मंडल स्तर)"
        6 -> "Payment Processed (भुगतान संसाधित)"
        -1 -> "Rejected (अस्वीकृत)"
        else -> "Unknown Status (अज्ञात स्थिति)"
    }
}
fun getCurrentTimestamp(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
}
// **Save Draft Dialog**
@Composable
fun RequiredDocumentsTable() {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = "Required Documents (आवश्यक दस्तावेज़)",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Category",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Required Documents",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(2f)
            )
        }

        // Table Content
        val documentList = listOf(
            "General" to "Aadhar, PAN, Passport Photo, Signature, Incident Photos - 3",
            "Human Injury" to "Injury Photo, Medical Certificate",
            "Death" to "Death Certificate, Sarpanch Report",
            "House Damage" to "4 Photos, R.I Report, ROR/Sale Deed Copy",
            "Cattle Kill" to "Photo, VAS Certificate"
        )

        documentList.forEach { (category, documents) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = documents,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}

@Composable
fun DatePickerField(label: String,selectedDate: String, onDateChange: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = remember { java.util.Calendar.getInstance() }
    val year = calendar.get(java.util.Calendar.YEAR)
    val month = calendar.get(java.util.Calendar.MONTH)
    val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                onDateChange("$selectedDay/${selectedMonth + 1}/$selectedYear")
            },
            year,
            month,
            day
        ).apply {
            datePicker.maxDate = calendar.timeInMillis // Disable future dates
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { datePickerDialog.show() }, // ✅ Click anywhere to open calendar
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.DateRange,
            contentDescription = "Select Date",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (selectedDate.isNotEmpty()) selectedDate else "Select Date",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DamageDetailsDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor() // Aligns the menu properly with the text field
                .clickable { expanded = !expanded }, // Ensures clicking opens the dropdown
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Filled.ArrowDropDown,
//                    contentDescription = "Dropdown Icon",
//                    modifier = Modifier.clickable { expanded = !expanded } // Toggles the dropdown
//                )
//            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Composable
fun SaveDraftDialog(onSave: () -> Unit, onDiscard: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Save Changes?") },
        text = { Text("Do you want to save the draft before exiting?") },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDiscard) {
                Text("Discard")
            }
        }
    )
}
@Composable
fun CompleteFormSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
            content()
        }
    }
}