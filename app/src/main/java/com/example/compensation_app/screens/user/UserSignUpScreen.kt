package com.example.compensation_app.screens.user

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.User
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.viewmodel.GuardViewModel
import java.util.Calendar

@Composable
fun SignUpScreen(navController: NavController) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") } // Date of Birth
    val viewModel: GuardViewModel = hiltViewModel()
    var selectedDivision by remember { mutableStateOf<String?>(null) }
    var selectedSubDivision by remember { mutableStateOf<String?>(null) }
    var selectedRange by remember { mutableStateOf<String?>(null) }
    var selectedBeat by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val divisions = listOf("Raipur")
    val subDivisionsMap = mapOf("Raipur" to listOf("Hello"))
    val rangesMap = mapOf("Raipur-Hello" to listOf("Abhanpur"))
    val beatsMap = mapOf("Raipur-Hello-Abhanpur" to listOf("0", "1"))

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Sign Up", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("Enter your User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = rePassword,
            onValueChange = { rePassword = it },
            label = { Text("Re-enter Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Enter your Mobile Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Date of Birth Picker
        DatePickerField(label = "Select Date of Birth", selectedDate = dob) {
            dob = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Division Dropdown
        DropdownMenuField(
            label = "Select Division",
            options = divisions,
            selectedOption = selectedDivision,
            onOptionSelected = {
                selectedDivision = it
                selectedSubDivision = null
                selectedRange = null
                selectedBeat = null
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Sub-Division Dropdown
        selectedDivision?.let {
            DropdownMenuField(
                label = "Select Sub Division",
                options = subDivisionsMap[it] ?: emptyList(),
                selectedOption = selectedSubDivision,
                onOptionSelected = {
                    selectedSubDivision = it
                    selectedRange = null
                    selectedBeat = null
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Range Dropdown
        selectedSubDivision?.let {
            val key = "$selectedDivision-$selectedSubDivision"
            DropdownMenuField(
                label = "Select Range",
                options = rangesMap[key] ?: emptyList(),
                selectedOption = selectedRange,
                onOptionSelected = {
                    selectedRange = it
                    selectedBeat = null
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Beat Dropdown
        selectedRange?.let {
            val key = "$selectedDivision-$selectedSubDivision-$selectedRange"
            DropdownMenuField(
                label = "Select Beat",
                options = beatsMap[key] ?: emptyList(),
                selectedOption = selectedBeat,
                onOptionSelected = { selectedBeat = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Error Message
        errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Register Button


        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    errorMessage = validateInputs(userId, password, rePassword, name, mobileNumber, dob)
                    if (errorMessage == null) {
                        isLoading = true
                        val newUser = User(
                            UserId = userId,
                            Password = password,
                            name = name,
                            mobile_number = mobileNumber,
                            DOB = dob,
                            division = selectedDivision,
                            subdivision = selectedSubDivision,
                            range_ = selectedRange,
                            beat = selectedBeat
                        )
                        viewModel.addUser(user = newUser) { success, message ->
                            isLoading = false // Stop loading when response is received
                            if (!success) {
                                errorMessage = message
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text(text = "Register")
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        // Footer Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { navController.navigate(NavigationScreens.UserLoginScreen.name) }) {
                Text(text = "Already a User? Sign in", color = Color.Blue)
            }
            TextButton(onClick = { navController.navigate(NavigationScreens.LoginScreen.name) }) {
                Text(text = "Official Login", color = Color.Blue)
            }
        }
    }
}

// Date Picker Composable
@Composable
fun DatePickerField(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onDateSelected("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedButton(
        onClick = { datePickerDialog.show() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = if (selectedDate.isNotEmpty()) selectedDate else label, color = if (selectedDate.isEmpty()) Color.Gray else Color.Black)
    }
}

// Validation Function
fun validateInputs(userId: String, password: String, rePassword: String, name: String, mobileNumber: String, dob: String): String? {
    if (userId.isEmpty() || password.isEmpty() || rePassword.isEmpty() || name.isEmpty() || mobileNumber.isEmpty() || dob.isEmpty()) {
        return "All fields are required."
    }
    if (!userId.matches(Regex("^[a-zA-Z0-9]+$"))) {
        return "User ID must contain only alphabets and digits."
    }
    if (password != rePassword) {
        return "Passwords do not match."
    }
    return null
}

// Reusable DropdownMenuField Composable
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedOption ?: label, color = if (selectedOption == null) Color.Gray else Color.Black)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }, text = { Text(text = option) })
            }
        }
    }
}
