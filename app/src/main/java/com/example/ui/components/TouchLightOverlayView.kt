package com.example.ui.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import kotlin.math.hypot

class TouchLightOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private data class Wave(
        val x: Float,
        val y: Float,
        val color: Int,
        val startTime: Long,
        val duration: Long = 1500L
    )

    private val waves = mutableListOf<Wave>()

    private var colorIndex = 0

    private val colors = intArrayOf(
        Color.rgb(0, 168, 255),
        Color.rgb(255, 212, 0),
        Color.rgb(57, 255, 20),
        Color.rgb(255, 23, 68),
        Color.rgb(176, 38, 255),
        Color.rgb(0, 255, 255),
        Color.rgb(255, 109, 0),
        Color.rgb(255, 0, 168),
        Color.rgb(124, 77, 255),
        Color.rgb(0, 230, 118),
        Color.rgb(255, 234, 0),
        Color.rgb(0, 184, 212),
        Color.rgb(255, 64, 129),
        Color.rgb(118, 255, 3),
        Color.rgb(101, 31, 255),
        Color.rgb(255, 145, 0),
        Color.rgb(24, 255, 255),
        Color.rgb(245, 0, 87),
        Color.rgb(213, 0, 249),
        Color.rgb(100, 255, 218)
    )

    private val wavePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

    private val glowPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

    private var animator: ValueAnimator? = null

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        isClickable = false
        isFocusable = false
    }

    fun addWave(x: Float, y: Float) {

        val color = colors[colorIndex]

        colorIndex = (colorIndex + 1) % colors.size

        waves.add(
            Wave(
                x = x,
                y = y,
                color = color,
                startTime = System.currentTimeMillis()
            )
        )

        startAnimation()
    }

    private fun startAnimation() {

        if (animator?.isRunning == true) {
            invalidate()
            return
        }

        animator = ValueAnimator.ofFloat(0f, 1f).apply {

            duration = 1500L

            interpolator = LinearInterpolator()

            repeatCount = ValueAnimator.INFINITE

            addUpdateListener {

                val now = System.currentTimeMillis()

                waves.removeAll { wave ->
                    now - wave.startTime > wave.duration
                }

                invalidate()

                if (waves.isEmpty()) {
                    cancel()
                    animator = null
                }
            }

            start()
        }
    }

    override fun onTouchEvent(
        event: MotionEvent
    ): Boolean {

        if (event.actionMasked ==
            MotionEvent.ACTION_DOWN
        ) {

            addWave(
                event.x,
                event.y
            )
        }

        // Never consume the touch.
        return false
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        if (waves.isEmpty()) return

        val now = System.currentTimeMillis()

        val maxRadius = hypot(
            width.toDouble(),
            height.toDouble()
        ).toFloat() * 1.25f

        for (wave in waves) {

            val elapsed =
                now - wave.startTime

            val progress =
                (elapsed.toFloat() / wave.duration)
                    .coerceIn(0f, 1f)

            val radius =
                25f + maxRadius * progress

            val alpha = when {
                progress < 0.08f ->
                    progress / 0.08f * 220f

                progress > 0.75f ->
                    (1f - progress) / 0.25f * 150f

                else ->
                    170f
            }

            val waveColor = Color.argb(
                alpha.toInt().coerceIn(0, 255),
                Color.red(wave.color),
                Color.green(wave.color),
                Color.blue(wave.color)
            )

            wavePaint.color = waveColor
            wavePaint.strokeWidth = 3f

            wavePaint.setShadowLayer(
                25f,
                0f,
                0f,
                waveColor
            )

            /*
             * Main travelling light wave.
             */
            canvas.drawCircle(
                wave.x,
                wave.y,
                radius,
                wavePaint
            )

            /*
             * Soft outer glow.
             */
            val outerColor = Color.argb(
                (alpha * 0.25f)
                    .toInt()
                    .coerceIn(0, 255),
                Color.red(wave.color),
                Color.green(wave.color),
                Color.blue(wave.color)
            )

            wavePaint.color = outerColor
            wavePaint.strokeWidth = 12f

            canvas.drawCircle(
                wave.x,
                wave.y,
                radius,
                wavePaint
            )
        }

        drawEdgeGlow(canvas, now)
    }

    private val glowTargets =
        mutableListOf<View>()

    private val glowTargetBounds =
        mutableMapOf<String, RectF>()

    fun registerGlowTarget(view: View) {

        if (!glowTargets.contains(view)) {
            glowTargets.add(view)
        }
    }

    fun unregisterGlowTarget(view: View) {

        glowTargets.remove(view)
    }

    fun registerGlowTargetBounds(id: String, bounds: RectF) {
        glowTargetBounds[id] = bounds
    }

    fun unregisterGlowTargetBounds(id: String) {
        glowTargetBounds.remove(id)
    }

    private fun drawEdgeGlow(
        canvas: Canvas,
        now: Long
    ) {

        if (glowTargets.isEmpty() && glowTargetBounds.isEmpty()) return

        for ((_, bounds) in glowTargetBounds) {
            val left = bounds.left
            val top = bounds.top
            val right = bounds.right
            val bottom = bounds.bottom

            val centerX = (left + right) / 2f
            val centerY = (top + bottom) / 2f

            var strongestGlow = 0f
            var strongestColor = Color.TRANSPARENT

            for (wave in waves) {
                val elapsed = now - wave.startTime
                val progress = (elapsed.toFloat() / wave.duration).coerceIn(0f, 1f)
                val maxRadius = hypot(width.toDouble(), height.toDouble()).toFloat() * 1.25f
                val radius = 25f + maxRadius * progress

                val distance = hypot(centerX - wave.x, centerY - wave.y)
                val difference = kotlin.math.abs(distance - radius)
                val glowWidth = 100f

                if (difference < glowWidth) {
                    val intensity = 1f - difference / glowWidth
                    if (intensity > strongestGlow) {
                        strongestGlow = intensity
                        strongestColor = wave.color
                    }
                }
            }

            if (strongestGlow > 0.01f) {
                val alpha = (strongestGlow * 230f).toInt().coerceIn(0, 230)
                val glowColor = Color.argb(
                    alpha,
                    Color.red(strongestColor),
                    Color.green(strongestColor),
                    Color.blue(strongestColor)
                )

                glowPaint.color = glowColor
                glowPaint.strokeWidth = 2f + strongestGlow * 4f
                glowPaint.setShadowLayer(
                    18f + strongestGlow * 25f,
                    0f,
                    0f,
                    glowColor
                )

                val cornerRadius = minOf(right - left, bottom - top) * 0.18f

                canvas.drawRoundRect(
                    left - 3f,
                    top - 3f,
                    right + 3f,
                    bottom + 3f,
                    cornerRadius,
                    cornerRadius,
                    glowPaint
                )
            }
        }

        val location = IntArray(2)

        for (target in glowTargets) {

            if (!target.isShown) continue

            target.getLocationOnScreen(location)

            val left = location[0].toFloat()
            val top = location[1].toFloat()

            val right =
                left + target.width

            val bottom =
                top + target.height

            val centerX =
                (left + right) / 2f

            val centerY =
                (top + bottom) / 2f

            var strongestGlow = 0f
            var strongestColor =
                Color.TRANSPARENT

            for (wave in waves) {

                val elapsed =
                    now - wave.startTime

                val progress =
                    (elapsed.toFloat() /
                            wave.duration)
                        .coerceIn(0f, 1f)

                val maxRadius =
                    hypot(
                        width.toDouble(),
                        height.toDouble()
                    ).toFloat() * 1.25f

                val radius =
                    25f +
                    maxRadius * progress

                val distance =
                    hypot(
                        centerX - wave.x,
                        centerY - wave.y
                    )

                val difference =
                    kotlin.math.abs(
                        distance - radius
                    )

                val glowWidth = 100f

                if (difference < glowWidth) {

                    val intensity =
                        1f -
                        difference / glowWidth

                    if (intensity >
                        strongestGlow) {

                        strongestGlow =
                            intensity

                        strongestColor =
                            wave.color
                    }
                }
            }

            if (strongestGlow > 0.01f) {

                val alpha =
                    (strongestGlow * 230f)
                        .toInt()
                        .coerceIn(0, 230)

                val glowColor =
                    Color.argb(
                        alpha,
                        Color.red(
                            strongestColor
                        ),
                        Color.green(
                            strongestColor
                        ),
                        Color.blue(
                            strongestColor
                        )
                    )

                glowPaint.color =
                    glowColor

                glowPaint.strokeWidth =
                    2f +
                    strongestGlow * 4f

                glowPaint.setShadowLayer(
                    18f +
                    strongestGlow * 25f,
                    0f,
                    0f,
                    glowColor
                )

                val cornerRadius =
                    minOf(
                        target.width,
                        target.height
                    ) * 0.18f

                canvas.drawRoundRect(
                    left - 3f,
                    top - 3f,
                    right + 3f,
                    bottom + 3f,
                    cornerRadius,
                    cornerRadius,
                    glowPaint
                )
            }
        }
    }

    override fun onDetachedFromWindow() {

        animator?.cancel()

        animator = null

        waves.clear()

        glowTargets.clear()

        glowTargetBounds.clear()

        super.onDetachedFromWindow()
    }
}
