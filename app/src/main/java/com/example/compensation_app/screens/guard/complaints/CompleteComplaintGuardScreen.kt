package com.example.compensation_app.screens.guard.complaints

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
import com.example.compensation_app.Backend.FormData

import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.FireStorage.ImageRow
import com.example.compensation_app.FireStorage.encodeFirebaseUrl
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.components.CompleteFormSectionCard
import com.example.compensation_app.components.InputField
import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteComplaintFormScreen(navController: NavController, encodedFormComplaint: String?, guard: String?) {

    val gson = Gson()
    val gguard = guard?.let {
        gson.fromJson(it, emp::class.java)
    }
    val viewModel: GuardViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()

    val context= LocalContext.current
    val retrivalForm = encodedFormComplaint?.let {
        gson.fromJson(it, UserComplaintRetrievalForm::class.java)
    }
    val formData by remember{
        mutableStateOf(FormData())
    }


    var comment by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("") }

    if (retrivalForm != null) {
        //retrivalForm.documentURL = encodeFirebaseUrl(retrivalForm.documentURL)
        retrivalForm.eSignUrl = encodeFirebaseUrl(retrivalForm.eSignUrl)
        retrivalForm.photoUrl = encodeFirebaseUrl(retrivalForm.photoUrl)

        retrivalForm.incidentUrl1 = encodeFirebaseUrl(retrivalForm.incidentUrl1)

        retrivalForm.incidentUrl2 = encodeFirebaseUrl(retrivalForm.incidentUrl2)
        retrivalForm.incidentUrl3 = encodeFirebaseUrl(retrivalForm.incidentUrl3)



        Column(

        ) {
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
                            text = "Form Details (प्रपत्र विवरण)",
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
            Column ( modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Group 1: Basic Information
                    item {
                        CompleteFormSectionCard(title = "Basic Information (मूल जानकारी)") {
                            DetailRow(label = "Form ID (प्रपत्र आईडी)", value = retrivalForm.complaint_id?.toString())
                            DetailRow(label = "Submission Date/Time (जमा करने की तारीख/समय)", value = retrivalForm.SubmissionDateTime)
                            //DetailRow(label = "Forest Guard ID (वन रक्षक आईडी)", value = retrivalForm.forestGuardID)
                            DetailRow(label = "Applicant Name (आवेदक का नाम)", value = retrivalForm.name)
                            DetailRow(label = "Age (आयु)", value = retrivalForm.age?.toString())
                            DetailRow(label = "Father/Spouse Name (पिता/पति का नाम)", value = retrivalForm.fatherOrSpouseName)
                            DetailRow(label = "Mobile (मोबाइल)", value = retrivalForm.mobile)
                        }
                    }
                    // Group 2: Incident Details
                    item {
                        CompleteFormSectionCard(title = "Incident Details (घटना का विवरण)") {
                            DetailRow(label = "Animal Name (जानवर का नाम)", value = retrivalForm.animalList)
                            DetailRow(label = "Incident Date (घटना की तारीख)", value = retrivalForm.damageDate)
                            DetailRow(label = "Additional Details (अतिरिक्त विवरण)", value = retrivalForm.additionalDetails)
                            DetailRow(label = "Address (पता)", value = retrivalForm.address)
                        }
                    }
                    // Group 3: Human Death/Injury
                    item {
                        CompleteFormSectionCard(title = "Human Death/Injury (मानव मृत्यु/चोट)") {
                            DetailRow(label = "Name of Victim (पीड़ित का नाम)", value = retrivalForm.humanDeathVictimNames)
                            DetailRow(label = "Number of Deaths (मृत्यु की संख्या)", value = retrivalForm.humanDeathNumber?.toString())
                            DetailRow(label = "Temporary Injury Details (अस्थायी चोटों का विवरण)", value = retrivalForm.temporaryInjuryDetails)
                            DetailRow(label = "Permanent Injury Details (स्थायी चोटों का विवरण)", value = retrivalForm.permanentInjuryDetails)

                        }
                    }
                    // Group 4: Livestock Damage
                    item {
                        CompleteFormSectionCard(title = "Livestock Damage (पशुधन क्षति)") {
                            DetailRow(label = "Number of Cattles Died (मरे हुए मवेशियों की संख्या)", value = retrivalForm.cattleInjuryNumber?.toString())
                            DetailRow(label = "Estimated Cattle Age (मवेशियों की अनुमानित आयु)", value = retrivalForm.cattleInjuryEstimatedAge?.toString())

                        }
                    }
                    // Group 5: Crop & Property Damage
                    item {
                        CompleteFormSectionCard(title = "Crop & Property Damage (फसल और संपत्ति की क्षति)") {
                            DetailRow(label = "Crop Type (फसल का प्रकार)", value = retrivalForm.cropType)
                            DetailRow(label = "Cereal Crop (अनाज की फसल)", value = retrivalForm.cerealCrop)

                            DetailRow(label = "Full House Damage (पूर्ण घर क्षति)", value = retrivalForm.fullHousesDamaged)
                            DetailRow(label = "Partial House Damage (आंशिक घर क्षति)", value = retrivalForm.partialHousesDamaged)

                        }
                    }

                    // Group 7: Status
//                    item {
//                        CompleteFormSectionCard(title = "Form Status (प्रपत्र स्थिति)") {
//                            DetailRow(label = "Status (स्थिति)", value = retrivalForm.status)
//                            DetailRow(label = "Verified By (द्वारा सत्यापित)", value = retrivalForm.verifiedBy)
//                            DetailRow(label = "Payment Processed By (द्वारा भुगतान संसाधित)", value = retrivalForm.paymentProcessedBy)
//                        }
//                    }
//                    item {
//                        CompleteFormSectionCard(title = "Status History (स्थिति इतिहास)") {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(250.dp) // Set a fixed height for scrolling
//                                    .verticalScroll(rememberScrollState()) // Enables scrolling
//                                    .padding(8.dp)
//                            ) {
//                                Column {
//                                    retrivalForm.statusHistory?.forEach { statusUpdate ->
//                                        Column(
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .padding(8.dp)
//                                        ) {
//                                            DetailRow(label = "Level (स्तर)", value = getStatusLabel(statusUpdate.status))
//                                            DetailRow(label = "Comment (टिप्पणी)", value = statusUpdate.comment)
//                                            DetailRow(label = "Updated By (द्वारा अद्यतन)", value = statusUpdate.updatedBy)
//                                            DetailRow(label = "Timestamp (समय)", value = statusUpdate.timestamp)
//                                            Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                    // Group 4: Incident Images
                    item {
                        CompleteFormSectionCard(title = "Incident Images (घटना की तस्वीरें)") {
                            retrivalForm.incidentUrl1?.let { ImageRow("Incident Image 1", it) }
                            retrivalForm.incidentUrl2?.let { ImageRow("Incident Image 2", it) }
                            retrivalForm.incidentUrl3?.let { ImageRow("Incident Image 3", it) }
                        }
                    }

                    // Group 5: Photo and eSign
                    item {
                        CompleteFormSectionCard(title = "Photo & eSign (फोटो और हस्ताक्षर)") {
                            retrivalForm.photoUrl?.let { ImageRow("Applicant Photo", it) }
                            retrivalForm.eSignUrl?.let { ImageRow("eSign", it) }
                        }
                    }




//                    item{
//                        CompleteFormSectionCard(title = "Documents") {
//                            Button(onClick = {
//                                retrivalForm.documentURL?.let {
//
//                                    openPdfWithIntent(context = context, it)
//                                } ?: run {
//                                    Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()  // Show a toast if URL is null
//                                }
//                            }) {
//                                Text("Open PDF")
//                            }
//
//                        }
//
//                    }
//                    item{
//                        CompleteFormSectionCard(title = "Print Application Form") {
//                            Button(onClick = { showDownloadConfirmationDialog(context,null,retrivalForm)}) {
//                                Text("Print Application Form")
//                            }
//                        }
//                    }
                    item {
                        CompleteFormSectionCard(title = "Application Actions") {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)) {
                                Text("Comments", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                InputField(
                                    label = "Comments",
                                    value = comment,
                                    onValueChange = {
                                        comment = it },
                                    keyboardType = KeyboardType.Text
                                )

//                                OutlinedTextField(
//                                    value = comment,
//                                    onValueChange = { comment = it },
//                                    label = { Text("Add your comments here...") },
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .height(120.dp),
//                                    keyboardOptions = KeyboardOptions.Default.copy(
//                                        capitalization = KeyboardCapitalization.None // Normal keyboard input
//                                    ),
//                                    enabled = selectedAction == "Forward"
//                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = {
                                            selectedAction = "reject"
                                            showDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(Color.Red)
                                    ) {
                                        Text("Reject", color = Color.White)
                                    }

                                    Button(
                                        onClick = {
                                            selectedAction = "accept"
                                            showDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(Color.Green),
                                        //enabled = comment.isNotEmpty() // Requires comment for forwarding
                                    ) {
                                        Text("Forward", color = Color.White)
                                    }

                                }

                                if (showDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDialog = false },
                                        title = { Text("Confirm Action") },
                                        text = { Text("Are you sure you want to $selectedAction this application?\nComment: $comment") },
                                        confirmButton = {
                                            Button(onClick = {
                                                showDialog = false
                                                retrivalForm.let {
                                                    if (gguard != null) {
                                                        formData.forestGuardID = gguard.emp_id
                                                        formData.complaint_id=it.complaint_id
                                                        formData.name = it.name.orEmpty()
                                                        formData.age = it.age.orEmpty()
                                                        formData.fatherOrSpouseName = it.fatherOrSpouseName.orEmpty()
                                                        formData.mobile = it.mobile.orEmpty()
                                                        formData.animalList = it.animalList.orEmpty()
                                                        formData.damageDate = it.damageDate.orEmpty()
                                                        formData.additionalDetails = it.additionalDetails.orEmpty()
                                                        formData.address = it.address.orEmpty()

                                                        // These values may not be in `retrivalForm`, so default to empty or 0
                                                        formData.cropType = it.cropType.orEmpty()
                                                        formData.cerealCrop = it.cerealCrop.orEmpty()
                                                        formData.cropDamageArea = "" // Missing in `retrivalForm`, set if needed
                                                        formData.cropDamageAmount = 0.0 // Missing in `retrivalForm`, set if needed

                                                        formData.fullHousesDamaged = it.fullHousesDamaged.orEmpty()
                                                        formData.partialHousesDamaged = it.partialHousesDamaged.orEmpty()
                                                        formData.houseDamageAmount = 0.0 // Missing in `retrivalForm`, set if needed

                                                        formData.cattleInjuryNumber = it.cattleInjuryNumber.orEmpty()
                                                        formData.cattleInjuryEstimatedAge = it.cattleInjuryEstimatedAge.orEmpty()
                                                        formData.catleInjuryAmount = 0.0 // Missing in `retrivalForm`, set if needed

                                                        formData.humanDeathVictimNames = it.humanDeathVictimNames.orEmpty()
                                                        formData.humanDeathNumber = it.humanDeathNumber.orEmpty()
                                                        formData.humanDeathAmount = 0.0 // Missing in `retrivalForm`, set if needed
                                                        formData.humanInjuryAmount = 0.0 // Missing in `retrivalForm`, set if needed

                                                        formData.temporaryInjuryDetails = it.temporaryInjuryDetails.orEmpty()
                                                        formData.permanentInjuryDetails = it.permanentInjuryDetails.orEmpty()

                                                        // Bank Details (not available in `retrivalForm`, set if needed)
                                                        formData.bankName = ""
                                                        formData.ifscCode = ""
                                                        formData.bankBranch = ""
                                                        formData.bankHolderName = ""
                                                        formData.bankAccountNumber = ""
                                                        formData.pan = ""
                                                        formData.adhar = ""

                                                        // Document & Image URLs
                                                        formData.documentURL = ""
                                                        formData.photoUrl = it.photoUrl.orEmpty()
                                                        formData.eSignUrl = it.eSignUrl.orEmpty()
                                                        formData.incidentUrl1 = it.incidentUrl1.orEmpty()
                                                        formData.incidentUrl2 = it.incidentUrl2.orEmpty()
                                                        formData.incidentUrl3 = it.incidentUrl3.orEmpty()
                                                    }
                                                } // Not present in `retrivalForm`, set it if needed

                                                if (gguard != null) {
                                                    val jsonForm = gson.toJson(formData)
                                                    val encodedFormJson = URLEncoder.encode(jsonForm, StandardCharsets.UTF_8.toString())


                                                    showDownloadConfirmationDialog(
                                                        navController = navController,
                                                        viewModel = viewModel,
                                                        context = context,
                                                        action = selectedAction,
                                                        guardId = gguard.emp_id,
                                                        guard = guard,
                                                        encodedFormJson = encodedFormJson,
                                                        comment = comment)
                                                }
                                            }) {
                                                Text("Confirm")
                                            }
                                        },
                                        dismissButton = {
                                            Button(onClick = { showDialog = false }) {
                                                Text("Cancel")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}





@Composable
fun DetailRow(label: String, value: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = value ?: "Not Provided (उपलब्ध नहीं)",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)
    }
}

fun showDownloadConfirmationDialog(
    navController: NavController,
    viewModel: GuardViewModel,
    context: Context,
    action: String,
    guardId:String,
    guard:String?,
    encodedFormJson: String?,
    comment: String = "") {



    when (action) {

        "accept" -> {
            if (comment.isNotBlank()) {
                guardId.let {
                    navController.navigate(NavigationScreens.EditDraftScreen.name +"/$guard"+ "/$encodedFormJson")
                }
                // Handle Send Forward Logic (use the comment)
            } else {
                Toast.makeText(context, "Comment required for forwarding", Toast.LENGTH_SHORT).show()
            }
        }
        "reject" -> {
            if (comment.isNotBlank()) {
                guardId.let {
//                    viewModel.updateStatus(
//                        formId = form.formID.toString(),
//                        empId = it,
//                        action = action,
//                        comments = comment){output->
//                        output.onSuccess {
//                            Toast.makeText(context, "Successfully Sent Backward by ${it.verified_by}", Toast.LENGTH_SHORT).show()
//                        }
//                        output.onFailure {
//                            Toast.makeText(context, "Error ${it}", Toast.LENGTH_SHORT).show()
//
//                        }
//                    }

                }
                // Handle Send Forward Logic (use the comment)
            } else {
                Toast.makeText(context, "Comment required for Rejecting", Toast.LENGTH_SHORT).show()
            }
            // Handle Rejection Logic (comment not required)
        }
    }
}
