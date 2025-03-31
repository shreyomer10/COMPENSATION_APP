package com.example.compensation_app.screens.guard

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Backend.validate
import com.example.compensation_app.FireStorage.FileDetails
import com.example.compensation_app.FireStorage.ImagePickerButton
import com.example.compensation_app.FireStorage.SelectPdfButton
import com.example.compensation_app.FireStorage.uploadFormFiles
import com.example.compensation_app.components.DamageDetailsDropdown
import com.example.compensation_app.components.DatePickerField
import com.example.compensation_app.components.InputField
import com.example.compensation_app.components.RequiredDocumentsTable
import com.example.compensation_app.components.SectionTitle
import com.example.compensation_app.components.getCurrentTimestamp
import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.viewmodel.GuardViewModel
import com.example.landareacalculator.ControlButtons
import com.example.landareacalculator.LandAreaTracker
import com.example.landareacalculator.MapScreen
import com.google.gson.Gson


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDraftApplication(navController: NavController, guard: String?, draftForm: String?) {
    var formData by remember { mutableStateOf(FormData()) }
    val gson = Gson()
    val fform = draftForm.let {
        gson.fromJson(it, FormData::class.java)
    }

    val context= LocalContext.current

    LaunchedEffect (draftForm){

        formData=fform

    }
    val gguard = guard?.let {
        gson.fromJson(it, emp::class.java)
    }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var DraftSaver by remember { mutableStateOf(false) }
    // Data class instance to hold form data.visibility = View.GONE
    val viewModel: GuardViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()



    if(gguard!=null){
        Log.d("Final 111", "NewApplication:${gguard.emp_id} ")
        //Log.d("User", "NewApplication: $mobileNumber")
        var isUploading by remember { mutableStateOf(false) }
        // var animalExpanded by remember { mutableStateOf(false) } // For dropdown menu
        var cropDamageChecked by remember { mutableStateOf(false) }
        var houseDamageChecked by remember { mutableStateOf(false) }
        var cattleInjuryChecked by remember { mutableStateOf(false) }
        var humanDeathChecked by remember { mutableStateOf(false) }
        var humanInjuryChecked by remember { mutableStateOf(false) }
        var showToast by remember { mutableStateOf<String?>(null) }
        var pdfUri by remember { mutableStateOf<Uri?>(null) }
        var nocReport by remember { mutableStateOf<Uri?>(null) }
        var landOwnershipReport by remember { mutableStateOf<Uri?>(null) }
        var rorReport by remember { mutableStateOf<Uri?>(null) }
        var propertyOwnerReport by remember { mutableStateOf<Uri?>(null) }
        var vasCertificate by remember { mutableStateOf<Uri?>(null) }
        var deathCertificate by remember { mutableStateOf<Uri?>(null) }
        var sarpanchReport by remember { mutableStateOf<Uri?>(null) }
        var pmReport by remember { mutableStateOf<Uri?>(null) }
        var medicalCertificate by remember { mutableStateOf<Uri?>(null) }




        var photoUri by remember { mutableStateOf<Uri?>(null) }
        var eSignUri by remember { mutableStateOf<Uri?>(null) }
        var cropDamagePhoto by remember { mutableStateOf<Uri?>(null) }
        var housedamagePhoto1 by remember { mutableStateOf<Uri?>(null) }
        var housedamagePhoto2 by remember { mutableStateOf<Uri?>(null) }
        var cattlePhoto by remember { mutableStateOf<Uri?>(null) }
        var humanPhoto1 by remember { mutableStateOf<Uri?>(null) }
        var humanPhoto2 by remember { mutableStateOf<Uri?>(null) }
        var humanInjuryPhoto by remember { mutableStateOf<Uri?>(null) }


        val pdfSizeError = remember { mutableStateOf<String?>(null) }
        val context= LocalContext.current

        var area by remember {
            mutableStateOf(0.0)
        }
        val landAreaTracker = remember { LandAreaTracker(context) }
        val points by landAreaTracker.locationList.collectAsState()

        LaunchedEffect(showToast) {
            showToast?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                showToast = null
            }
        }
        BackHandler {
            DraftSaver=true
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
                            text = "Edit Applications",
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
                        MapScreen(points) // <-- Now using MapScreen to show the map
                        ControlButtons(landAreaTracker) { calculatedArea ->
                            Log.d("CalculatedArea", "Area: $calculatedArea square meters")
                            area=calculatedArea
                        }
                        val areaInAcres = String.format("%.3f", area / 4046.86)
                        formData.cropDamageAmount=(area/4046.86)*9000

                        formData.cropDamageArea= areaInAcres
                        SectionTitle(title = "Area in sq-m")
                        InputField(
                            label = "Crop Damage Area (फसल नुकसान क्षेत्र)",
                            value = formData.cropDamageArea,
                            onValueChange = {}, // No effect when user types
                            keyboardType = KeyboardType.Decimal,
                            enabled = false // Disables user input
                        )
                        SectionTitle(title = "Amount in Rs.")

                        InputField(
                            label = "Crop Damage Amount",
                            value = formData.cropDamageAmount.toString(),
                            onValueChange = { formData = formData.copy(cropDamageAmount = it.toDouble()) },
                            keyboardType = KeyboardType.Decimal,
                            enabled = false
                        )
                        ImagePickerButton("Select Crop Photo") { uri -> cropDamagePhoto = uri }
                        cropDamagePhoto?.let { Text("Selected: $it", fontSize = 14.sp) }

                        SelectPdfButton ("NOC REPORT"){ uri->
                            nocReport= uri

                        }
                        SelectPdfButton("Land Ownership Report") { uri->
                            landOwnershipReport= uri

                        }
                        SelectPdfButton("ROR report") { uri->
                            rorReport= uri

                        }

                    }
                    else{
                        formData.cropType=""
                        formData.cerealCrop=""
                        formData.cropDamageAmount= 0.0
                        formData.cropDamageArea=""
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
                        ImagePickerButton("Select damage photo 1") { uri -> housedamagePhoto1 = uri }
                        housedamagePhoto1?.let { Text("Selected: $it", fontSize = 14.sp) }

                        ImagePickerButton("Select damage photo 2") { uri -> housedamagePhoto2 = uri }
                        housedamagePhoto2?.let { Text("Selected: $it", fontSize = 14.sp) }

                        SelectPdfButton ("Propert Ownership "){ uri->

                            propertyOwnerReport= uri

                        }
                        InputField(
                            label = "House Damage Amount",
                            value = formData.houseDamageAmount.toString(),
                            onValueChange = { formData = formData.copy(houseDamageAmount = it.toDouble()) },
                            keyboardType = KeyboardType.Decimal
                        )


                    }
                    else{
                        formData.fullHousesDamaged=""
                        formData.partialHousesDamaged=""
                        formData.houseDamageAmount= 0.0
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
                        InputField(
                            label = "Cattle Injury Amount",
                            value = formData.catleInjuryAmount.toString(),
                            onValueChange = { formData = formData.copy(catleInjuryAmount = it.toDouble()) },
                            keyboardType = KeyboardType.Decimal
                        )
                        ImagePickerButton("Select Cattle photo 1") { uri -> cattlePhoto = uri }
                        cattlePhoto?.let { Text("Selected: $it", fontSize = 14.sp) }

                        SelectPdfButton("Vas Certificate") { uri->
                            vasCertificate= uri

                        }

                    }
                    else{
                        formData.cattleInjuryNumber=""
                        formData.cattleInjuryEstimatedAge=""
                        formData.catleInjuryAmount= 0.0
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
                        ImagePickerButton("Select Human death photo 1") { uri -> humanPhoto1 = uri }
                        humanPhoto1?.let { Text("Selected: $it", fontSize = 14.sp) }

                        ImagePickerButton("Select Human death photo 2") { uri -> humanPhoto2 = uri }
                        humanPhoto2?.let { Text("Selected: $it", fontSize = 14.sp) }

                        SelectPdfButton("deathCertificate") { uri->
                            deathCertificate= uri

                        }
                        SelectPdfButton ("sarpanchReport"){ uri->
                            sarpanchReport= uri

                        }
                        SelectPdfButton("pmReport") { uri->
                            pmReport= uri

                        }


                    }
                    else{
                        formData.humanDeathVictimNames=""
                        formData.humanDeathNumber=""
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
                        InputField(
                            label = "Human Injury Amount",
                            value = formData.humanInjuryAmount.toString(),
                            onValueChange = { formData = formData.copy(humanInjuryAmount = it.toDouble()) },
                            keyboardType = KeyboardType.Decimal
                        )

                        ImagePickerButton("Select Human injury photo 1") { uri -> humanInjuryPhoto = uri }
                        humanInjuryPhoto?.let { Text("Selected: $it", fontSize = 14.sp) }

                        SelectPdfButton ("medicalCertificate"){ uri->
                            medicalCertificate= uri
                        }
                    }
                    else{
                        formData.temporaryInjuryDetails=""
                        formData.permanentInjuryDetails=""
                        formData.humanInjuryAmount=0.0
                    }
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



                        SelectPdfButton("Identity Proof") { uri->
                            pdfUri= uri
                        }
//                        Text("Selected: $pdfUri", fontSize = 14.sp, color = Color.Green)


                        //Text("Error: $pdfSizeError", fontSize = 14.sp, color = Color.Red)

                        Spacer(modifier = Modifier.height(8.dp))
                    }


                    // Show file details or error
                    // FileDetails(pdfUri = pdfUri)
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
                                        isUploading = true  // ✅ Show loading while uploading

                                        // ✅ Upload photos to Firebase
                                        uploadFormFiles(
                                            forestGuardId = gguard.emp_id,
                                            applicantMobile = formData.mobile,
                                            photoUri = photoUri,
                                            eSignUri = eSignUri,
                                            idProof = pdfUri, // Renamed from documentURL
                                            cropdamagePhoto = cropDamagePhoto,
                                            nocReport = nocReport,
                                            landOwnershipReport = landOwnershipReport,
                                            rorReport = rorReport,
                                            housedamagePhoto1 = housedamagePhoto1,
                                            housedamagePhoto2 = housedamagePhoto2,
                                            propertyOwnerReport = propertyOwnerReport,
                                            cattlePhoto = cattlePhoto,
                                            vasCertificate = vasCertificate,
                                            humanPhoto1 = humanPhoto1,
                                            humanPhoto2 = humanPhoto2,
                                            deathCertificate = deathCertificate,
                                            sarpanchReport = sarpanchReport,
                                            pmReport = pmReport,
                                            humanInjuryPhoto = humanInjuryPhoto,
                                            medicalCertificate = medicalCertificate
                                        ) { uploadedUrls ->

                                            // ✅ Store uploaded URLs in formData
                                            formData.idProof = uploadedUrls["idProof.pdf"] ?: ""

                                            formData.photoUrl = uploadedUrls["photo.jpg"] ?: ""
                                            formData.eSignUrl = uploadedUrls["esign.jpg"] ?: ""
                                            formData.cropdamagePhoto = uploadedUrls["cropDamagePhoto.jpg"] ?: ""
                                            formData.nocReport = uploadedUrls["nocReport.pdf"] ?: ""
                                            formData.landOwnershipReport = uploadedUrls["landOwnershipReport.pdf"] ?: ""
                                            formData.rorReport = uploadedUrls["rorReport.pdf"] ?: ""
                                            formData.housedamagePhoto1 = uploadedUrls["housedamagePhoto1.jpg"] ?: ""
                                            formData.housedamagePhoto2 = uploadedUrls["housedamagePhoto2.jpg"] ?: ""
                                            formData.propertyOwnerReport = uploadedUrls["propertyOwnerReport.pdf"] ?: ""
                                            formData.cattlePhoto = uploadedUrls["cattlePhoto.jpg"] ?: ""
                                            formData.vasCertificate  = uploadedUrls["vasCertificate.pdf"] ?: ""
                                            formData.humanPhoto1  = uploadedUrls["humanPhoto1.jpg"] ?: ""
                                            formData.humanPhoto2  = uploadedUrls["humanPhoto2.jpg"] ?: ""
                                            formData.deathCertificate  = uploadedUrls["deathCertificate.pdf"] ?: ""
                                            formData.sarpanchReport  = uploadedUrls["sarpanchReport.pdf"] ?: ""
                                            formData.pmReport   = uploadedUrls["pmReport.pdf"] ?: ""
                                            formData.humanInjuryPhoto   = uploadedUrls["humanInjuryPhoto.jpg"] ?: ""
                                            formData.medicalCertificate   = uploadedUrls["medicalCertificate.pdf"] ?: ""


                                            Log.d("photoUrl", "NewApplication: ${formData.photoUrl}")
                                            Log.d("sign", "NewApplication: ${formData.eSignUrl}")

                                            Log.d("pdf", "NewApplication: ${formData.idProof}")
                                            // ✅ Proceed only if required files are uploaded
                                            if (formData.idProof.isNotEmpty() && formData.photoUrl.isNotEmpty() && formData.eSignUrl.isNotEmpty()) {

                                                val form = CompensationForm(
                                                    forestGuardID = gguard.emp_id,
                                                    complaint_id = null,
                                                    applicantName = formData.name,
                                                    age = formData.age.toInt(),
                                                    fatherSpouseName = formData.fatherOrSpouseName,
                                                    mobile = formData.mobile,
                                                    email = formData.email,
                                                    animalName = formData.animalList,
                                                    incidentDate = formData.damageDate,
                                                    additionalDetails = formData.additionalDetails,
                                                    circle_CG = gguard.Circle_CG,
                                                    circle1 = gguard.Circle1,
                                                    division = gguard.division,
                                                    subdivision = gguard.subdivision,
                                                    range_ = gguard.range_,
                                                    beat = gguard.beat,
                                                    address = formData.address,
                                                    cropType = formData.cropType,
                                                    cerealCrop = formData.cerealCrop,
                                                    cropDamageArea = formData.cropDamageArea.toDoubleOrNull() ?: 0.0,
                                                    fullHouseDamage = formData.fullHousesDamaged,
                                                    partialHouseDamage = formData.partialHousesDamaged,
                                                    numberOfCattlesDied = formData.cattleInjuryNumber.toIntOrNull() ?: 0,
                                                    estimatedCattleAge = formData.cattleInjuryEstimatedAge.toIntOrNull() ?: 0,
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
                                                    totalCompensationAmount = (
                                                            formData.cropDamageAmount +
                                                                    formData.catleInjuryAmount +
                                                                    formData.humanInjuryAmount +
                                                                    formData.houseDamageAmount +
                                                                    formData.humanDeathAmount
                                                            ),
                                                    statusHistory = mutableListOf(
                                                        StatusUpdate(
                                                            timestamp = getCurrentTimestamp(),
                                                            status = "2",
                                                            comment = "Submitted Successfully",
                                                            updatedBy = gguard.emp_id
                                                        )
                                                    ),
                                                    idProof = formData.idProof,
                                                    photoUrl = formData.photoUrl, // ✅ Add uploaded URLs
                                                    eSignUrl = formData.eSignUrl,
                                                    cropdamagePhoto  = formData.cropdamagePhoto ,
                                                    nocReport  = formData.nocReport ,

                                                    landOwnershipReport  = formData.landOwnershipReport ,
                                                    rorReport  = formData.rorReport ,
                                                    housedamagePhoto1  = formData.housedamagePhoto1 ,
                                                    housedamagePhoto2  = formData.housedamagePhoto2 ,
                                                    propertyOwnerReport  = formData.propertyOwnerReport ,
                                                    cattlePhoto  = formData.cattlePhoto ,
                                                    vasCertificate  = formData.vasCertificate ,
                                                    humanPhoto1 = formData.humanPhoto1,
                                                    humanPhoto2 = formData.humanPhoto2,
                                                    deathCertificate = formData.deathCertificate,
                                                    sarpanchReport = formData.sarpanchReport,
                                                    pmReport = formData.pmReport,
                                                    humanInjuryPhoto = formData.humanInjuryPhoto,
                                                    medicalCertificate = formData.medicalCertificate,
                                                    status = "2",
                                                    verifiedBy = "Pending",
                                                    paymentProcessedBy = "Pending",
                                                    comments = "",
                                                    cropDamageAmount = formData.cropDamageAmount,
                                                    catleInjuryAmount = formData.catleInjuryAmount,
                                                    humanInjuryAmount = formData.humanInjuryAmount,
                                                    houseDamageAmount = formData.houseDamageAmount,
                                                    humanDeathAmount = formData.humanDeathAmount
                                                )

                                                if (validate(form)) {
                                                    Log.d("validity", "NewApplication: Valid hai")


                                                    viewModel.newApplicationForm(form = form) { success, status ->
                                                        isUploading = false  // ✅ Hide loading when done

                                                        if (success) {
                                                            showToast = "Form submitted successfully!"
                                                            navController.popBackStack()
                                                        } else {
                                                            showToast = "Error: $status"
                                                            Log.d("error", "NewApplication: $status")
                                                        }
                                                    }


                                                } else {
                                                    showToast = "Please Check all fields (कृपया सभी फ़ील्ड्स की जांच करें)"
                                                    isUploading = false
                                                }
                                            } else {
                                                showToast = "Error: Required files not uploaded!"
                                                isUploading = false
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                                ) {
                                    if (isUploading) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                                    } else {
                                        Text(text = "Yes (हां)", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            ,
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
        if (DraftSaver) {
            Log.d("Edit form done or not ", "EditDraftApplication: $formData")
            SaveDraftDialogForEdit(

                onSave = {
                    mainViewModel.updateForm(entity = formData)
                    //saveDraft(formData)
                    showToast = "Draft saved"
                    DraftSaver = false
                    navController.popBackStack()
                },
                onDiscard = {
                    DraftSaver = false
                    navController.popBackStack()
                },
                onCancel = { DraftSaver = false }
            )
        }
    }

}
@Composable
fun SaveDraftDialogForEdit(onSave: () -> Unit, onDiscard: () -> Unit, onCancel: () -> Unit) {
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