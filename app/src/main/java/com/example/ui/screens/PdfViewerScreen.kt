package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.data.SubjectRepository
import com.example.ui.components.SKTopBar
import com.example.ui.components.pressScale
import com.example.ui.theme.*

@Composable
fun PdfViewerScreen(
    pdfId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val pdf = remember(pdfId) {
        SubjectRepository.pdfs.value.find { it.id == pdfId }
    }

    Scaffold(
        topBar = {
            SKTopBar(
                title = pdf?.title ?: "PDF Viewer",
                subtitle = "SK MISSION BOARD Study Material",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (pdf == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("PDF Document not found", color = TextMuted)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // PDF Details Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, CyanGlow.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = CyanGlow,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = pdf.title,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Size: ${pdf.fileSize} • ${pdf.pageCount} Pages",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdf.pdfUrl))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.testTag("pdf_open_external_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Open External",
                            tint = CyanGlow
                        )
                    }
                }
            }

            // PDF WebView Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                    .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            ) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = true
                            // Google Docs viewer engine for web PDF render
                            val docsViewerUrl = "https://docs.google.com/gview?embedded=true&url=${pdf.pdfUrl}"
                            loadUrl(docsViewerUrl)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Bottom Action Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = DarkSurface,
                tonalElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val fullBtnInteraction = remember { MutableInteractionSource() }
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdf.pdfUrl))
                            context.startActivity(intent)
                            Toast.makeText(context, "Opening PDF...", Toast.LENGTH_SHORT).show()
                        },
                        interactionSource = fullBtnInteraction,
                        modifier = Modifier
                            .weight(1f)
                            .pressScale(interactionSource = fullBtnInteraction)
                            .testTag("pdf_view_fullscreen_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = null, tint = DarkBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Fullscreen", color = DarkBackground, fontWeight = FontWeight.Bold)
                    }

                    val downloadBtnInteraction = remember { MutableInteractionSource() }
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Downloading ${pdf.title}...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdf.pdfUrl))
                            context.startActivity(intent)
                        },
                        interactionSource = downloadBtnInteraction,
                        modifier = Modifier
                            .weight(1f)
                            .pressScale(interactionSource = downloadBtnInteraction)
                            .testTag("pdf_download_bottom_button"),
                        border = BorderStroke(1.dp, CyanGlow),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = CyanGlow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download PDF", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
