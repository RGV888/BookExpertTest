package com.pp.bookxpert.composescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.pp.bookxpert.utils.AppUtils.downloadPdfFile
import java.io.File

@Composable
fun PdfViewerScreen(
    pdfUrl: String,
    onCloseClick: () -> Unit
) {
    val context = LocalContext.current
    var pdfFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(Unit) {
        pdfFile = downloadPdfFile(context, pdfUrl)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        pdfFile?.let { file ->
            AndroidView(
                factory = {
                    PDFView(it, null).apply {
                        fromFile(file)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .load()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } ?: run {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close PDF",
                tint = Color.White
            )
        }
    }
}

