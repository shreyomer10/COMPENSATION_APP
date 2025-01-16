package com.example.compensation_app.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.CompensationForm
import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.Guard
import com.example.compensation_app.Backend.validate
import com.example.compensation_app.components.DamageDetailsDropdown
import com.example.compensation_app.components.DatePickerField
import com.example.compensation_app.components.InputField
import com.example.compensation_app.components.SectionTitle
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewApplication(navController: NavController) {
    // Data class instance to hold form data.visibility = View.GONE
    val viewModel:GuardViewModel= hiltViewModel()
    val auth = FirebaseAuth.getInstance()
    val mobileNumber = auth.currentUser?.phoneNumber
    val formattedNumber = mobileNumber?.replace("+91", "") ?: ""
    Log.d("format", "$formattedNumber")
    var gguard by remember {
        mutableStateOf<Guard>(Guard(emp_id = "",
            mobile_number = "",
            name = "",
            division = "", range_ = "", beat = 0))
    }
    viewModel.searchByMobile(mobile = formattedNumber) {guard, Message->
        if (guard != null) {
            Log.d("Final 0", "NewApplication: $guard")
            gguard=guard
            println("Guard fetched: $guard")
        } else {
            println("Error: $Message")
        }
    }
    Log.d("Final", "NewApplication:${gguard.emp_id} ")


    Log.d("User", "NewApplication: $mobileNumber")
    var formData by remember { mutableStateOf(FormData()) }
   // var animalExpanded by remember { mutableStateOf(false) } // For dropdown menu
    var cropDamageChecked by remember { mutableStateOf(false) }
    var houseDamageChecked by remember { mutableStateOf(false) }
    var cattleInjuryChecked by remember { mutableStateOf(false) }
    var humanDeathChecked by remember { mutableStateOf(false) }
    var humanInjuryChecked by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf<String?>(null) }
val context= LocalContext.current



    LaunchedEffect(showToast) {
        showToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showToast = null
        }
    }
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "New Applications",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = TopAppBarDefaults.topAppBarColors(Color(0xFFFFFFFF)),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()/* Handle back navigation */
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
            },

            )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Applicant Details Section
            item {
                SectionTitle("Applicant Details (आवेदक विवरण)")
                InputField(
                    label = "Name (नाम)",
                    value = formData.name,
                    onValueChange = { formData = formData.copy(name = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "Age (उम्र)",
                    value = formData.age,
                    onValueChange = { formData = formData.copy(age = it) },
                    keyboardType = KeyboardType.Number
                )
                InputField(
                    label = "Father/Spouse Name (पिता/पति का नाम)",
                    value = formData.fatherOrSpouseName,
                    onValueChange = { formData = formData.copy(fatherOrSpouseName = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "Mobile (मोबाइल)",
                    value = formData.mobile,
                    onValueChange = { if (it.length <= 10) formData = formData.copy(mobile = it) },
                    keyboardType = KeyboardType.Number
                )
            }

            // Damage Details Section
            item {
                SectionTitle("Damage Details (नुकसान विवरण)")

                // Dropdown for Animal List
                DamageDetailsDropdown(
                    label = "Animal List (जानवरों की सूची)",
                    options = listOf(
                        "Elephant (हाथी)",
                        "Leopard (तेंदुआ)",
                        "Tiger (बाघ)",
                        "Sloth Bear (कंबल भालू)",
                        "Hyena (हाइना)",
                        "Wild Boar (जंगली सूअर)",
                        "Jungle Cat (जंगल बिल्ली)"
                    ),
                    selectedOption = formData.animalList,
                    onOptionSelected = { formData = formData.copy(animalList = it) }
                )

                // Date Picker
                DatePickerField(
                    label = "Damage Date (नुकसान की तारीख)",
                    selectedDate = formData.damageDate,
                    onDateChange = { formData = formData.copy(damageDate = it) }
                )

                InputField(
                    label = "Additional Details (अतिरिक्त विवरण)",
                    value = formData.additionalDetails,
                    onValueChange = { formData = formData.copy(additionalDetails = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "Address (पता)",
                    value = formData.address,
                    onValueChange = { formData = formData.copy(address = it) },
                    keyboardType = KeyboardType.Text
                )
            }

            // Crop Damage Section
            item {
                SectionTitle("Crop Damage (फसल का नुकसान)")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = cropDamageChecked,
                        onCheckedChange = { cropDamageChecked = it })
                    Text(
                        "Crop Damage Details Required (फसल का नुकसान विवरण आवश्यक है)",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                if (cropDamageChecked) {
                    InputField(
                        label = "Crop Type (फसल का प्रकार)",
                        value = formData.cropType,
                        onValueChange = { formData = formData.copy(cropType = it) },
                        keyboardType = KeyboardType.Text
                    )
                    InputField(
                        label = "Cereal Crop (अनाज की फसल)",
                        value = formData.cerealCrop,
                        onValueChange = { formData = formData.copy(cerealCrop = it) },
                        keyboardType = KeyboardType.Text
                    )
                    InputField(
                        label = "Crop Damage Area (फसल नुकसान क्षेत्र)",
                        value = formData.cropDamageArea,
                        onValueChange = { formData = formData.copy(cropDamageArea = it) },
                        keyboardType = KeyboardType.Decimal
                    )
                }
            }

            // House Damage Section
            item {
                SectionTitle("House Damage (घर का नुकसान)")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = houseDamageChecked,
                        onCheckedChange = { houseDamageChecked = it })
                    Text(
                        "House Damage Details Required (घर का नुकसान विवरण आवश्यक है)",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                if (houseDamageChecked) {
                    InputField(
                        label = "Full Houses Damaged (पूर्ण घरों का नुकसान)",
                        value = formData.fullHousesDamaged,
                        onValueChange = { formData = formData.copy(fullHousesDamaged = it) },
                        keyboardType = KeyboardType.Text
                    )
                    InputField(
                        label = "Partial Houses Damaged (आंशिक घरों का नुकसान)",
                        value = formData.partialHousesDamaged,
                        onValueChange = { formData = formData.copy(partialHousesDamaged = it) },
                        keyboardType = KeyboardType.Text
                    )
                }
            }

            // Cattle Injury Section
            item {
                SectionTitle("Cattle Injury (पशु चोट)")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = cattleInjuryChecked,
                        onCheckedChange = { cattleInjuryChecked = it })
                    Text(
                        "Cattle Injury Details Required (पशु चोट विवरण आवश्यक है)",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                if (cattleInjuryChecked) {
                    InputField(
                        label = "Number of Individuals (व्यक्तियों की संख्या)",
                        value = formData.cattleInjuryNumber,
                        onValueChange = { formData = formData.copy(cattleInjuryNumber = it) },
                        keyboardType = KeyboardType.Number
                    )
                    InputField(
                        label = "Estimated Age (अनुमानित आयु)",
                        value = formData.cattleInjuryEstimatedAge,
                        onValueChange = { formData = formData.copy(cattleInjuryEstimatedAge = it) },
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Human Death Section
            item {
                SectionTitle("Human Death (मानव मृत्यु)")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = humanDeathChecked,
                        onCheckedChange = { humanDeathChecked = it })
                    Text(
                        "Human Death Details Required (मानव मृत्यु विवरण आवश्यक है)",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                if (humanDeathChecked) {
                    InputField(
                        label = "Victim Names (पीड़ितों के नाम)",
                        value = formData.humanDeathVictimNames,
                        onValueChange = { formData = formData.copy(humanDeathVictimNames = it) },
                        keyboardType = KeyboardType.Text
                    )
                    InputField(
                        label = "Number of Individuals (व्यक्तियों की संख्या)",
                        value = formData.humanDeathNumber,
                        onValueChange = { formData = formData.copy(humanDeathNumber = it) },
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Human Injury Section
            item {
                SectionTitle("Human Injury (मानव चोट)")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = humanInjuryChecked,
                        onCheckedChange = { humanInjuryChecked = it })
                    Text(
                        "Human Injury Details Required (मानव चोट विवरण आवश्यक है)",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                if (humanInjuryChecked) {
                    InputField(
                        label = "Temporary Injury Details (अस्थायी चोट विवरण)",
                        value = formData.temporaryInjuryDetails,
                        onValueChange = { formData = formData.copy(temporaryInjuryDetails = it) },
                        keyboardType = KeyboardType.Text
                    )
                    InputField(
                        label = "Permanent Injury Details (स्थायी चोट विवरण)",
                        value = formData.permanentInjuryDetails,
                        onValueChange = { formData = formData.copy(permanentInjuryDetails = it) },
                        keyboardType = KeyboardType.Text
                    )
                }
            }

            // Bank Details Section
            item {
                SectionTitle("Bank Details (बैंक विवरण)")
                InputField(
                    label = "Name (नाम)",
                    value = formData.bankName,
                    onValueChange = { formData = formData.copy(bankName = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "IFSC Code (IFSC कोड)",
                    value = formData.ifscCode,
                    onValueChange = { formData = formData.copy(ifscCode = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "Branch (शाखा)",
                    value = formData.bankBranch,
                    onValueChange = { formData = formData.copy(bankBranch = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "Account Holder Name (खाता धारक का नाम)",
                    value = formData.bankHolderName,
                    onValueChange = { formData = formData.copy(bankHolderName = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "Account Number (खाता संख्या)",
                    value = formData.bankAccountNumber,
                    onValueChange = { formData = formData.copy(bankAccountNumber = it) },
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                SectionTitle("Documents (दस्तावेज)")
                InputField(
                    label = "PAN Number (PAN नंबर)",
                    value = formData.pan,
                    onValueChange = { if (it.length <= 10) formData = formData.copy(pan = it) },
                    keyboardType = KeyboardType.Text
                )
                InputField(
                    label = "Aadhar Number (आधार नंबर)",
                    value = formData.adhar,
                    onValueChange = { if (it.length <= 12) formData = formData.copy(adhar = it) },
                    keyboardType = KeyboardType.Number
                )
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        val form = CompensationForm(
                            forestGuardID = gguard.emp_id,
                            applicantName = formData.name,
                            age = formData.age.toInt(),
                            fatherSpouseName = formData.fatherOrSpouseName,
                            mobile = formData.mobile,
                            animalName = formData.animalList,
                            incidentDate = formData.damageDate,
                            additionalDetails = formData.additionalDetails,
                            address = formData.address,
                            cropType = formData.cropType,
                            cerealCrop = formData.cerealCrop,
                            cropDamageArea = formData.cropDamageArea.toDoubleOrNull() ?: 0.0,
                            fullHouseDamage = formData.fullHousesDamaged,
                            partialHouseDamage = formData.partialHousesDamaged,
                            numberOfCattlesDied = formData.cattleInjuryNumber.toIntOrNull() ?: 0,
                            estimatedCattleAge = formData.cattleInjuryEstimatedAge.toIntOrNull()
                                ?: 0,
                            humanDeathVictimName = formData.humanDeathVictimNames,
                            numberOfDeaths = formData.humanDeathNumber.toIntOrNull() ?: 0,
                            temporaryInjuryDetails = formData.temporaryInjuryDetails,
                            permanentInjuryDetails = formData.permanentInjuryDetails,
                            bankName = formData.bankName,
                            ifscCode = formData.ifscCode,
                            branchName = formData.bankBranch,
                            accountHolderName = formData.bankHolderName,
                            accountNumber = formData.bankAccountNumber,
                            panNumber = formData.pan,
                            aadhaarNumber = formData.adhar,
                            status = "",
                            verifiedBy = "Pending",
                            paymentProcessedBy = "Pending"
                        )
                        // Add form validation and submission logic here
                        if (validate(form)) {
                            Log.d("validity", "NewApplication: Valid hai")



                            viewModel.newApplicationForm(form = form) { success, status ->
                                Log.d("view model ke andar ", "NewApplication:ky bhau ")

                                if (success) {
                                    showToast = "Form submitted successfully!"
                                } else {
                                    Log.d("status", "NewApplication: $status")
                                    showToast = "Error: $status"
                                }

                            }
                        }
                        else{
                            showToast="Please Check all fields (कृपया सभी फ़ील्ड्स की जांच करें)"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Application (आवेदन सबमिट करें)")
                }
            }

        }
    }
}
