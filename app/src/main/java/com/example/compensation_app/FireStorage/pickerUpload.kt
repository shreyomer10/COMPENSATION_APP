package com.example.compensation_app.FireStorage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.storage.FirebaseStorage

@Composable
fun ImagePickerButton(label: String, onImageSelected: (Uri?) -> Unit) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri) // Pass the selected image URI
    }

    Button(
        onClick = { imagePickerLauncher.launch("image/*") },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Icon(imageVector = Icons.Default.Info, contentDescription = label)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}
fun uploadFileToFirebase(uri: Uri, path: String, onUploadComplete: (String?) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference.child(path)

    storageRef.putFile(uri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                onUploadComplete(downloadUrl.toString())  // Return the file URL
            }.addOnFailureListener {
                onUploadComplete(null)
            }
        }
        .addOnFailureListener {
            onUploadComplete(null)
        }
}
fun uploadFormFiles(
    forestGuardId: String, // ID of the forest guard
    applicantMobile: String, // Mobile number of the applicant
    photoUri: Uri?,
    eSignUri: Uri?,
    idProof: Uri?,
    cropdamagePhoto: Uri?,
    nocReport: Uri?,
    landOwnershipReport: Uri?,
    rorReport: Uri?,
    housedamagePhoto1: Uri?,
    housedamagePhoto2: Uri?,
    propertyOwnerReport: Uri?,

    cattlePhoto: Uri?,
    vasCertificate: Uri?,
    humanPhoto1: Uri?,
    humanPhoto2: Uri?,
    deathCertificate: Uri?,
    sarpanchReport: Uri?,
    pmReport: Uri?,
    humanInjuryPhoto: Uri?,
    medicalCertificate: Uri?,


    onComplete: (Map<String, String>) -> Unit // Callback with uploaded URLs
) {
    val folderName = "${forestGuardId}_${applicantMobile}"  // Unique folder for each form
    val uploadedUrls = mutableMapOf<String, String>()
    var filesToUpload = 0
    var completedUploads = 0

    // 🔹 Function to check completion
    fun checkCompletion() {
        if (completedUploads == filesToUpload) {
            // 🔹 Decode all URLs before returning
//            val decodedUrls = uploadedUrls.mapValues { (_, url) ->
//                URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
//            }
            onComplete(uploadedUrls) // Return decoded URLs
        }
    }

    // 🔹 Upload only non-null files
    fun uploadIfNotNull(uri: Uri?, fileName: String) {
        if (uri != null) {
            filesToUpload++ // Count files to be uploaded
            val path = "forms/$folderName/$fileName"
            uploadFileToFirebase(uri, path) { url ->
                if (url != null) {
                    uploadedUrls[fileName] = url  // Store raw URL
                }
                completedUploads++
                checkCompletion()
            }
        }
    }

    // **Mandatory Files (must be there)**
    if (photoUri != null && eSignUri != null && idProof != null) {
        uploadIfNotNull(photoUri, "photo.jpg")
        uploadIfNotNull(eSignUri, "esign.jpg")
        uploadIfNotNull(idProof ,"idProof.pdf")
        uploadIfNotNull(cropdamagePhoto, "cropDamagePhoto.jpg")
        uploadIfNotNull(nocReport, "nocReport.pdf")
        uploadIfNotNull(landOwnershipReport, "landOwnershipReport.pdf")
        uploadIfNotNull(rorReport, "rorReport.pdf")
        uploadIfNotNull(housedamagePhoto1, "housedamagePhoto1.jpg")
        uploadIfNotNull(housedamagePhoto2, "housedamagePhoto2.jpg")
        uploadIfNotNull(propertyOwnerReport, "document.pdf")
        uploadIfNotNull(cattlePhoto, "cattlePhoto.jpg")
        uploadIfNotNull(vasCertificate, "vasCertificate.pdf")

        // **Optional Incident Photos**
        uploadIfNotNull(humanPhoto1, "humanPhoto1.jpg")
        uploadIfNotNull(humanPhoto2, "humanPhoto2.jpg")
        uploadIfNotNull(deathCertificate, "deathCertificate.pdf")
        uploadIfNotNull(sarpanchReport, "sarpanchReport.pdf")
        uploadIfNotNull(pmReport, "pmReport.pdf")
        uploadIfNotNull(humanInjuryPhoto, "humanInjuryPhoto.jpg")
        uploadIfNotNull(medicalCertificate, "medicalCertificate.pdf")





    } else {
        // 🔴 If any mandatory file is missing, do NOT upload
        onComplete(emptyMap())
    }
}
fun deleteFormFiles(forestGuardId: String, applicantMobile: String, onComplete: (Boolean) -> Unit) {
    val folderPath = "forms/${forestGuardId}_${applicantMobile}"  // Folder where files are stored
    val storageRef = FirebaseStorage.getInstance().reference.child(folderPath)

    // List all files in the folder
    storageRef.listAll()
        .addOnSuccessListener { listResult ->
            val totalFiles = listResult.items.size
            var deletedFiles = 0

            if (totalFiles == 0) {
                onComplete(true) // Nothing to delete
                return@addOnSuccessListener
            }

            // Delete each file
            listResult.items.forEach { fileRef ->
                fileRef.delete()
                    .addOnSuccessListener {
                        deletedFiles++
                        if (deletedFiles == totalFiles) {
                            onComplete(true)  // All files deleted
                        }
                    }
                    .addOnFailureListener {
                        onComplete(false) // Failed to delete some files
                    }
            }
        }
        .addOnFailureListener {
            onComplete(false) // Failed to list folder contents
        }
}

fun uploadComplaintFiles(
    username: String, // Name of the user
    mobileNumber: String, // Mobile number of the user
    photoUri: Uri?, // Photo of the complaint
    eSignUri: Uri?, // E-Signature
    incident1: Uri?, // Incident photo 1
    incident2: Uri?, // Incident photo 2
    incident3: Uri?, // Incident photo 3
    onComplete: (Map<String, String>) -> Unit // Callback with uploaded URLs
) {
    val folderName = "${username}_${mobileNumber}"  // Unique folder for each complaint
    val uploadedUrls = mutableMapOf<String, String>()
    var filesToUpload = 0
    var completedUploads = 0

    // Function to check completion
    fun checkCompletion() {
        if (completedUploads == filesToUpload) {
            onComplete(uploadedUrls) // Return uploaded URLs
        }
    }

    // Upload only non-null files
    fun uploadIfNotNull(uri: Uri?, fileName: String) {
        if (uri != null) {
            filesToUpload++ // Count files to be uploaded
            val path = "complaints/$folderName/$fileName" // Updated path for complaints
            uploadFileToFirebase(uri, path) { url ->
                if (url != null) {
                    uploadedUrls[fileName] = url // Store the uploaded URL
                }
                completedUploads++
                checkCompletion()
            }
        }
    }

    // **Mandatory Files (photo and eSign must be there)**
    if (photoUri != null && eSignUri != null) {
        uploadIfNotNull(photoUri, "photo.jpg")
        uploadIfNotNull(eSignUri, "esign.jpg")

        // **Optional Incident Photos**
        uploadIfNotNull(incident1, "incident1.jpg")
        uploadIfNotNull(incident2, "incident2.jpg")
        uploadIfNotNull(incident3, "incident3.jpg")
    } else {
        // 🔴 If any mandatory file is missing, do NOT upload
        onComplete(emptyMap())
    }
}
fun encodeFirebaseUrl(url: String?): String? {
    if (url == null) return null

    return url.replace(Regex("(?<=/o/)([^?]+)")) { matchResult ->
        matchResult.value.replace("/", "%2F")
    }
}
