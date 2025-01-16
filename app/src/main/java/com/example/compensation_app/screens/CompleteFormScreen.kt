package com.example.compensation_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compensation_app.Backend.RetrivalForm
import com.google.gson.Gson
@Composable
fun RetrivalFormDetailsScreen(navController: NavController, encodedForm: String?) {
    val gson = Gson()
    val retrivalForm = encodedForm?.let {
        gson.fromJson(it, RetrivalForm::class.java)
    }

    if (retrivalForm != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Form Details (प्रपत्र विवरण)",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Group 1: Basic Information
                item {
                    SectionCard(title = "Basic Information (मूल जानकारी)") {
                        DetailRow(label = "Form ID (प्रपत्र आईडी)", value = retrivalForm.formID?.toString())
                        DetailRow(label = "Submission Date/Time (जमा करने की तारीख/समय)", value = retrivalForm.submissionDateTime)
                        DetailRow(label = "Forest Guard ID (वन रक्षक आईडी)", value = retrivalForm.forestGuardID)
                        DetailRow(label = "Applicant Name (आवेदक का नाम)", value = retrivalForm.applicantName)
                        DetailRow(label = "Age (आयु)", value = retrivalForm.age?.toString())
                        DetailRow(label = "Father/Spouse Name (पिता/पति का नाम)", value = retrivalForm.fatherSpouseName)
                        DetailRow(label = "Mobile (मोबाइल)", value = retrivalForm.mobile)
                    }
                }

                // Group 2: Incident Details
                item {
                    SectionCard(title = "Incident Details (घटना का विवरण)") {
                        DetailRow(label = "Animal Name (जानवर का नाम)", value = retrivalForm.animalName)
                        DetailRow(label = "Incident Date (घटना की तारीख)", value = retrivalForm.incidentDate)
                        DetailRow(label = "Additional Details (अतिरिक्त विवरण)", value = retrivalForm.additionalDetails)
                        DetailRow(label = "Address (पता)", value = retrivalForm.address)
                    }
                }

                // Group 3: Human Death/Injury
                item {
                    SectionCard(title = "Human Death/Injury (मानव मृत्यु/चोट)") {
                        DetailRow(label = "Name of Victim (पीड़ित का नाम)", value = retrivalForm.humanDeathVictimName)
                        DetailRow(label = "Number of Deaths (मृत्यु की संख्या)", value = retrivalForm.numberOfDeaths?.toString())
                        DetailRow(label = "Temporary Injury Details (अस्थायी चोटों का विवरण)", value = retrivalForm.temporaryInjuryDetails)
                        DetailRow(label = "Permanent Injury Details (स्थायी चोटों का विवरण)", value = retrivalForm.permanentInjuryDetails)
                    }
                }

                // Group 4: Livestock Damage
                item {
                    SectionCard(title = "Livestock Damage (पशुधन क्षति)") {
                        DetailRow(label = "Number of Cattles Died (मरे हुए मवेशियों की संख्या)", value = retrivalForm.numberOfCattlesDied?.toString())
                        DetailRow(label = "Estimated Cattle Age (मवेशियों की अनुमानित आयु)", value = retrivalForm.estimatedCattleAge?.toString())
                    }
                }

                // Group 5: Crop & Property Damage
                item {
                    SectionCard(title = "Crop & Property Damage (फसल और संपत्ति की क्षति)") {
                        DetailRow(label = "Crop Type (फसल का प्रकार)", value = retrivalForm.cropType)
                        DetailRow(label = "Cereal Crop (अनाज की फसल)", value = retrivalForm.cerealCrop)
                        DetailRow(label = "Crop Damage Area (फसल क्षति क्षेत्र)", value = retrivalForm.cropDamageArea?.toString())
                        DetailRow(label = "Full House Damage (पूर्ण घर क्षति)", value = retrivalForm.fullHouseDamage)
                        DetailRow(label = "Partial House Damage (आंशिक घर क्षति)", value = retrivalForm.partialHouseDamage)
                    }
                }

                // Group 6: Banking Details
                item {
                    SectionCard(title = "Banking Details (बैंकिंग विवरण)") {
                        DetailRow(label = "Bank Name (बैंक का नाम)", value = retrivalForm.bankName)
                        DetailRow(label = "IFSC Code (आईएफएससी कोड)", value = retrivalForm.ifscCode)
                        DetailRow(label = "Branch Name (शाखा का नाम)", value = retrivalForm.branchName)
                        DetailRow(label = "Account Holder Name (खाता धारक का नाम)", value = retrivalForm.accountHolderName)
                        DetailRow(label = "Account Number (खाता संख्या)", value = retrivalForm.accountNumber)
                        DetailRow(label = "PAN Number (पैन नंबर)", value = retrivalForm.panNumber)
                        DetailRow(label = "Aadhaar Number (आधार नंबर)", value = retrivalForm.aadhaarNumber)
                    }
                }

                // Group 7: Status
                item {
                    SectionCard(title = "Form Status (प्रपत्र स्थिति)") {
                        DetailRow(label = "Status (स्थिति)", value = retrivalForm.status)
                        DetailRow(label = "Verified By (द्वारा सत्यापित)", value = retrivalForm.verifiedBy)
                        DetailRow(label = "Payment Processed By (द्वारा भुगतान संसाधित)", value = retrivalForm.paymentProcessedBy)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
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
