package com.example.compensation_app.FireStorage

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.storage.FirebaseStorage

import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.compensation_app.R
import com.google.firebase.storage.StorageReference
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class PdfViewerActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val rawPdfUrl = intent.getStringExtra("pdfUrl") ?: return

        // Encode the URL properly to be used as a query parameter in pdf.js
        val encodedPdfUrl = URLEncoder.encode(rawPdfUrl, StandardCharsets.UTF_8.toString())

        val pdfWebView = findViewById<WebView>(R.id.pdfWebView)
        val settings = pdfWebView.settings
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true

        // Corrected URL with proper encoding
        val pdfJsUrl = "https://mozilla.github.io/pdf.js/web/viewer.html?file=$encodedPdfUrl"

        Log.d("PDF Viewer URL", "Loading: $pdfJsUrl")
        pdfWebView.loadUrl(pdfJsUrl)
    }

}
fun openPdfExternally(context: Context, pdfUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No PDF viewer found!", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun UploadButton(
    onFileSelected: (Uri?, String?) -> Unit,
    onUploadComplete: (String?) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileSize = context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1
            if (fileSize > 2 * 1024 * 1024) { // Check if file size > 2MB
                onFileSelected(null, "File size exceeds 2MB.")
            } else {
                onFileSelected(uri, null)

                // Upload the file to Firebase Storage
                uploadPdfToFirebase(uri, context) { downloadUrl ->
                    if (downloadUrl != null) {
                        onUploadComplete(downloadUrl)
                    } else {
                        onUploadComplete(null)
                    }
                }
            }
        } else {
            onFileSelected(null, "No file selected.")
        }
    }

    Button(onClick = { launcher.launch("application/pdf") }) {
        Text(text = "Choose File (फ़ाइल चुनें)")
    }
}
fun uploadPdfToFirebase(uri: Uri, context: Context, onUploadComplete: (String?) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val fileName = "documents/${System.currentTimeMillis()}.pdf"
    val fileRef = storageRef.child(fileName)

    val uploadTask = fileRef.putFile(uri)
    uploadTask
        .addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                onUploadComplete(downloadUrl.toString())
            }.addOnFailureListener {
                onUploadComplete(null)
            }
        }
        .addOnFailureListener {
            onUploadComplete(null)
        }
}

@Composable
fun FileDetails(pdfUri: State<Uri?>, pdfSizeError: State<String?>) {
    pdfUri.value?.let {
        Text(
            text = "Selected file: ${it.path}",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    pdfSizeError.value?.let {
        Text(
            text = it,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
@SuppressLint("QueryPermissionsNeeded")
fun openPdfWithIntent(context: Context, pdfUrl: String) {
    Log.d("URL AT LAST", "openPdfWithIntent: $pdfUrl")
    openPdfExternally(context, pdfUrl)
//    val intent = Intent(context, PdfViewerActivity::class.java)
//    intent.putExtra("pdfUrl", pdfUrl)
//    context.startActivity(intent)



//    getPdfDownloadUrl(pdfUrl, { downloadUrl ->
//        // On success, pass the download URL to PdfViewerActivity
//        val intent = Intent(context, PdfViewerActivity::class.java)
//        intent.putExtra("pdfUrl", downloadUrl)
//        context.startActivity(intent)
//    }, { exception ->
//        Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
//    })
}


fun getPdfDownloadUrl(pdfPath: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
    val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child(pdfPath)

    storageReference.downloadUrl.addOnSuccessListener { uri ->
        // Decode the URL to avoid double encoding issues
        val decodedUrl = Uri.decode(uri.toString()) // This will decode the URL if needed
        onSuccess(decodedUrl)
    }.addOnFailureListener { exception ->
        onFailure(exception)
    }
}


