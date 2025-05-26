package com.example.compensation_app.screens.guard.complaints

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
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
import com.example.compensation_app.Backend.PdfRequest
import com.example.compensation_app.Backend.RejectComplaintRequest

import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.Backend.UserComplaintRetrievalFormShort
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.FireStorage.ImageRow
import com.example.compensation_app.FireStorage.encodeFirebaseUrl
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.Navigation.logoutUser
import com.example.compensation_app.components.CompleteFormSectionCard
import com.example.compensation_app.components.DetailRow
import com.example.compensation_app.components.InputField
import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.gson.Gson
import showDownloadConfirmationDialogPDF
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
        gson.fromJson(it, UserComplaintRetrievalFormShort::class.java)
    }
    val formData by remember{
        mutableStateOf(FormData())
    }
    var Form by remember{
        mutableStateOf(UserComplaintRetrievalForm.default())
    }
    val isLoading = remember { mutableStateOf(false) }

    var comment by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("") }
    var confirmDownload by remember { mutableStateOf(false) }

    Log.d("Complaint Guard", "Comaplint Guard: $retrivalForm")

    LaunchedEffect(retrivalForm?.complaint_id) {
        retrivalForm?.complaint_id?.let { formID ->
            isLoading.value = true
            viewModel.getEachComplaintForm(formId = formID) { form, message, code ->
                isLoading.value = false
                if (form != null) {

                    Form = form
                } else {
                    Log.d("Forms", "No forms found or error: $message")
                }
            }
        }
    }
    //retrivalForm.documentURL = encodeFirebaseUrl(retrivalForm.documentURL)
    Form.eSignUrl = encodeFirebaseUrl(Form.eSignUrl)
    Form.photoUrl = encodeFirebaseUrl(Form.photoUrl)

    Form.incidentUrl1 = encodeFirebaseUrl(Form.incidentUrl1)
    Form.incidentUrl2 = encodeFirebaseUrl(Form.incidentUrl2)
    Form.incidentUrl3 = encodeFirebaseUrl(Form.incidentUrl3)




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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())){

            CompleteFormSectionCard(title = "Basic Information (मूल जानकारी)") {
                DetailRow(label = "Form ID (प्रपत्र आईडी)", value = Form.complaint_id?.toString())
                DetailRow(label = "Submission Date/Time (जमा करने की तारीख/समय)", value = Form.SubmissionDateTime)
                DetailRow(label = "Applicant Name (आवेदक का नाम)", value = Form.name)
                DetailRow(label = "Age (आयु)", value = Form.age?.toString())
                DetailRow(label = "Father/Spouse Name (पिता/पति का नाम)", value = Form.fatherOrSpouseName)
                DetailRow(label = "Mobile (मोबाइल)", value = Form.mobile)
            }
            Spacer(modifier = Modifier.height(12.dp))
            CompleteFormSectionCard(title = "Incident Details (घटना का विवरण)") {
                DetailRow(label = "Animal Name (जानवर का नाम)", value = Form.animalList)
                DetailRow(label = "Incident Date (घटना की तारीख)", value = Form.damageDate)
                DetailRow(label = "Additional Details (अतिरिक्त विवरण)", value = Form.additionalDetails)
                DetailRow(label = "Address (पता)", value = Form.address)
            }
            Spacer(modifier = Modifier.height(12.dp))

            CompleteFormSectionCard(title = "Human Death/Injury (मानव मृत्यु/चोट)") {
                DetailRow(label = "Name of Victim (पीड़ित का नाम)", value = Form.humanDeathVictimNames)
                DetailRow(label = "Number of Deaths (मृत्यु की संख्या)", value = Form.humanDeathNumber?.toString())
                DetailRow(label = "Temporary Injury Details (अस्थायी चोटों का विवरण)", value = Form.temporaryInjuryDetails)
                DetailRow(label = "Permanent Injury Details (स्थायी चोटों का विवरण)", value = Form.permanentInjuryDetails)
            }
            Spacer(modifier = Modifier.height(12.dp))

            CompleteFormSectionCard(title = "Livestock Damage (पशुधन क्षति)") {
                DetailRow(label = "Number of Cattles Died (मरे हुए मवेशियों की संख्या)", value = Form.cattleInjuryNumber?.toString())
                DetailRow(label = "Estimated Cattle Age (मवेशियों की अनुमानित आयु)", value = Form.cattleInjuryEstimatedAge?.toString())
            }
            Spacer(modifier = Modifier.height(12.dp))

            CompleteFormSectionCard(title = "Crop & Property Damage (फसल और संपत्ति की क्षति)") {
                DetailRow(label = "Crop Type (फसल का प्रकार)", value = Form.cropType)
                DetailRow(label = "Cereal Crop (अनाज की फसल)", value = Form.cerealCrop)
                DetailRow(label = "Full House Damage (पूर्ण घर क्षति)", value = Form.fullHousesDamaged)
                DetailRow(label = "Partial House Damage (आंशिक घर क्षति)", value = Form.partialHousesDamaged)
            }
            Spacer(modifier = Modifier.height(12.dp))

            CompleteFormSectionCard(title = "Incident Images (घटना की तस्वीरें)") {
                Form.incidentUrl1?.let { ImageRow("Incident Image 1", it) }
                Form.incidentUrl2?.let { ImageRow("Incident Image 2", it) }
                Form.incidentUrl3?.let { ImageRow("Incident Image 3", it) }
            }
            Spacer(modifier = Modifier.height(12.dp))

            CompleteFormSectionCard(title = "Photo & eSign (फोटो और हस्ताक्षर)") {
                Form.photoUrl?.let { ImageRow("Applicant Photo", it) }
                Form.eSignUrl?.let { ImageRow("eSign", it) }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CompleteFormSectionCard(title = "Print Application Form") {
                Button(onClick = {
                    confirmDownload=true
                    if(Form.mobile!=null && Form.name !=null && Form.complaint_id!=null){
                        viewModel.getOrCreatePdf(
                            PdfRequest(
                                mobile = Form.mobile!!,
                                username = Form.name!!,
                                form_id = Form.complaint_id.toString(),
                                forestguardId = null,
                                is_compensation = false
                            )
                        ) { result, code ->
                            confirmDownload=false
                            when {
                                result.isSuccess -> {
                                    val downloadUrl = result.getOrNull()
                                    Log.d("PDF", "Download URL: $downloadUrl")
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(Uri.parse(downloadUrl), "application/pdf")
                                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    context.startActivity(intent)
                                }
                                result.isFailure -> {
                                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                                    Log.e("PDF", "Error: $error | Code: $code")
                                    Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }) {
                    Text("Print Application Form")
                }
//                if (isLoading.value) {
//                    CircularProgressIndicator() // Show loading indicator
//                } else {
//
//                }
            }
            Spacer(modifier = Modifier.height(12.dp))

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
                            colors = ButtonDefaults.buttonColors(Color(0xFF3E7B27)),
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
                                    Form.let {
                                        if (gguard != null) {
                                            formData.forestGuardID = gguard.emp_id
                                            formData.complaint_id=it.complaint_id
                                            formData.name = it.name.orEmpty()
                                            formData.age = it.age.orEmpty()
                                            formData.fatherOrSpouseName = it.fatherOrSpouseName.orEmpty()
                                            formData.mobile = it.mobile.orEmpty()
                                            formData.animalList = it.animalList.orEmpty()
                                            formData.damageDate = it.damageDate.orEmpty()
                                            formData.email= it.email.toString()

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
                                            formData.adhar = it.adhaar.toString()

                                            // Document & Image URLs
                                            formData.idProof = ""
                                            formData.photoUrl = it.photoUrl.orEmpty()
                                            formData.eSignUrl = it.eSignUrl.orEmpty()

                                        }
                                    } // Not present in `retrivalForm`, set it if needed

                                    if (gguard != null) {
                                        val jsonForm = gson.toJson(formData)
                                        val encodedFormJson = URLEncoder.encode(jsonForm, StandardCharsets.UTF_8.toString())


                                        showDownloadConfirmationDialog(
                                            navController = navController,
                                            viewModel = viewModel,
                                            mainViewModel=mainViewModel,
                                            gguard=gguard,
                                            context = context,
                                            action = selectedAction,
                                            guardId = gguard.emp_id,
                                            guard = guard,
                                            encodedFormJson = encodedFormJson,
                                            form = formData,
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
    if (confirmDownload) {
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
}







fun showDownloadConfirmationDialog(
    navController: NavController,
    viewModel: GuardViewModel,
    mainViewModel: MainViewModel,
    gguard:emp,
    context: Context,
    action: String,
    guardId:String,
    guard:String?,
    form:FormData,
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
            if (comment.isNotBlank() && form.complaint_id!=null) {
                guardId.let {

                    val request = RejectComplaintRequest(
                        complaintId = form.complaint_id!!,
                        comment = comment,
                        guardId = guardId
                    )
                    viewModel.rejectComplaint(request = request){result ,code->

                        result.onSuccess { message ->
                            Toast.makeText(context, "Successfully Rejected ", Toast.LENGTH_SHORT).show()
                        }.onFailure { error ->
                            if (code==401 ||  code ==403){
                                logoutUser(navController = navController, mainViewModel = mainViewModel, emp =gguard )
                            }
                           Toast.makeText(context, "Error $error", Toast.LENGTH_SHORT).show()
                            Log.d("error", "showDownloadConfirmationDialog: $error")
                        }
                    }

                }
                // Handle Send Forward Logic (use the comment)
            } else {
                Toast.makeText(context, "Comment required for Rejecting", Toast.LENGTH_SHORT).show()
            }
            // Handle Rejection Logic (comment not required)
        }
    }
}
