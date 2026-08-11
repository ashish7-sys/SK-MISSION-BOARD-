package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.ui.components.pressScale
import com.example.data.SubjectRepository
import com.example.model.Chapter
import com.example.ui.components.ChapterCard
import com.example.ui.components.EmptyResourceState
import com.example.ui.components.SKTopBar
import com.example.ui.theme.*

@Composable
fun SubjectScreen(
    subjectId: String,
    onBackClick: () -> Unit,
    onChapterClick: (String) -> Unit
) {
    val subject = SubjectRepository.getSubjectById(subjectId)
    val allChapters = SubjectRepository.getChaptersForSubject(subjectId)

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) } // 0: All, 1: With PDFs, 2: With Videos

    val filteredChapters = remember(searchQuery, selectedTab, allChapters) {
        allChapters.filter { ch ->
            val matchesSearch = searchQuery.isEmpty() ||
                    ch.title.contains(searchQuery, ignoreCase = true) ||
                    (ch.titleHindi?.contains(searchQuery) == true) ||
                    "ch ${ch.number}".contains(searchQuery, ignoreCase = true)

            val matchesTab = when (selectedTab) {
                1 -> SubjectRepository.getPdfsForChapter(ch.id).isNotEmpty()
                2 -> SubjectRepository.getVideosForChapter(ch.id).isNotEmpty()
                else -> true
            }

            matchesSearch && matchesTab
        }
    }

    Scaffold(
        topBar = {
            SKTopBar(
                title = subject?.name ?: "Subject",
                subtitle = subject?.nameHindi ?: "Class 10 Syllabus",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (subject == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Subject not found", color = TextMuted)
            }
            return@Scaffold
        }

        val subjectColor = Color(subject.colorHex)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Subject Header Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("subject_header_banner")
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
                                    text = subject.code,
                                    color = subjectColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Text(
                                text = "Total ${allChapters.size} Chapters Complete Syllabus",
                                color = GoldAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = subject.name,
                                color = TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${subject.nameHindi})",
                                color = subjectColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = subject.description,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Filter Input Inside Subject
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter chapters in ${subject.name}...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = subjectColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("subject_chapter_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = subjectColor,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        label = { Text("All (${allChapters.size})", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = subjectColor,
                            selectedLabelColor = Color.White,
                            containerColor = DarkSurfaceVariant,
                            labelColor = TextSecondary
                        )
                    )

                    FilterChip(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        label = { Text("PDF Notes Available", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanGlow,
                            selectedLabelColor = DarkBackground,
                            containerColor = DarkSurfaceVariant,
                            labelColor = TextSecondary
                        )
                    )

                    FilterChip(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        label = { Text("Videos Available", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = YoutubeRed,
                            selectedLabelColor = Color.White,
                            containerColor = DarkSurfaceVariant,
                            labelColor = TextSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (filteredChapters.isEmpty()) {
                item {
                    EmptyResourceState(
                        title = "No Chapters Found",
                        description = "No chapter matching '$searchQuery' in ${subject.name}.",
                        icon = Icons.Default.Book
                    )
                }
            } else {
                items(filteredChapters, key = { it.id }) { chapter ->
                    val pdfs = SubjectRepository.getPdfsForChapter(chapter.id)
                    val videos = SubjectRepository.getVideosForChapter(chapter.id)
                    val mcqs = SubjectRepository.getMcqsForChapter(chapter.id)

                    ChapterCard(
                        chapter = chapter,
                        pdfCount = pdfs.size,
                        videoCount = videos.size,
                        mcqCount = mcqs.size,
                        onClick = { onChapterClick(chapter.id) }
                    )
                }
            }
        }
    }
}
