import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.compensation_app.Backend.CompensationForm
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Backend.UserComplaintForm
import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import java.net.URL

import android.os.StrictMode
import android.util.Log
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.InputStream
import java.net.HttpURLConnection

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

suspend fun fetchBitmapFromUrl(imageUrl: String?): Bitmap? {
    if (imageUrl.isNullOrEmpty()) return null // Skip if URL is empty

    return withContext(Dispatchers.IO) { // Run network request in background
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if fetching fails
        }
    }
}

suspend fun addImageToPdf(document: Document, imageUrl: String?) {
    val bitmap = fetchBitmapFromUrl(imageUrl) ?: return
    val image = Image.getInstance(bitmapToByteArray(bitmap))
    image.scaleToFit(200f, 200f)
    image.alignment = Image.ALIGN_CENTER
    document.add(image)
}

@RequiresApi(Build.VERSION_CODES.Q)
fun showDownloadConfirmationDialogPDF(
    context: Context,
    compensationForm: CompensationForm? = null,
    retrivalForm: RetrivalForm? = null,
    complaint: UserComplaintForm? = null,
    complaintRetrievalForm: UserComplaintRetrievalForm? = null,
    isLoading: MutableState<Boolean>
) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Confirm Download")
    builder.setMessage("Do you want to download the Compensation Form?")
    builder.setPositiveButton("Yes") { _, _ ->

        isLoading.value = true // Show loading state

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Generate PDF in background
                when {
                    compensationForm != null -> generateCompensationFormPdfWithLogo(context, compensationForm = compensationForm, isLoading = isLoading)
                    retrivalForm != null -> generateCompensationFormPdfWithLogo(context, retrivalForm = retrivalForm, isLoading = isLoading)
                    complaint != null -> generateCompensationFormPdfWithLogo(context, complaint = complaint, isLoading =  isLoading)
                    complaintRetrievalForm != null -> generateCompensationFormPdfWithLogo(context, complaintRetrievalForm = complaintRetrievalForm, isLoading = isLoading)
                }

                // Update UI on main thread after completion
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    Toast.makeText(context, "PDF Created Successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    Log.d("TAG", "showDownloadConfirmationDialogPDF: $e")
                    Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    builder.setNegativeButton("No") { dialog, _ ->
        dialog.dismiss()
    }
    builder.show()
}

// Function to generate PDF with the form and logo (same as before)
@RequiresApi(Build.VERSION_CODES.Q)
suspend fun generateCompensationFormPdfWithLogo(
    context: Context,
    compensationForm: CompensationForm?=null ,
    retrivalForm: RetrivalForm?=null,
    complaint:UserComplaintForm?=null,
    complaintRetrievalForm: UserComplaintRetrievalForm?=null,
    isLoading: MutableState<Boolean> ) {
    try {
        isLoading.value = true // Start loading
        val document = Document()

        val contentResolver = context.contentResolver


        if (compensationForm!=null){
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "CompensationForm_${compensationForm.forestGuardID}.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            // Insert the file into the MediaStore
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                // Open an OutputStream for the URI
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    PdfWriter.getInstance(document, outputStream)
                    document.open()

                    // Add Logo to the PDF
                    addLogoToPdf(document, context)

                    // Add Title and Form Details
                    document.add(Paragraph("Compensation Claim Form", Font(Font.FontFamily.TIMES_ROMAN, 18f)))
                    document.add(Paragraph("Forest Guard Compensation Application"))
                    document.add(Paragraph("Form ID: ${compensationForm.forestGuardID}"))
                    document.add(Paragraph("\n"))

                    // Personal Details Section
                    document.add(Paragraph("Personal Details"))
                    document.add(Paragraph("Applicant Name: ${compensationForm.applicantName}"))
                    document.add(Paragraph("Age: ${compensationForm.age}"))
                    document.add(Paragraph("Father/Spouse Name: ${compensationForm.fatherSpouseName}"))
                    document.add(Paragraph("Mobile Number: ${compensationForm.mobile}"))
                    document.add(Paragraph("Address: ${compensationForm.address}"))
                    document.add(Paragraph("\n"))
                    document.add(Paragraph("Photo And e-signature"))
                    document.add(Paragraph("\n"))

                    addImageToPdf(document = document,compensationForm.photoUrl)
                    addImageToPdf(document = document,compensationForm.eSignUrl)

                    // Incident Details Section
                    document.add(Paragraph("Incident Details"))
                    document.add(Paragraph("Animal Involved: ${compensationForm.animalName}"))
                    document.add(Paragraph("Incident Date: ${compensationForm.incidentDate}"))
                    document.add(Paragraph("Additional Details: ${compensationForm.additionalDetails}"))
                    document.add(Paragraph("\n"))

                    // Crop Damage Section
                    document.add(Paragraph("Crop Damage"))
                    document.add(Paragraph("Crop Type: ${compensationForm.cropType}"))
                    document.add(Paragraph("Cereal Crop: ${compensationForm.cerealCrop}"))
                    document.add(Paragraph("Damage Area: ${compensationForm.cropDamageArea} hectares"))
                    document.add(Paragraph("\n"))

                    // House Damage Section
                    document.add(Paragraph("House Damage"))
                    document.add(Paragraph("Full House Damage: ${compensationForm.fullHouseDamage}"))
                    document.add(Paragraph("Partial House Damage: ${compensationForm.partialHouseDamage}"))
                    document.add(Paragraph("\n"))

                    // Cattle Damage Section
                    document.add(Paragraph("Cattle Damage"))
                    document.add(Paragraph("Number of Cattles Died: ${compensationForm.numberOfCattlesDied}"))
                    document.add(Paragraph("Estimated Cattle Age: ${compensationForm.estimatedCattleAge}"))
                    document.add(Paragraph("\n"))

                    // Human Death Section
                    document.add(Paragraph("Human Death Details"))
                    document.add(Paragraph("Human Death Victim Name: ${compensationForm.humanDeathVictimName}"))
                    document.add(Paragraph("Number of Deaths: ${compensationForm.numberOfDeaths}"))
                    document.add(Paragraph("\n"))

                    // Injury Details Section
                    document.add(Paragraph("Injury Details"))
                    document.add(Paragraph("Temporary Injury Details: ${compensationForm.temporaryInjuryDetails}"))
                    document.add(Paragraph("Permanent Injury Details: ${compensationForm.permanentInjuryDetails}"))
                    document.add(Paragraph("\n"))

                    // Bank Account Details Section
                    document.add(Paragraph("Bank Account Details"))
                    document.add(Paragraph("Bank Name: ${compensationForm.bankName}"))
                    document.add(Paragraph("IFSC Code: ${compensationForm.ifscCode}"))
                    document.add(Paragraph("Branch Name: ${compensationForm.branchName}"))
                    document.add(Paragraph("Account Holder Name: ${compensationForm.accountHolderName}"))
                    document.add(Paragraph("Account Number: ${compensationForm.accountNumber}"))
                    document.add(Paragraph("PAN Number: ${compensationForm.panNumber}"))
                    document.add(Paragraph("Aadhaar Number: ${compensationForm.aadhaarNumber}"))
                    document.add(Paragraph("\n"))

                    // Status Section
                    document.add(Paragraph("Status: ${compensationForm.status}"))
                    document.add(Paragraph("Document URL: ${compensationForm.documentURL}"))
                    document.add(Paragraph("Incident Images"))

                    document.add(Paragraph("\n"))
                    addImageToPdf(document = document,compensationForm.incidentUrl1)
                    addImageToPdf(document = document,compensationForm.incidentUrl2)
                    addImageToPdf(document = document,compensationForm.incidentUrl3)



                    document.add(Paragraph("Verified By: ${compensationForm.verifiedBy}"))
                    document.add(Paragraph("Payment Processed By: ${compensationForm.paymentProcessedBy}"))
                    document.add(Paragraph("\n"))

                    // Close the document
                    document.close()

                    // Notify user about successful PDF creation
                    sendDownloadCompleteNotification(context)

                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        Toast.makeText(context, "PDF Generated Successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
        else if (retrivalForm!=null){
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "CompensationForm_${retrivalForm.forestGuardID}.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            // Insert the file into the MediaStore
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                // Open an OutputStream for the URI
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    PdfWriter.getInstance(document, outputStream)
                    document.open()

                    // Add Logo to the PDF
                    addLogoToPdf(document, context)

                    // Add Title and Form Details
                    document.add(Paragraph("Compensation Claim Form", Font(Font.FontFamily.TIMES_ROMAN, 18f)))
                    document.add(Paragraph("Forest Guard Compensation Application"))
                    document.add(Paragraph("Form ID: ${retrivalForm.formID}"))
                    document.add(Paragraph("Filled by: ${retrivalForm.forestGuardID}"))
                    document.add(Paragraph("Submission date/time: ${retrivalForm.submissionDateTime}"))

                    document.add(Paragraph("\n"))

                    // Personal Details Section
                    document.add(Paragraph("Personal Details"))
                    document.add(Paragraph("Applicant Name: ${retrivalForm.applicantName}"))
                    document.add(Paragraph("Age: ${retrivalForm.age}"))
                    document.add(Paragraph("Father/Spouse Name: ${retrivalForm.fatherSpouseName}"))
                    document.add(Paragraph("Mobile Number: ${retrivalForm.mobile}"))
                    document.add(Paragraph("Address: ${retrivalForm.address}"))
                    document.add(Paragraph("\n"))
                    document.add(Paragraph("Photo And e-signature"))
                    document.add(Paragraph("\n"))

                    retrivalForm.photoUrl?.let { addImageToPdf(document = document, it) }
                    retrivalForm.eSignUrl?.let { addImageToPdf(document = document, it) }

                    // Incident Details Section
                    document.add(Paragraph("Incident Details"))
                    document.add(Paragraph("Animal Involved: ${retrivalForm.animalName}"))
                    document.add(Paragraph("Incident Date: ${retrivalForm.incidentDate}"))
                    document.add(Paragraph("Additional Details: ${retrivalForm.additionalDetails}"))
                    document.add(Paragraph("\n"))

                    // Crop Damage Section
                    document.add(Paragraph("Crop Damage"))
                    document.add(Paragraph("Crop Type: ${retrivalForm.cropType}"))
                    document.add(Paragraph("Cereal Crop: ${retrivalForm.cerealCrop}"))
                    document.add(Paragraph("Damage Area: ${retrivalForm.cropDamageArea} hectares"))
                    document.add(Paragraph("\n"))

                    // House Damage Section
                    document.add(Paragraph("House Damage"))
                    document.add(Paragraph("Full House Damage: ${retrivalForm.fullHouseDamage}"))
                    document.add(Paragraph("Partial House Damage: ${retrivalForm.partialHouseDamage}"))
                    document.add(Paragraph("\n"))

                    // Cattle Damage Section
                    document.add(Paragraph("Cattle Damage"))
                    document.add(Paragraph("Number of Cattles Died: ${retrivalForm.numberOfCattlesDied}"))
                    document.add(Paragraph("Estimated Cattle Age: ${retrivalForm.estimatedCattleAge}"))
                    document.add(Paragraph("\n"))

                    // Human Death Section
                    document.add(Paragraph("Human Death Details"))
                    document.add(Paragraph("Human Death Victim Name: ${retrivalForm.humanDeathVictimName}"))
                    document.add(Paragraph("Number of Deaths: ${retrivalForm.numberOfDeaths}"))
                    document.add(Paragraph("\n"))

                    // Injury Details Section
                    document.add(Paragraph("Injury Details"))
                    document.add(Paragraph("Temporary Injury Details: ${retrivalForm.temporaryInjuryDetails}"))
                    document.add(Paragraph("Permanent Injury Details: ${retrivalForm.permanentInjuryDetails}"))
                    document.add(Paragraph("\n"))

                    // Bank Account Details Section
                    document.add(Paragraph("Bank Account Details"))
                    document.add(Paragraph("Bank Name: ${retrivalForm.bankName}"))
                    document.add(Paragraph("IFSC Code: ${retrivalForm.ifscCode}"))
                    document.add(Paragraph("Branch Name: ${retrivalForm.branchName}"))
                    document.add(Paragraph("Account Holder Name: ${retrivalForm.accountHolderName}"))
                    document.add(Paragraph("Account Number: ${retrivalForm.accountNumber}"))
                    document.add(Paragraph("PAN Number: ${retrivalForm.panNumber}"))
                    document.add(Paragraph("Aadhaar Number: ${retrivalForm.aadhaarNumber}"))
                    document.add(Paragraph("\n"))

                    document.add(Paragraph("Incident Images"))

                    document.add(Paragraph("\n"))
                    retrivalForm.incidentUrl1?.let { addImageToPdf(document = document, it) }
                    retrivalForm.incidentUrl2?.let { addImageToPdf(document = document, it) }
                    retrivalForm.incidentUrl3?.let { addImageToPdf(document = document, it) }


                    // Status Section
                    document.add(Paragraph("Status: ${retrivalForm.status}"))
                    document.add(Paragraph("Document URL: ${retrivalForm.documentURL}"))
                    document.add(Paragraph("Verified By: ${retrivalForm.verifiedBy}"))
                    document.add(Paragraph("Payment Processed By: ${retrivalForm.paymentProcessedBy}"))
                    document.add(Paragraph("\n"))

                    // Close the document
                    document.close()

                    // Notify user about successful PDF creation
                    sendDownloadCompleteNotification(context)
                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        Toast.makeText(context, "PDF Generated Successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
        else if ( complaint!=null){
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "CompensationForm_${complaint.mobile}.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            // Insert the file into the MediaStore
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                // Open an OutputStream for the URI
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    PdfWriter.getInstance(document, outputStream)
                    document.open()

                    // Add Logo to the PDF
                    addLogoToPdf(document, context)

                    // Add Title and Form Details
                    document.add(Paragraph("Complaint Form", Font(Font.FontFamily.TIMES_ROMAN, 18f)))
                    document.add(Paragraph("Forest Guard Compensation Application"))
                    document.add(Paragraph("\n"))

                    // Personal Details Section
                    document.add(Paragraph("Personal Details"))
                    document.add(Paragraph("Applicant Name: ${complaint.name}"))
                    document.add(Paragraph("Age: ${complaint.age}"))
                    document.add(Paragraph("Father/Spouse Name: ${complaint.fatherOrSpouseName}"))
                    document.add(Paragraph("Mobile Number: ${complaint.mobile}"))
                    document.add(Paragraph("Address: ${complaint.address}"))
                    document.add(Paragraph("\n"))
                    document.add(Paragraph("Photo And e-signature"))
                    document.add(Paragraph("\n"))

                    complaint.photoUrl.let { addImageToPdf(document = document, it) }
                    complaint.eSignUrl.let { addImageToPdf(document = document, it) }


                    // Incident Details Section
                    document.add(Paragraph("Incident Details"))
                    document.add(Paragraph("Animal Involved: ${complaint.animalList}"))
                    document.add(Paragraph("Incident Date: ${complaint.damageDate}"))
                    document.add(Paragraph("Additional Details: ${complaint.additionalDetails}"))
                    document.add(Paragraph("\n"))

                    // Crop Damage Section
                    document.add(Paragraph("Crop Damage"))
                    document.add(Paragraph("Crop Type: ${complaint.cropType}"))
                    document.add(Paragraph("Cereal Crop: ${complaint.cerealCrop}"))
                    document.add(Paragraph("\n"))

                    // House Damage Section
                    document.add(Paragraph("House Damage"))
                    document.add(Paragraph("Full House Damage: ${complaint.fullHousesDamaged}"))
                    document.add(Paragraph("Partial House Damage: ${complaint.partialHousesDamaged}"))
                    document.add(Paragraph("\n"))

                    // Cattle Damage Section
                    document.add(Paragraph("Cattle Damage"))
                    document.add(Paragraph("Number of Cattles Died: ${complaint.cattleInjuryNumber}"))
                    document.add(Paragraph("Estimated Cattle Age: ${complaint.cattleInjuryEstimatedAge}"))
                    document.add(Paragraph("\n"))

                    // Human Death Section
                    document.add(Paragraph("Human Death Details"))
                    document.add(Paragraph("Human Death Victim Name: ${complaint.humanDeathVictimNames}"))
                    document.add(Paragraph("Number of Deaths: ${complaint.humanDeathNumber}"))
                    document.add(Paragraph("\n"))

                    // Injury Details Section
                    document.add(Paragraph("Injury Details"))
                    document.add(Paragraph("Temporary Injury Details: ${complaint.temporaryInjuryDetails}"))
                    document.add(Paragraph("Permanent Injury Details: ${complaint.permanentInjuryDetails}"))
                    document.add(Paragraph("\n"))


                    // Status Section
                    document.add(Paragraph("Status: ${complaint.status}"))
                    document.add(Paragraph("\n"))
                    document.add(Paragraph("Incident Images"))

                    document.add(Paragraph("\n"))
                    complaint.incidentUrl1.let { addImageToPdf(document = document, it) }
                    complaint.incidentUrl2.let { addImageToPdf(document = document, it) }
                    complaint.incidentUrl3.let { addImageToPdf(document = document, it) }

                    // Close the document
                    document.close()

                    // Notify user about successful PDF creation
                    sendDownloadCompleteNotification(context)

                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        Toast.makeText(context, "PDF Generated Successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
        else if (complaintRetrievalForm!=null){
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "CompensationForm_${complaintRetrievalForm.complaint_id}.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            // Insert the file into the MediaStore
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                // Open an OutputStream for the URI
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    PdfWriter.getInstance(document, outputStream)
                    document.open()

                    // Add Logo to the PDF
                    addLogoToPdf(document, context)

                    // Add Title and Form Details
                    document.add(Paragraph("Complaint Form", Font(Font.FontFamily.TIMES_ROMAN, 18f)))
                    document.add(Paragraph("Forest Guard Compensation Application"))
                    document.add(Paragraph("Form ID: ${complaintRetrievalForm.complaint_id}"))
                    document.add(Paragraph("Submission date/time: ${complaintRetrievalForm.SubmissionDateTime}"))
                    document.add(Paragraph("\n"))

                    // Personal Details Section
                    document.add(Paragraph("Personal Details"))
                    document.add(Paragraph("Applicant Name: ${complaintRetrievalForm.name}"))
                    document.add(Paragraph("Age: ${complaintRetrievalForm.age}"))
                    document.add(Paragraph("Father/Spouse Name: ${complaintRetrievalForm.fatherOrSpouseName}"))
                    document.add(Paragraph("Mobile Number: ${complaintRetrievalForm.mobile}"))
                    document.add(Paragraph("Address: ${complaintRetrievalForm.address}"))
                    document.add(Paragraph("\n"))

                    document.add(Paragraph("Photo And e-signature"))
                    document.add(Paragraph("\n"))

                    complaintRetrievalForm.photoUrl?.let { addImageToPdf(document = document, it) }
                    complaintRetrievalForm.eSignUrl?.let { addImageToPdf(document = document, it) }


                    // Incident Details Section
                    document.add(Paragraph("Incident Details"))
                    document.add(Paragraph("Animal Involved: ${complaintRetrievalForm.animalList}"))
                    document.add(Paragraph("Incident Date: ${complaintRetrievalForm.damageDate}"))
                    document.add(Paragraph("Additional Details: ${complaintRetrievalForm.additionalDetails}"))
                    document.add(Paragraph("\n"))

                    // Crop Damage Section
                    document.add(Paragraph("Crop Damage"))
                    document.add(Paragraph("Crop Type: ${complaintRetrievalForm.cropType}"))
                    document.add(Paragraph("Cereal Crop: ${complaintRetrievalForm.cerealCrop}"))
                    document.add(Paragraph("\n"))

                    // House Damage Section
                    document.add(Paragraph("House Damage"))
                    document.add(Paragraph("Full House Damage: ${complaintRetrievalForm.fullHousesDamaged}"))
                    document.add(Paragraph("Partial House Damage: ${complaintRetrievalForm.partialHousesDamaged}"))
                    document.add(Paragraph("\n"))

                    // Cattle Damage Section
                    document.add(Paragraph("Cattle Damage"))
                    document.add(Paragraph("Number of Cattles Died: ${complaintRetrievalForm.cattleInjuryNumber}"))
                    document.add(Paragraph("Estimated Cattle Age: ${complaintRetrievalForm.cattleInjuryEstimatedAge}"))
                    document.add(Paragraph("\n"))

                    // Human Death Section
                    document.add(Paragraph("Human Death Details"))
                    document.add(Paragraph("Human Death Victim Name: ${complaintRetrievalForm.humanDeathVictimNames}"))
                    document.add(Paragraph("Number of Deaths: ${complaintRetrievalForm.humanDeathNumber}"))
                    document.add(Paragraph("\n"))

                    // Injury Details Section
                    document.add(Paragraph("Injury Details"))
                    document.add(Paragraph("Temporary Injury Details: ${complaintRetrievalForm.temporaryInjuryDetails}"))
                    document.add(Paragraph("Permanent Injury Details: ${complaintRetrievalForm.permanentInjuryDetails}"))
                    document.add(Paragraph("\n"))


                    // Status Section
                    document.add(Paragraph("Status: ${complaintRetrievalForm.status}"))
                    document.add(Paragraph("\n"))
                    document.add(Paragraph("Incident Images"))

                    document.add(Paragraph("\n"))
                    complaintRetrievalForm.incidentUrl1?.let { addImageToPdf(document = document, it) }
                    complaintRetrievalForm.incidentUrl2?.let { addImageToPdf(document = document, it) }
                    complaintRetrievalForm.incidentUrl3?.let { addImageToPdf(document = document, it) }
                    // Close the document
                    document.close()

                    // Notify user about successful PDF creation
                    sendDownloadCompleteNotification(context)

                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        Toast.makeText(context, "PDF Generated Successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Access the Downloads directory via MediaStore for Android 10+

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error generating PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
    finally {
        isLoading.value=false
    }
}

// Function to add logo to the PDF
fun addLogoToPdf(document: Document, context: Context) {
    try {
        // Get the logo bitmap
        val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo) // Replace with your logo's resource ID

        // Convert the Bitmap to a byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Convert the byte array into an Image
        val logo = Image.getInstance(byteArray)

        // Scale the image if necessary (optional)
        logo.scaleToFit(200f, 100f)  // Adjust size if necessary
        logo.alignment = Image.ALIGN_CENTER

        // Add the logo to the document
        document.add(logo)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Function to send a notification after download completion
fun sendDownloadCompleteNotification(context: Context) {
    // Create a notification channel (only for Android 8 and above)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "download_complete_channel",
            "Download Complete",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notification for PDF download completion"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Create a notification
    val notification = NotificationCompat.Builder(context, "download_complete_channel")
        .setSmallIcon(android.R.drawable.stat_sys_download_done) // Set an icon for the notification
        .setContentTitle("Download Complete")
        .setContentText("The Compensation Form PDF has been successfully downloaded.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    // Show the notification
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    NotificationManagerCompat.from(context).notify(0, notification)
}

fun getBitmapFromDrawable(context: Context, drawableResId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableResId) as BitmapDrawable
    return drawable.bitmap
}
