import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.compensation_app.Backend.CompensationForm
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

// Function to display confirmation dialog
@RequiresApi(Build.VERSION_CODES.Q)
fun showDownloadConfirmationDialog(context: Context, compensationForm: CompensationForm?=null , retrivalForm: RetrivalForm?=null) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Confirm Download")
    builder.setMessage("Do you want to download the Compensation Form?")
    builder.setPositiveButton("Yes") { _, _ ->
        // If user confirms, generate the PDF
        if(compensationForm!=null){
            generateCompensationFormPdfWithLogo(context, compensationForm,null)

        }
        else{
            generateCompensationFormPdfWithLogo(context,null, retrivalForm)
        }
    }
    builder.setNegativeButton("No") { dialog, _ ->
        dialog.dismiss()
    }
    builder.show()
}

// Function to generate PDF with the form and logo (same as before)
@RequiresApi(Build.VERSION_CODES.Q)
fun generateCompensationFormPdfWithLogo(context: Context, compensationForm: CompensationForm?=null , retrivalForm: RetrivalForm?=null) {
    try {
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
                    document.add(Paragraph("Verified By: ${compensationForm.verifiedBy}"))
                    document.add(Paragraph("Payment Processed By: ${compensationForm.paymentProcessedBy}"))
                    document.add(Paragraph("\n"))

                    // Close the document
                    document.close()

                    // Notify user about successful PDF creation
                    sendDownloadCompleteNotification(context)

                    Toast.makeText(context, "PDF Created Successfully in Downloads Folder!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Failed to create PDF file in Downloads folder", Toast.LENGTH_SHORT).show()
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
                    document.add(Paragraph("Form ID: ${retrivalForm.forestGuardID}"))
                    document.add(Paragraph("\n"))

                    // Personal Details Section
                    document.add(Paragraph("Personal Details"))
                    document.add(Paragraph("Applicant Name: ${retrivalForm.applicantName}"))
                    document.add(Paragraph("Age: ${retrivalForm.age}"))
                    document.add(Paragraph("Father/Spouse Name: ${retrivalForm.fatherSpouseName}"))
                    document.add(Paragraph("Mobile Number: ${retrivalForm.mobile}"))
                    document.add(Paragraph("Address: ${retrivalForm.address}"))
                    document.add(Paragraph("\n"))

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

                    Toast.makeText(context, "PDF Created Successfully in Downloads Folder!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Failed to create PDF file in Downloads folder", Toast.LENGTH_SHORT).show()
            }
        }

        // Access the Downloads directory via MediaStore for Android 10+

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error generating PDF: ${e.message}", Toast.LENGTH_LONG).show()
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
