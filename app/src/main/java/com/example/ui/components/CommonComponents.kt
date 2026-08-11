package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SubjectRepository
import com.example.model.Chapter
import com.example.model.PdfResource
import com.example.model.Subject
import com.example.model.VideoResource
import com.example.ui.theme.*

/**
 * Premium tactile scale animation modifier when an element is pressed.
 */
@Composable
fun Modifier.pressScale(
    scaleDown: Float = 0.97f,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
): Modifier {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pressScaleAnimation"
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

@Composable
fun SKTopBar(
    title: String = "SK MISSION BOARD",
    subtitle: String = "by SK ACADEMY • Class 10",
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    onSearchClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = DarkBackground,
        tonalElevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (showBack) {
                        val backInteraction = remember { MutableInteractionSource() }
                        IconButton(
                            onClick = onBackClick,
                            interactionSource = backInteraction,
                            modifier = Modifier
                                .testTag("top_bar_back_button")
                                .padding(end = 10.dp)
                                .size(40.dp)
                                .pressScale(interactionSource = backInteraction)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .border(1.dp, DarkBorder, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        // Official SK MISSION BOARD Logo Image
                        Box(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceVariant)
                                .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sk_logo),
                                contentDescription = "SK MISSION BOARD Logo",
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Column {
                        Text(
                            text = title,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = subtitle,
                            color = CyanGlow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Class 10 Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = DarkSurfaceVariant,
                        border = BorderStroke(1.dp, PurpleGlow.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Class 10",
                            color = GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (onSearchClick != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        val searchInteraction = remember { MutableInteractionSource() }
                        IconButton(
                            onClick = onSearchClick,
                            interactionSource = searchInteraction,
                            modifier = Modifier
                                .testTag("top_bar_search_button")
                                .size(40.dp)
                                .pressScale(interactionSource = searchInteraction)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .border(1.dp, DarkBorder, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = DarkBorder.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    glowColor: Color = PurpleGlow,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    GlowTargetContainer(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .pressScale(interactionSource = interactionSource)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(glowColor.copy(alpha = 0.5f), DarkBorder)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = ripple(),
                            onClick = onClick
                        )
                    } else Modifier
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

@Composable
fun SubjectCard(
    subject: Subject,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val gradient = when (subject.id) {
        "science" -> Brush.horizontalGradient(listOf(ScienceGradientStart, ScienceGradientEnd))
        "mathematics" -> Brush.horizontalGradient(listOf(MathGradientStart, MathGradientEnd))
        "social_science" -> Brush.horizontalGradient(listOf(SstGradientStart, SstGradientEnd))
        "hindi" -> Brush.horizontalGradient(listOf(HindiGradientStart, HindiGradientEnd))
        "english" -> Brush.horizontalGradient(listOf(EnglishGradientStart, EnglishGradientEnd))
        "sanskrit" -> Brush.horizontalGradient(listOf(SanskritGradientStart, SanskritGradientEnd))
        else -> Brush.horizontalGradient(listOf(SanskritGradientStart, SanskritGradientEnd))
    }

    val icon: ImageVector = when (subject.id) {
        "science" -> Icons.Default.Science
        "mathematics" -> Icons.Default.Calculate
        "social_science" -> Icons.Default.Public
        "hindi" -> Icons.AutoMirrored.Filled.MenuBook
        "sanskrit" -> Icons.Default.AutoStories
        "english" -> Icons.Default.Translate
        else -> Icons.Default.AutoStories
    }

    GlowTargetContainer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("subject_card_${subject.id}")
                .pressScale(interactionSource = interactionSource)
                .border(
                    width = 1.dp,
                    color = Color(subject.colorHex).copy(alpha = 0.35f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = onClick
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = subject.name,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = subject.name,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${subject.nameHindi})",
                            color = Color(subject.accentHex),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subject.description,
                        color = TextMuted,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkSurfaceVariant
                    ) {
                        Text(
                            text = "${subject.totalChaptersCount} Chapters Complete Syllabus",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = Color(subject.colorHex),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun ChapterCard(
    chapter: Chapter,
    pdfCount: Int,
    videoCount: Int,
    mcqCount: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    GlowTargetContainer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("chapter_card_${chapter.id}")
                .pressScale(interactionSource = interactionSource)
                .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = onClick
                ),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chapter Number Badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, PurpleGlow.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "CH",
                            color = PurpleGlowVariant,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${chapter.number}",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.title,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    val titleHindi = chapter.titleHindi
                    if (!titleHindi.isNullOrEmpty()) {
                        Text(
                            text = titleHindi,
                            color = CyanGlow,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Resource Badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (pdfCount > 0) {
                            ResourceChip(text = "$pdfCount PDFs", icon = Icons.Default.PictureAsPdf, color = CyanGlow)
                        }
                        if (videoCount > 0) {
                            ResourceChip(text = "$videoCount Videos", icon = Icons.Default.PlayCircle, color = YoutubeRed)
                        }
                        if (mcqCount > 0) {
                            ResourceChip(text = "$mcqCount MCQs", icon = Icons.Default.Quiz, color = SuccessGreen)
                        }
                        if (pdfCount == 0 && videoCount == 0 && mcqCount == 0) {
                            Text(
                                text = "Content updating",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open Chapter",
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun String?.isNull_or_Empty(): Boolean = this == null || this.trim().isEmpty()

@Composable
fun ResourceChip(text: String, icon: ImageVector, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = text,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PdfItemCard(
    pdf: PdfResource,
    onViewClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .testTag("pdf_item_${pdf.id}")
            .pressScale(interactionSource = interactionSource)
            .border(1.dp, CyanGlow.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyanGlow.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "PDF Document",
                        tint = CyanGlow,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(pdf.category.badgeColorHex).copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = pdf.category.displayName,
                            color = Color(pdf.category.badgeColorHex),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = pdf.title,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    if (!pdf.description.isNull_or_Empty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = pdf.description!!,
                            color = TextMuted,
                            fontSize = 12.sp,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "• ${pdf.fileSize}", color = TextSecondary, fontSize = 11.sp)
                        Text(text = "• ${pdf.pageCount} Pages", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View Button
                val viewBtnInteraction = remember { MutableInteractionSource() }
                Button(
                    onClick = onViewClick,
                    interactionSource = viewBtnInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .pressScale(interactionSource = viewBtnInteraction)
                        .testTag("pdf_view_button_${pdf.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = DarkBackground,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View PDF", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                // Download Button
                val downloadBtnInteraction = remember { MutableInteractionSource() }
                OutlinedButton(
                    onClick = {
                        onDownloadClick()
                        Toast.makeText(context, "Downloading ${pdf.title}...", Toast.LENGTH_SHORT).show()
                    },
                    interactionSource = downloadBtnInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .pressScale(interactionSource = downloadBtnInteraction)
                        .testTag("pdf_download_button_${pdf.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = BorderStroke(1.dp, CyanGlow),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = CyanGlow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun VideoItemCard(
    video: VideoResource,
    onWatchClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .testTag("video_item_${video.id}")
            .pressScale(interactionSource = interactionSource)
            .border(1.dp, YoutubeRed.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Thumbnail Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(DarkSurfaceVariant, Color(0xFF2A0F15))
                        )
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        onClick = onWatchClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(YoutubeRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "SK MISSION BOARD OFFICIAL LECTURE",
                        color = GoldAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Duration badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = video.duration,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = video.title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            val watchBtnInteraction = remember { MutableInteractionSource() }
            Button(
                onClick = onWatchClick,
                interactionSource = watchBtnInteraction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .pressScale(interactionSource = watchBtnInteraction)
                    .testTag("watch_video_button_${video.id}"),
                colors = ButtonDefaults.buttonColors(containerColor = YoutubeRed),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Watch Lecture Now", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VideoFallbackSection(
    hasAvailableVideos: Boolean
) {
    val context = LocalContext.current
    val btnInteraction = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("video_fallback_section")
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(PurpleGlow.copy(alpha = 0.5f), YoutubeRed.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Subscriptions,
                    contentDescription = null,
                    tint = YoutubeRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasAvailableVideos) "MORE VIDEO RESOURCES" else "VIDEO LECTURES",
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (hasAvailableVideos)
                    "Looking for additional lectures & live revision streams?"
                else
                    "Specific chapter video is not available yet. SK ACADEMY faculty will upload it soon!",
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(SubjectRepository.OFFICIAL_YOUTUBE_CHANNEL_URL)
                    )
                    context.startActivity(intent)
                },
                interactionSource = btnInteraction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .pressScale(interactionSource = btnInteraction)
                    .testTag("visit_youtube_channel_button"),
                colors = ButtonDefaults.buttonColors(containerColor = YoutubeRed),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.OndemandVideo,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Visit SK MISSION BOARD YouTube Channel",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun EmptyResourceState(
    title: String,
    description: String,
    icon: ImageVector = Icons.Default.FolderOpen
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(DarkSurfaceVariant)
                .border(1.dp, DarkBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            color = TextMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}
