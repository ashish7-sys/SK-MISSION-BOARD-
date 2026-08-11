package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.R
import com.example.data.SubjectRepository
import com.example.ui.components.SKTopBar
import com.example.ui.components.pressScale
import com.example.ui.theme.*

@Composable
fun VideoPlayerScreen(
    videoId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val video = remember(videoId) {
        SubjectRepository.videos.value.find { it.id == videoId }
    }

    Scaffold(
        topBar = {
            SKTopBar(
                title = video?.title ?: "Video Lecture",
                subtitle = "SK MISSION BOARD YouTube Lecture",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (video == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Video not found", color = TextMuted)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Player Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            val embedUrl = "https://www.youtube.com/embed/${video.youtubeVideoId}?autoplay=1"
                            loadUrl(embedUrl)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = YoutubeRed.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "OFFICIAL CHAPTER LECTURE",
                        color = YoutubeRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = video.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Watch in YouTube App Button
                val openYtInteraction = remember { MutableInteractionSource() }
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl))
                        context.startActivity(intent)
                    },
                    interactionSource = openYtInteraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .pressScale(interactionSource = openYtInteraction)
                        .testTag("open_youtube_app_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = YoutubeRed),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Open in YouTube App",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // SK MISSION BOARD Channel Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sk_logo),
                            contentDescription = "SK MISSION BOARD Official Logo",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "SK MISSION BOARD OFFICIAL CHANNEL",
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )

                        Text(
                            text = "Subscribe to SK MISSION BOARD channel by SK ACADEMY for daily live sessions, doubt clearing & strategy videos.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val channelInteraction = remember { MutableInteractionSource() }
                        Button(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(SubjectRepository.OFFICIAL_YOUTUBE_CHANNEL_URL)
                                )
                                context.startActivity(intent)
                            },
                            interactionSource = channelInteraction,
                            modifier = Modifier
                                .fillMaxWidth()
                                .pressScale(interactionSource = channelInteraction)
                                .testTag("visit_channel_from_player_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OndemandVideo,
                                contentDescription = null,
                                tint = DarkBackground,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Visit SK MISSION BOARD Channel",
                                color = DarkBackground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
