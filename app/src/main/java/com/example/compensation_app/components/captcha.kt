package com.example.compensation_app.components
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun CaptchaScreen() {

}

@Composable
fun CaptchaImage(captchaText: String) {
    Canvas(modifier = Modifier.size(200.dp, 50.dp).background(Color.LightGray)) {
        drawRect(Color.White)
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 40f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            captchaText.forEachIndexed { index, char ->
                canvas.nativeCanvas.drawText(
                    char.toString(),
                    30f + (index * 30),
                    40f + Random.nextInt(-10, 10),
                    paint
                )
            }
        }

        // Add some distortion lines
        repeat(5) {
            drawLine(
                color = Color.Black,
                start = Offset(Random.nextFloat() * size.width, Random.nextFloat() * size.height),
                end = Offset(Random.nextFloat() * size.width, Random.nextFloat() * size.height),
                strokeWidth = 2f
            )
        }
    }
}

fun generateCaptchaText(): String {
    val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    return (1..5).map { chars.random() }.joinToString("")
}
