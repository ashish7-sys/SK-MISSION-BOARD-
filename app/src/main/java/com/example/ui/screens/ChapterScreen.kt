package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SubjectRepository
import com.example.model.McqItem
import com.example.model.PdfResource
import com.example.model.VideoResource
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ChapterScreen(
    chapterId: String,
    onBackClick: () -> Unit,
    onViewPdf: (PdfResource) -> Unit,
    onWatchVideo: (VideoResource) -> Unit
) {
    val context = LocalContext.current
    val chapter = SubjectRepository.getChapterById(chapterId)
    val subject = chapter?.let { SubjectRepository.getSubjectById(it.subjectId) }

    val pdfs = SubjectRepository.getPdfsForChapter(chapterId)
    val videos = SubjectRepository.getVideosForChapter(chapterId)
    val mcqs = SubjectRepository.getMcqsForChapter(chapterId)

    var selectedTabState by remember { mutableStateOf(0) } // 0: PDFs, 1: Videos, 2: MCQs

    Scaffold(
        topBar = {
            SKTopBar(
                title = chapter?.let { "Ch ${it.number}: ${it.title}" } ?: "Chapter Details",
                subtitle = subject?.name ?: "SK MISSION BOARD",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (chapter == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Chapter not found", color = TextMuted)
            }
            return@Scaffold
        }

        val subjectColor = subject?.let { Color(it.colorHex) } ?: PurpleGlow

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Chapter Details Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chapter_header_card")
                        .border(1.dp, subjectColor.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(subjectColor.copy(alpha = 0.2f), DarkSurface)
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = subjectColor.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "CHAPTER ${chapter.number}",
                                    color = subjectColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Text(
                                text = subject?.name ?: "",
                                color = GoldAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = chapter.title,
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 26.sp
                        )

                        if (!chapter.titleHindi.isNull_or_Empty()) {
                            Text(
                                text = chapter.titleHindi!!,
                                color = CyanGlow,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        if (!chapter.description.isNull_or_Empty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = chapter.description!!,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Selector Bar
                TabRow(
                    selectedTabIndex = selectedTabState,
                    containerColor = DarkSurface,
                    contentColor = TextPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabState]),
                            color = when (selectedTabState) {
                                0 -> CyanGlow
                                1 -> YoutubeRed
                                else -> SuccessGreen
                            }
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTabState == 0,
                        onClick = { selectedTabState = 0 },
                        text = {
                            Text(
                                text = "PDFs (${pdfs.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabState == 1,
                        onClick = { selectedTabState = 1 },
                        text = {
                            Text(
                                text = "Videos (${videos.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabState == 2,
                        onClick = { selectedTabState = 2 },
                        text = {
                            Text(
                                text = "MCQs (${mcqs.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // TAB CONTENT: 0 - PDFs
            if (selectedTabState == 0) {
                if (pdfs.isEmpty()) {
                    item {
                        EmptyResourceState(
                            title = "No PDF Material Added Yet",
                            description = "Study notes, chapter PDFs & PYQs for Chapter ${chapter.number} will be uploaded soon by SK ACADEMY team.",
                            icon = Icons.Default.PictureAsPdf
                        )
                    }
                } else {
                    items(pdfs, key = { it.id }) { pdf ->
                        PdfItemCard(
                            pdf = pdf,
                            onViewClick = { onViewPdf(pdf) },
                            onDownloadClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdf.pdfUrl))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }

            // TAB CONTENT: 1 - VIDEOS (CRITICAL ORDERING RULES)
            if (selectedTabState == 1) {
                if (videos.isNotEmpty()) {
                    item {
                        Text(
                            text = "AVAILABLE LECTURES (${videos.size})",
                            color = GoldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // CATEGORY A: ALL AVAILABLE VIDEOS SHOWN FIRST
                    items(videos, key = { it.id }) { video ->
                        VideoItemCard(
                            video = video,
                            onWatchClick = { onWatchVideo(video) }
                        )
                    }
                }

                // CATEGORY B: FALLBACK SECTION ALWAYS SHOWN BELOW AVAILABLE VIDEOS OR AS MAIN IF NO VIDEOS
                item {
                    VideoFallbackSection(hasAvailableVideos = videos.isNotEmpty())
                }
            }

            // TAB CONTENT: 2 - MCQs
            if (selectedTabState == 2) {
                if (mcqs.isEmpty()) {
                    item {
                        EmptyResourceState(
                            title = "MCQ Practice Quiz Coming Soon",
                            description = "Multiple choice questions with explanations for Chapter ${chapter.number} are being prepared.",
                            icon = Icons.Default.Quiz
                        )
                    }
                } else {
                    items(mcqs, key = { it.id }) { mcq ->
                        McqPracticeCard(mcq = mcq)
                    }
                }
            }
        }
    }
}

@Composable
fun McqPracticeCard(mcq: McqItem) {
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = mcq.question,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            mcq.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOptionIndex == index
                val isCorrect = index == mcq.correctOptionIndex

                val optionColor = when {
                    selectedOptionIndex == null -> DarkSurfaceVariant
                    isSelected && isCorrect -> SuccessGreen.copy(alpha = 0.3f)
                    isSelected && !isCorrect -> YoutubeRed.copy(alpha = 0.3f)
                    isCorrect && selectedOptionIndex != null -> SuccessGreen.copy(alpha = 0.2f)
                    else -> DarkSurfaceVariant
                }

                val optionBorder = when {
                    selectedOptionIndex == null -> DarkBorder
                    isSelected && isCorrect -> SuccessGreen
                    isSelected && !isCorrect -> YoutubeRed
                    isCorrect && selectedOptionIndex != null -> SuccessGreen
                    else -> DarkBorder
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { selectedOptionIndex = index },
                    color = optionColor,
                    border = BorderStroke(1.dp, optionBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${('A' + index)}. ",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = optionText,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )

                        if (selectedOptionIndex != null) {
                            if (isCorrect) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = SuccessGreen)
                            } else if (isSelected) {
                                Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = YoutubeRed)
                            }
                        }
                    }
                }
            }

            if (selectedOptionIndex != null && !mcq.explanation.isNull_or_Empty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceVariant
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = "Explanation:", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(text = mcq.explanation!!, color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private fun String?.isNull_or_Empty(): Boolean = this == null || this.trim().isEmpty()
