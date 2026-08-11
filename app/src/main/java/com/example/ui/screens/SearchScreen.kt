package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.data.SubjectRepository
import com.example.model.SearchResult
import com.example.ui.components.EmptyResourceState
import com.example.ui.components.SKTopBar
import com.example.ui.components.pressScale
import com.example.ui.theme.*

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onNavigateToSubject: (String) -> Unit,
    onNavigateToChapter: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val searchResults = remember(query) { SubjectRepository.searchAll(query) }

    Scaffold(
        topBar = {
            SKTopBar(
                title = "Global Search",
                subtitle = "Search subjects, chapters, PDFs & videos",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Type chapter name, formula, or subject...", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PurpleGlow) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("global_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface,
                    focusedBorderColor = PurpleGlow,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (query.trim().isEmpty()) {
                EmptyResourceState(
                    title = "Search SK MISSION BOARD",
                    description = "Search across all Class 10 subjects, chapter notes, PDFs, and YouTube video lectures.",
                    icon = Icons.Default.Search
                )
            } else if (searchResults.isEmpty()) {
                EmptyResourceState(
                    title = "No Results Found",
                    description = "No matching subject, chapter, or PDF note for '$query'. Try another keyword.",
                    icon = Icons.Default.Search
                )
            } else {
                Text(
                    text = "${searchResults.size} Search Results Found",
                    color = GoldAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(searchResults, key = { it.id }) { result ->
                        SearchResultCard(
                            result = result,
                            onClick = {
                                if (result.chapterId != null) {
                                    onNavigateToChapter(result.chapterId)
                                } else {
                                    onNavigateToSubject(result.subjectId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(
    result: SearchResult,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val badgeColor = when (result.typeName) {
        "Subject" -> PurpleGlow
        "Chapter" -> CyanGlow
        "PDF Note" -> GoldAccent
        else -> YoutubeRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pressScale(interactionSource = interactionSource)
            .border(1.dp, badgeColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = result.typeName,
                        color = badgeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = result.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = result.subtitle,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
