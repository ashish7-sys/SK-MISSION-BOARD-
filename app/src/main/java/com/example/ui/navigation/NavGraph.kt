package com.example.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*

object Routes {
    const val HOME = "home"
    const val SUBJECT = "subject/{subjectId}"
    const val CHAPTER = "chapter/{chapterId}"
    const val SEARCH = "search"
    const val PDF_VIEWER = "pdf_viewer/{pdfId}"
    const val VIDEO_PLAYER = "video_player/{videoId}"

    fun buildSubjectRoute(subjectId: String) = "subject/$subjectId"
    fun buildChapterRoute(chapterId: String) = "chapter/$chapterId"
    fun buildPdfViewerRoute(pdfId: String) = "pdf_viewer/$pdfId"
    fun buildVideoPlayerRoute(videoId: String) = "video_player/$videoId"
}

@Composable
fun SKMissionBoardNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn(animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onSubjectClick = { subjectId ->
                    navController.navigate(Routes.buildSubjectRoute(subjectId))
                },
                onSearchClick = {
                    navController.navigate(Routes.SEARCH)
                },
                onPdfClick = { pdfId ->
                    navController.navigate(Routes.buildPdfViewerRoute(pdfId))
                }
            )
        }

        composable(
            route = Routes.SUBJECT,
            arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
            SubjectScreen(
                subjectId = subjectId,
                onBackClick = { navController.popBackStack() },
                onChapterClick = { chapterId ->
                    navController.navigate(Routes.buildChapterRoute(chapterId))
                }
            )
        }

        composable(
            route = Routes.CHAPTER,
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
            ChapterScreen(
                chapterId = chapterId,
                onBackClick = { navController.popBackStack() },
                onViewPdf = { pdf ->
                    navController.navigate(Routes.buildPdfViewerRoute(pdf.id))
                },
                onWatchVideo = { video ->
                    navController.navigate(Routes.buildVideoPlayerRoute(video.id))
                }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToSubject = { subjectId ->
                    navController.navigate(Routes.buildSubjectRoute(subjectId))
                },
                onNavigateToChapter = { chapterId ->
                    navController.navigate(Routes.buildChapterRoute(chapterId))
                }
            )
        }

        composable(
            route = Routes.PDF_VIEWER,
            arguments = listOf(navArgument("pdfId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pdfId = backStackEntry.arguments?.getString("pdfId") ?: ""
            PdfViewerScreen(
                pdfId = pdfId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.VIDEO_PLAYER,
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
            VideoPlayerScreen(
                videoId = videoId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
