package com.example.compensation_app.screens.guard

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compensation_app.Backend.PdfRequest
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.FireStorage.ImageRow
import com.example.compensation_app.FireStorage.encodeFirebaseUrl
import com.example.compensation_app.FireStorage.openPdfWithIntent
import com.example.compensation_app.Navigation.logoutUser
import com.example.compensation_app.components.CompleteFormSectionCard
import com.example.compensation_app.components.DetailRow
import com.example.compensation_app.components.InputField
import com.example.compensation_app.components.getStatusLabel
import com.example.compensation_app.sqlite.MainViewModel
import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.gson.Gson
import showDownloadConfirmationDialogPDF


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RetrivalFormDetailsScreen(navController: NavController, encodedForm: String?,text:String?) {
    val viewModel: GuardViewModel = hiltViewModel()
    val mainViewModel:MainViewModel= hiltViewModel()

    var emp by remember {
        mutableStateOf<emp>(
            emp(emp_id = "",
            mobile_number = "",
            Name = "",
            Circle_CG = "",
            Circle1 = "",
            roll = "guard",
            subdivision = "",
            division = "", range_ = "", beat = "")
        )
    }
    LaunchedEffect(Unit) {
        mainViewModel.GetGuard {
            if (it != null) {
                emp = it
            }
        }
    }


    val context= LocalContext.current
    val gson = Gson()
    val retrivalForm = encodedForm?.let {
        gson.fromJson(it, RetrivalFormShort::class.java)
    }

    var comment by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var confirmDownload by remember { mutableStateOf(false) }

    var selectedAction by remember { mutableStateOf("") }
    var isLoading = remember { mutableStateOf(false) }
    val isLoading2 = remember { mutableStateOf(false) }

    var Form by remember {
        mutableStateOf<RetrivalForm>(RetrivalForm.default())
    }
    LaunchedEffect(retrivalForm?.formID) {
        retrivalForm?.formID?.let { formID ->
            isLoading.value = true
            viewModel.getEachCompensationForm(formId = formID.toString()) { form, message, code ->
                isLoading.value = false
                if (form != null) {
                    Log.d("Initiasl", "PrevApplicationScreen: $form")
                    Form = form
                    Log.d("Forms", "Fetched Forms: $Form")
                } else {
                    Log.d("Forms", "No forms found or error: $message")
                }
            }
        }
    }






    Form.idProof = encodeFirebaseUrl(Form.idProof)
    Form.eSignUrl = encodeFirebaseUrl(Form.eSignUrl)
    Form.photoUrl = encodeFirebaseUrl(Form.photoUrl)

    Form.cropdamagePhoto = encodeFirebaseUrl(Form.cropdamagePhoto)
    Form.nocReport = encodeFirebaseUrl(Form.nocReport)
    Form.landOwnershipReport = encodeFirebaseUrl(Form.landOwnershipReport)
    Form.rorReport = encodeFirebaseUrl(Form.rorReport)

    Form.housedamagePhoto1 = encodeFirebaseUrl(Form.housedamagePhoto1)
    Form.housedamagePhoto2 = encodeFirebaseUrl(Form.housedamagePhoto2)
    Form.propertyOwnerReport = encodeFirebaseUrl(Form.propertyOwnerReport)

    Form.cattlePhoto = encodeFirebaseUrl(Form.cattlePhoto)
    Form.vasCertificate = encodeFirebaseUrl(Form.vasCertificate)

    Form.humanPhoto1 = encodeFirebaseUrl(Form.humanPhoto1)
    Form.humanPhoto2 = encodeFirebaseUrl(Form.humanPhoto2)
    Form.deathCertificate = encodeFirebaseUrl(Form.deathCertificate)
    Form.sarpanchReport = encodeFirebaseUrl(Form.sarpanchReport)
    Form.pmReport = encodeFirebaseUrl(Form.pmReport)

    Form.humanInjuryPhoto = encodeFirebaseUrl(Form.humanInjuryPhoto)
    Form.medicalCertificate = encodeFirebaseUrl(Form.medicalCertificate)



// Restore correct encoding

    Log.d("KY KARU", "RetrivalFormDetailsScreen: ${Form.idProof}")

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

        if(isLoading2.value || isLoading.value){
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
        else{
            Column ( modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())){
                CompleteFormSectionCard(title = "Basic Information (मूल जानकारी)") {
                    DetailRow(label = "Form ID (प्रपत्र आईडी)", value = Form.formID?.toString())
                    DetailRow(label = "Submission Date/Time (जमा करने की तारीख/समय)", value = Form.submissionDateTime)
                    DetailRow(label = "Forest Guard ID (वन रक्षक आईडी)", value = Form.forestGuardID)
                    DetailRow(label = "Applicant Name (आवेदक का नाम)", value = Form.applicantName)
                    DetailRow(label = "Age (आयु)", value = Form.age?.toString())
                    DetailRow(label = "Father/Spouse Name (पिता/पति का नाम)", value = Form.fatherSpouseName)
                    DetailRow(label = "Mobile (मोबाइल)", value = Form.mobile)
                    DetailRow(label = "Email", value = Form.email)
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Incident Details (घटना का विवरण)") {
                    DetailRow(label = "Animal Name (जानवर का नाम)", value = Form.animalName)
                    DetailRow(label = "Incident Date (घटना की तारीख)", value = Form.incidentDate)
                    DetailRow(label = "Additional Details (अतिरिक्त विवरण)", value = Form.additionalDetails)
                    DetailRow(label = "Address (पता)", value = Form.address)
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Human Death/Injury (मानव मृत्यु/चोट)") {
                    DetailRow(label = "Name of Victim (पीड़ित का नाम)", value = Form.humanDeathVictimName)
                    DetailRow(label = "Number of Deaths (मृत्यु की संख्या)", value = Form.numberOfDeaths?.toString())
                    DetailRow(label = "Temporary Injury Details (अस्थायी चोटों का विवरण)", value = Form.temporaryInjuryDetails)
                    DetailRow(label = "Permanent Injury Details (स्थायी चोटों का विवरण)", value = Form.permanentInjuryDetails)
                    DetailRow(label = "Human Injury Amount", value = Form.humanInjuryAmount.toString())
                    DetailRow(label = "Human Death Amount ", value = Form.humanDeathAmount.toString())
                    Form.humanInjuryPhoto?.let { ImageRow("Human Injury Photo", it) }
                    Form.humanPhoto1?.let { ImageRow("Human Death Photo", it) }
                    Form.humanPhoto2?.let { ImageRow("Human Death Photo", it) }
                    Button(onClick = {
                        Form.deathCertificate?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Death certificate Report")
                    }
                    Button(onClick = {
                        Form.sarpanchReport?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("SarpanchReport")
                    }
                    Button(onClick = {
                        Form.pmReport?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("PM Report")
                    }
                    Button(onClick = {
                        Form.medicalCertificate?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Mediacl Certificate")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Livestock Damage (पशुधन क्षति)") {
                    DetailRow(label = "Number of Cattles Died (मरे हुए मवेशियों की संख्या)", value = Form.numberOfCattlesDied?.toString())
                    DetailRow(label = "Estimated Cattle Age (मवेशियों की अनुमानित आयु)", value = Form.estimatedCattleAge?.toString())
                    DetailRow(label = "Cattle Death Amount", value = Form.catleInjuryAmount.toString())
                    Form.cattlePhoto?.let { ImageRow("Cattle Photo", it) }
                    Button(onClick = {
                        Form.vasCertificate?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Vas Certificate")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Crop & Property Damage (फसल और संपत्ति की क्षति)") {
                    DetailRow(label = "Crop Type (फसल का प्रकार)", value = Form.cropType)
                    DetailRow(label = "Cereal Crop (अनाज की फसल)", value = Form.cerealCrop)
                    DetailRow(label = "Crop Damage Area (फसल क्षति क्षेत्र)", value = Form.cropDamageArea?.toString())
                    DetailRow(label = "Crop Damage Amount ", value = Form.cropDamageAmount.toString())
                    Form.cropdamagePhoto?.let { ImageRow("Crop Damage Photo", it) }
                    Button(onClick = {
                        Form.nocReport?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("NOC Report")
                    }
                    Button(onClick = {
                        Form.landOwnershipReport?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Land Ownership Report")
                    }
                    Button(onClick = {
                        Form.rorReport?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("ROR REPORT")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "House Damage Details") {
                    DetailRow(label = "Full House Damage (पूर्ण घर क्षति)", value = Form.fullHouseDamage)
                    DetailRow(label = "Partial House Damage (आंशिक घर क्षति)", value = Form.partialHouseDamage)
                    DetailRow(label = "House Damage Amount", value = Form.houseDamageAmount.toString())
                    Form.housedamagePhoto1?.let { ImageRow("House Damage Photo", it) }
                    Form.housedamagePhoto2?.let { ImageRow("House Damage Photo", it) }
                    Button(onClick = {
                        Form.propertyOwnerReport?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Property Ownership Document")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))


                CompleteFormSectionCard(title = "Banking Details (बैंकिंग विवरण)") {
                    DetailRow(label = "Bank Name (बैंक का नाम)", value = Form.bankName)
                    DetailRow(label = "IFSC Code (आईएफएससी कोड)", value = Form.ifscCode)
                    DetailRow(label = "Branch Name (शाखा का नाम)", value = Form.branchName)
                    DetailRow(label = "Account Holder Name (खाता धारक का नाम)", value = Form.accountHolderName)
                    DetailRow(label = "Account Number (खाता संख्या)", value = Form.accountNumber)
                    DetailRow(label = "PAN Number (पैन नंबर)", value = Form.panNumber)
                    DetailRow(label = "Aadhaar Number (आधार नंबर)", value = Form.aadhaarNumber)
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Total Amount ") {
                    DetailRow(label = "Total Amount:", value = " ${Form.totalCompensationAmount.toString()}/.")
                    //DetailRow(label = "Verified By (द्वारा सत्यापित)", value = Form.verifiedBy)
                    //DetailRow(label = "Payment Processed By (द्वारा भुगतान संसाधित)", value = Form.paymentProcessedBy)
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Form Status (प्रपत्र स्थिति)") {
                    DetailRow(label = "Status (स्थिति)", value = Form.status?.let {
                        getStatusLabel(it)
                    })
                    DetailRow(label = "Verified By (द्वारा सत्यापित)", value = Form.verifiedBy)
                    DetailRow(label = "Payment Processed By (द्वारा भुगतान संसाधित)", value = Form.paymentProcessedBy)
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Status History (स्थिति इतिहास)") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .verticalScroll(rememberScrollState())
                            .padding(8.dp)
                    ) {
                        Column {
                            Form.statusHistory?.forEach { statusUpdate ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    DetailRow(label = "Level (स्तर)", value = getStatusLabel(statusUpdate.status))
                                    DetailRow(label = "Comment (टिप्पणी)", value = statusUpdate.comment)
                                    DetailRow(label = "Updated By (द्वारा अद्यतन)", value = statusUpdate.updatedBy)
                                    DetailRow(label = "Timestamp (समय)", value = statusUpdate.timestamp)
                                    Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Photo & eSign (फोटो और हस्ताक्षर)") {
                    Form.photoUrl?.let { ImageRow("Applicant Photo", it) }
                    Form.eSignUrl?.let { ImageRow("eSign", it) }
                }
                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Documents") {
                    Button(onClick = {
                        Form.idProof?.let {
                            openPdfWithIntent(context = context, it)
                        } ?: run {
                            Toast.makeText(context, "No URL found", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Open PDF")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CompleteFormSectionCard(title = "Print Application Form") {
                    if (isLoading.value) {
                        CircularProgressIndicator() // Show loading indicator
                    }
                    else{
                        Button(
                            onClick = {
                                confirmDownload=true
                                if(Form.mobile!=null && Form.applicantName !=null && Form.formID!=null){
                                    viewModel.getOrCreatePdf(
                                        PdfRequest(
                                            mobile = Form.mobile!!,
                                            username = Form.applicantName!!,
                                            forestguardId = Form.forestGuardID,
                                            form_id = Form.formID.toString(),
                                            is_compensation = true
                                        )) { result, code ->
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

//                            showDownloadConfirmationDialogPDF(
//                                context,
//                                null,
//                                retrivalForm = Form,
//                                isLoading = isLoading)
                        }) {
                            Text("Print Application Form")
                        }
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))


                if(text=="PendingForYou"){
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

                                Button(
                                    onClick = {
                                        selectedAction = "send_back"
                                        showDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Blue)
                                ) {
                                    Text("Back", color = Color.White)
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
                                            showDownloadConfirmationDialog(navController,viewModel,emp,context, selectedAction, Form, comment,isLoading2)
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
    emp: emp,
    context: Context,
    action: String,
    form: RetrivalForm,
    comment: String = "",
    isLoading2:MutableState<Boolean>) {

    when (action) {
        "send_back" -> {
            if(comment.isNotBlank()){
                form.forestGuardID?.let {
                    isLoading2.value=true
                    viewModel.updateStatus(
                        formId = form.formID.toString(),
                        empId = emp.emp_id,
                        action = action,
                        comments = comment){output,code->
                        isLoading2.value=false
                        output.onSuccess {
                            Toast.makeText(context, "Successfully Sent Backward by ${it.verified_by}", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                        output.onFailure {
                            if (code==401 || code ==403){
                                //logoutUser(navController = navController, mainViewModel = mainViewModel, emp = )
                            }
                            Toast.makeText(context, "Error ${it}", Toast.LENGTH_SHORT).show()

                        }
                    }

                }
                //
            }
            else{
                Toast.makeText(context, "Comment required for backward", Toast.LENGTH_SHORT).show()

            }
            // Handle Send Backward Logic (comment not required)
        }
        "accept" -> {
            if (comment.isNotBlank()) {
                form.forestGuardID?.let {
                    isLoading2.value=true

                    viewModel.updateStatus(
                        formId = form.formID.toString(),
                        empId = emp.emp_id,
                        action = action,
                        comments = comment){output,code->
                        isLoading2.value=false

                        output.onSuccess {
                            Toast.makeText(context, "Successfully Sent Forward by ${it.verified_by}", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                        output.onFailure {
                            if (code==401 || code ==403){
                                //logoutUser(navController = navController, mainViewModel = mainViewModel, emp = )
                            }
                            Toast.makeText(context, "Error ${it}", Toast.LENGTH_SHORT).show()

                        }
                    }

                }
                // Handle Send Forward Logic (use the comment)
            } else {
                Toast.makeText(context, "Comment required for forwarding", Toast.LENGTH_SHORT).show()
            }
        }
        "reject" -> {
            if (comment.isNotBlank()) {
                form.forestGuardID?.let {
                    viewModel.updateStatus(
                        formId = form.formID.toString(),
                        empId = emp.emp_id,
                        action = action,
                        comments = comment){output,code->
                        output.onSuccess {
                            Toast.makeText(context, "Successfully Rejected by ${it.verified_by}", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                        output.onFailure {
                            if (code==401 || code ==403){
                                //logoutUser(navController = navController, mainViewModel = mainViewModel, emp = )
                            }
                            Toast.makeText(context, "Error ${it}", Toast.LENGTH_SHORT).show()

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
