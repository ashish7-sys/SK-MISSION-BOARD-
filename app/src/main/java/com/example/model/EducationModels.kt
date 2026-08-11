package com.example.model

enum class ResourceType(val displayName: String, val badgeColorHex: Long) {
    NOTES("Study Notes", 0xFF8B5CF6),
    PDF("PDF Material", 0xFF06B6D4),
    VIDEO("Video Lecture", 0xFFEF4444),
    MCQ("MCQ Quiz", 0xFF10B981),
    IMPORTANT_QUESTIONS("Imp. Questions", 0xFFF59E0B),
    PREVIOUS_YEAR_QUESTIONS("PYQs", 0xFFEC4899),
    QUICK_REVISION("Quick Revision", 0xFF6366F1)
}

data class Subject(
    val id: String,
    val name: String,
    val nameHindi: String,
    val code: String,
    val description: String,
    val colorHex: Long,
    val accentHex: Long,
    val totalChaptersCount: Int = 0
)

data class Chapter(
    val id: String,
    val subjectId: String,
    val number: Int,
    val title: String,
    val titleHindi: String? = null,
    val description: String? = null,
    val keyTopics: List<String> = emptyList()
)

data class PdfResource(
    val id: String,
    val chapterId: String,
    val subjectId: String,
    val title: String,
    val description: String? = null,
    val pdfUrl: String,
    val fileSize: String = "1.2 MB",
    val pageCount: Int = 5,
    val category: ResourceType = ResourceType.PDF,
    val isSample: Boolean = false
)

data class VideoResource(
    val id: String,
    val chapterId: String,
    val subjectId: String,
    val title: String,
    val youtubeUrl: String,
    val youtubeVideoId: String,
    val duration: String = "25:00",
    val addedDate: String = "2026"
)

data class McqItem(
    val id: String,
    val chapterId: String,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String? = null
)

data class QuestionItem(
    val id: String,
    val chapterId: String,
    val question: String,
    val answer: String,
    val marks: Int = 2,
    val isPyq: Boolean = false,
    val examYear: String? = null
)

data class SearchResult(
    val id: String,
    val title: String,
    val subtitle: String,
    val typeName: String,
    val subjectId: String,
    val chapterId: String? = null,
    val pdfUrl: String? = null,
    val youtubeUrl: String? = null
)
