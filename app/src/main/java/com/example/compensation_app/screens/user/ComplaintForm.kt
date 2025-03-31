package com.example.compensation_app.screens.user


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.CompensationForm
import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.StatusUpdate
import com.example.compensation_app.Backend.UserComplaintForm
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Backend.validate
import com.example.compensation_app.Backend.validateComplaint
import com.example.compensation_app.FireStorage.FileDetails
import com.example.compensation_app.FireStorage.ImagePickerButton
import com.example.compensation_app.FireStorage.uploadComplaintFiles
import com.example.compensation_app.FireStorage.uploadFormFiles
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.components.DamageDetailsDropdown
import com.example.compensation_app.components.DatePickerField
import com.example.compensation_app.components.InputField
import com.example.compensation_app.components.RequiredDocumentsTable
import com.example.compensation_app.components.SaveDraftDialog
import com.example.compensation_app.components.SectionTitle
import com.example.compensation_app.components.getCurrentTimestamp

import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.viewmodel.GuardViewModel
import com.example.landareacalculator.ControlButtons
import com.example.landareacalculator.LandAreaTracker
import com.example.landareacalculator.MapScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintForm(navController: NavController) {
    var formData by remember { mutableStateOf(UserComplaintForm()) }


    val gson = Gson()
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val viewModel:GuardViewModel= hiltViewModel()
    val mainViewModel:MainViewModel= hiltViewModel()
    var isUploading by remember { mutableStateOf(false) }


    // var animalExpanded by remember { mutableStateOf(false) } // For dropdown menu
    var cropDamageChecked by remember { mutableStateOf(false) }
    var houseDamageChecked by remember { mutableStateOf(false) }
    var cattleInjuryChecked by remember { mutableStateOf(false) }
    var humanDeathChecked by remember { mutableStateOf(false) }
    var humanInjuryChecked by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf<String?>(null) }
    val context= LocalContext.current




    var selectedDivision by remember { mutableStateOf("") }
    var selectedSubdivision by remember { mutableStateOf("") }
    var selectedRange by remember { mutableStateOf("") }
    var selectedCircle by remember { mutableStateOf("") }
    var selectedBeat by remember { mutableStateOf("") }

    val divisions = listOf("Raipur")
    val subdivisions = mapOf("Raipur" to listOf("hello"))
    val ranges = mapOf("hello" to listOf("Abhanpur"))
    val circles = mapOf("Abhanpur" to listOf("hello"))
    val beats = mapOf("hello" to listOf("Beat 1", "Beat 0"))



    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var eSignUri by remember { mutableStateOf<Uri?>(null) }

    var incident1 by remember { mutableStateOf<Uri?>(null) }
    var incident2 by remember { mutableStateOf<Uri?>(null) }
    var incident3 by remember { mutableStateOf<Uri?>(null) }
    var incident4 by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(showToast) {
        showToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showToast = null
        }
    }


    Column (modifier = Modifier.fillMaxSize()){
        androidx.compose.material3.TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                    Text(
                        text = "New Complaint",
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
        if (isUploading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Dim background
                    .clickable(enabled = false) {} // Prevent interaction
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center),
                    color = Color.White,
                    strokeWidth = 6.dp
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {

            Column ( modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())){





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
                InputField(
                    label = "Email",
                    value = formData.email,
                    onValueChange = { formData = formData.copy(email = it) },
                    keyboardType = KeyboardType.Text
                )
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
                DropdownMenuComponent("Select Division",
                    divisions, selectedDivision) {
                    selectedDivision = it;
                    formData.division=it;
                    formData.subdivision="";
                    formData.range_="";
                    formData.circle1="";
                    formData.beat="";
                    selectedSubdivision = "" }
                if (selectedDivision.isNotEmpty()) {
                    DropdownMenuComponent("Select Subdivision",
                        subdivisions[selectedDivision] ?: emptyList(), selectedSubdivision) {
                        selectedSubdivision = it;
                        formData.subdivision=it;
                        formData.range_="";
                        formData.circle1="";
                        formData.beat="";
                        selectedRange = "";  }
                }
                if (selectedSubdivision.isNotEmpty()) {
                    DropdownMenuComponent("Select Range",
                        ranges[selectedSubdivision] ?: emptyList(), selectedRange) {
                        selectedRange = it;
                        formData.range_=it;
                        formData.circle1="";
                        formData.beat="";
                        selectedCircle = "" }
                }
                if (selectedRange.isNotEmpty()) {
                    DropdownMenuComponent("Select Circle",
                        circles[selectedRange] ?: emptyList(), selectedCircle) {
                        selectedCircle = it;

                        formData.circle1=it;
                        formData.beat="";
                        selectedBeat = "" }
                }
                if (selectedCircle.isNotEmpty()) {
                    DropdownMenuComponent("Select Beat",
                        beats[selectedCircle] ?: emptyList(), selectedBeat) {
                        selectedBeat = it
                        formData.beat=it;}
                }
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


                }
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

                SectionTitle("Upload Photos")

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImagePickerButton("Select Passport Size Photo") { uri -> photoUri = uri }
                    photoUri?.let { Text("Selected: $it", fontSize = 14.sp) }

                    Spacer(modifier = Modifier.height(8.dp))

                    ImagePickerButton("Select E-Sign") { uri -> eSignUri = uri }
                    eSignUri?.let { Text("Selected: $it", fontSize = 14.sp) }

                    Spacer(modifier = Modifier.height(8.dp))

                    ImagePickerButton("Select Incident Photo 1") { uri -> incident1 = uri }
                    incident1?.let { Text("Selected: $it", fontSize = 14.sp) }

                    Spacer(modifier = Modifier.height(8.dp))

                    ImagePickerButton("Select Incident Photo 2") { uri -> incident2 = uri }
                    incident2?.let { Text("Selected: $it", fontSize = 14.sp) }

                    Spacer(modifier = Modifier.height(8.dp))

                    ImagePickerButton("Select Incident Photo 3") { uri -> incident3 = uri }
                    incident3?.let { Text("Selected: $it", fontSize = 14.sp) }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        showConfirmationDialog=true
                    },
                    enabled = !isUploading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Application (आवेदन सबमिट करें)")
                }

                if (showConfirmationDialog) {
                    AlertDialog(
                        onDismissRequest = { showConfirmationDialog = false },
                        title = {
                            Text(text = "Confirm Submission (जमा करने की पुष्टि करें)", fontWeight = FontWeight.Bold)
                        },
                        text = {
                            Text(text = "Are you sure you want to submit the application? (क्या आप सुनिश्चित हैं कि आप आवेदन जमा करना चाहते हैं?)")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showConfirmationDialog = false

                                    isUploading = true
                                    if (validateComplaint(formData)) {
                                        uploadComplaintFiles(
                                            mobileNumber = formData.mobile,
                                            username = formData.name,
                                            photoUri = photoUri,
                                            eSignUri = eSignUri,
                                            incident1 = incident1,
                                            incident2 = incident2,
                                            incident3 = incident3
                                        ) { uploadedUrls ->
                                            formData.photoUrl = uploadedUrls["photo.jpg"] ?: ""
                                            formData.eSignUrl = uploadedUrls["esign.jpg"] ?: ""
                                            formData.incidentUrl1 = uploadedUrls["incident1.jpg"] ?: ""
                                            formData.incidentUrl2 = uploadedUrls["incident2.jpg"] ?: ""
                                            formData.incidentUrl3 = uploadedUrls["incident3.jpg"] ?: ""
                                            formData.status="1"

                                            formData.statusHistory = mutableListOf(
                                                StatusUpdate(
                                                    timestamp = getCurrentTimestamp(),
                                                    status = "1",
                                                    comment = "Submitted Successfully by User",
                                                    updatedBy = ""
                                                )
                                            )
                                            if (formData.photoUrl.isNotEmpty() && formData.eSignUrl.isNotEmpty() && formData.email.isNotEmpty()) {
                                                viewModel.submitComplaint(form = formData) { success, complaintId, status ->
                                                    isUploading = false
                                                    if (success) {
                                                        showToast = "Form submitted successfully!"

                                                        // Convert formData to JSON & Encode to pass safely
                                                        val jsonData = Gson().toJson(formData)
                                                        val encodedJsonData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8.toString())

                                                        navController.navigate("${NavigationScreens.DisplaySuccessScreen.name}/$encodedJsonData/$complaintId")
                                                    } else {
                                                        showToast = "Error: $status"
                                                        Log.d("error", "NewApplication: $status")
                                                    }
                                                }


                                            }
                                            else {
                                                showToast = "Error: Required files not uploaded!"
                                                isUploading = false
                                            }

                                        }
                                    }
                                    else {
                                        showToast = "Please Check all fields (कृपया सभी फ़ील्ड्स की जांच करें)"
                                        isUploading = false
                                    }




                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green color
                            ) {
                                Text(text = "Yes (हां)", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showConfirmationDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Red color
                            ) {
                                Text(text = "No (नहीं)", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun DropdownMenuComponent(
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
            if (selectedOption != null) {
                Text(text = (selectedOption.ifEmpty { label }), color = if (selectedOption.isEmpty()) Color.Gray else Color.Black)
            }
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
