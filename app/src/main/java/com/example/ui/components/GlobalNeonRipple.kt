package com.example.ui.components

import android.graphics.RectF
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.viewinterop.AndroidView
import java.util.UUID

val LocalTouchLightOverlayView = staticCompositionLocalOf<TouchLightOverlayView?> { null }

@Composable
fun GlobalNeonRippleOverlay(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var overlayViewRef by remember { mutableStateOf<TouchLightOverlayView?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        for (change in event.changes) {
                            if (change.pressed && !change.previousPressed) {
                                overlayViewRef?.addWave(change.position.x, change.position.y)
                            }
                        }
                    }
                }
            }
    ) {
        // 1. TouchLightOverlayView rendered BEHIND the UI
        AndroidView(
            factory = { context ->
                TouchLightOverlayView(context).also { view ->
                    overlayViewRef = view
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Foreground Compose UI
        CompositionLocalProvider(LocalTouchLightOverlayView provides overlayViewRef) {
            content()
        }
    }
}

@Composable
fun GlowTargetContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val overlayView = LocalTouchLightOverlayView.current
    val id = remember { UUID.randomUUID().toString() }
    var currentBounds by remember { mutableStateOf<RectF?>(null) }

    LaunchedEffect(overlayView, currentBounds) {
        val view = overlayView
        val bounds = currentBounds
        if (view != null && bounds != null) {
            view.registerGlowTargetBounds(id, bounds)
        }
    }

    DisposableEffect(id, overlayView) {
        onDispose {
            overlayView?.unregisterGlowTargetBounds(id)
        }
    }

    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            val bounds = coordinates.boundsInWindow()
            currentBounds = RectF(bounds.left, bounds.top, bounds.right, bounds.bottom)
        }
    ) {
        content()
    }
}
