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

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.compensation_app.R
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
fun SelectPdfButton(onFileSelected: (Uri?, String?) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileSize = context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1
            if (fileSize > 2 * 1024 * 1024) { // Check if file size > 2MB
                onFileSelected(null, "File size exceeds 2MB.")
            } else {
                onFileSelected(uri, null) // Return URI to UI
            }
        } else {
            onFileSelected(null, "No file selected.")
        }
    }

    Button(onClick = { launcher.launch("application/pdf") }) {
        Icon(imageVector = Icons.Default.Info, contentDescription = "Upload PDF")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Choose File (फ़ाइल चुनें)")
    }
}
@Composable
fun ImageRow(label: String, imageUrl: String) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        AsyncImage(
            model = imageUrl,
            contentDescription = label,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )
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
}


