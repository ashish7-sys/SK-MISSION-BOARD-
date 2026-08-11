package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.SubjectRepository
import com.example.model.Subject
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.ui.components.GlowTargetContainer
import com.example.ui.components.SKTopBar
import com.example.ui.components.SubjectCard
import com.example.ui.components.pressScale
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    onSubjectClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onPdfClick: (String) -> Unit
) {
    val context = LocalContext.current
    val subjects by SubjectRepository.subjects.collectAsState()

    Scaffold(
        topBar = {
            SKTopBar(
                title = "SK MISSION BOARD",
                subtitle = "by SK ACADEMY • Class 10",
                onSearchClick = onSearchClick
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Welcome & Mission Card
                val missionCardInteraction = remember { MutableInteractionSource() }
                GlowTargetContainer(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("welcome_mission_card")
                            .pressScale(interactionSource = missionCardInteraction)
                            .border(
                                1.dp,
                                Brush.linearGradient(listOf(PurpleGlow, CyanGlow)),
                                RoundedCornerShape(20.dp)
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(PurpleGlow.copy(alpha = 0.15f), DarkSurface),
                                        radius = 800f
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = PurpleGlow.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(GoldAccent)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "MISSION 450+ MARKS",
                                            color = GoldAccent,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }

                                Text(
                                    text = "Class 10 Bihar Board/NCERT",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.sk_logo),
                                    contentDescription = "SK MISSION BOARD Official Logo",
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Fit
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Welcome to SK MISSION BOARD",
                                        color = TextPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        lineHeight = 22.sp
                                    )

                                    Text(
                                        text = "Study Today • Bright Tomorrow\nसही दिशा • सही ज्ञान • उज्ज्वल भविष्य",
                                        color = GoldAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 15.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Complete Class 10 Syllabus, Chapter-wise Study Notes, PDFs, Important VVI Questions & Video Lectures by SK ACADEMY.",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Quick Search Bar Trigger
                            val searchTriggerInteraction = remember { MutableInteractionSource() }
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .pressScale(interactionSource = searchTriggerInteraction)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(
                                        interactionSource = searchTriggerInteraction,
                                        indication = ripple(),
                                        onClick = onSearchClick
                                    ),
                                color = DarkSurfaceVariant,
                                border = BorderStroke(1.dp, DarkBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = CyanGlow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Search chapters, notes, PDFs, or video lectures...",
                                        color = TextMuted,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section Header: Subjects
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(PurpleGlow)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ALL SUBJECTS",
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Text(
                        text = "${subjects.size} Subjects",
                        color = CyanGlow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Subject Cards List
            items(subjects, key = { it.id }) { subject ->
                SubjectCard(
                    subject = subject,
                    onClick = { onSubjectClick(subject.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                // Section Header: Quick Access
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(CyanGlow)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "QUICK ACCESS RESOURCES",
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickResourceTile(
                        modifier = Modifier.weight(1f),
                        title = "Science Hand Notes",
                        subtitle = "Ch 1 to 16 Complete",
                        icon = Icons.Default.Science,
                        accentColor = PurpleGlow,
                        onClick = { onSubjectClick("science") }
                    )

                    QuickResourceTile(
                        modifier = Modifier.weight(1f),
                        title = "Math Formulas",
                        subtitle = "Ch 1 to 15 Mind Maps",
                        icon = Icons.Default.Calculate,
                        accentColor = CyanGlow,
                        onClick = { onSubjectClick("mathematics") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickResourceTile(
                        modifier = Modifier.weight(1f),
                        title = "SST 10-Yr PYQs",
                        subtitle = "Solved Board Exam",
                        icon = Icons.Default.HistoryEdu,
                        accentColor = GoldAccent,
                        onClick = { onSubjectClick("social_science") }
                    )

                    QuickResourceTile(
                        modifier = Modifier.weight(1f),
                        title = "Hindi Vyakaran",
                        subtitle = "Godhuli & Varnika",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        accentColor = YoutubeRed,
                        onClick = { onSubjectClick("hindi") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // YouTube Banner
                val ytInteraction = remember { MutableInteractionSource() }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_youtube_banner")
                        .pressScale(interactionSource = ytInteraction)
                        .border(1.dp, YoutubeRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable(
                            interactionSource = ytInteraction,
                            indication = ripple()
                        ) {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(SubjectRepository.OFFICIAL_YOUTUBE_CHANNEL_URL)
                            )
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sk_logo),
                            contentDescription = "SK MISSION BOARD Channel Logo",
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, YoutubeRed.copy(alpha = 0.6f), RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Official SK MISSION BOARD YouTube",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Subscribe for live revision classes & board exam tips.",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null,
                            tint = YoutubeRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickResourceTile(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    GlowTargetContainer(modifier = modifier) {
        Card(
            modifier = Modifier
                .pressScale(interactionSource = interactionSource)
                .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = onClick
                ),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
