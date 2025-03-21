package com.example.compensation_app.screens.user


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.FireStorage.ImageRow
import com.example.compensation_app.FireStorage.encodeFirebaseUrl
import com.example.compensation_app.components.CompleteFormSectionCard
import com.example.compensation_app.components.DetailRow
import com.example.compensation_app.components.getStatusLabel
import com.google.gson.Gson
import showDownloadConfirmationDialogPDF


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RetrivalComplaintDisplayScreen(navController: NavController, encodedForm: String?) {
    val context= LocalContext.current
    val gson = Gson()
    val retrivalForm = encodedForm?.let {
        gson.fromJson(it, UserComplaintRetrievalForm::class.java)
    }
    val isLoading = remember { mutableStateOf(false) }

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
                            //DetailRow(label = "Forest Guard ID (वन रक्षक आईडी)", value = retrivalForm.)
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
                    // Group 6: Banking Details


                    // Group 7: Status
                    item {
                        CompleteFormSectionCard(title = "Form Status (प्रपत्र स्थिति)") {
                            DetailRow(label = "Status (स्थिति)", value = retrivalForm.status?.let {
                                getStatusLabel(
                                    it
                                )
                            })                        }
                    }
                    item {
                        CompleteFormSectionCard(title = "Status History (स्थिति इतिहास)") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp) // Set a fixed height for scrolling
                                    .verticalScroll(rememberScrollState()) // Enables scrolling
                                    .padding(8.dp)
                            ) {
                                Column {
                                    retrivalForm.statusHistory?.forEach { statusUpdate ->
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
                    }
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




                    item{
                        CompleteFormSectionCard(title = "Print Application Form") {
                            if (isLoading.value) {
                                CircularProgressIndicator() // Show loading indicator
                            }
                            else{

                                Button(onClick = {
                                    showDownloadConfirmationDialogPDF(context, complaintRetrievalForm = retrivalForm, isLoading = isLoading)
                                }) {
                                    Text("Print Application Form")
                                }
                            }


                        }
                    }
                }
            }


        }
    }
}





