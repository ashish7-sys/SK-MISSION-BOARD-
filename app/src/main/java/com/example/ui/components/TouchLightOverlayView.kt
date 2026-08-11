package com.example.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot

class TouchLightOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private class Wave(
        val x: Float,
        val y: Float,
        var r: Float,
        val maxR: Float,
        val color: Int,
        val lineWidth: Float,
        val speed: Float,
        var alpha: Float = 1.0f
    )

    private val waves = mutableListOf<Wave>()
    private var colorIndex = 0

    // 20 Bright Sequential Neon Colors (Same as reference video & JS code)
    private val colors = intArrayOf(
        Color.parseColor("#00f3ff"),
        Color.parseColor("#ffe600"),
        Color.parseColor("#00ff66"),
        Color.parseColor("#ff0055"),
        Color.parseColor("#ff00a0"),
        Color.parseColor("#9d00ff"),
        Color.parseColor("#ff6600"),
        Color.parseColor("#a6ff00"),
        Color.parseColor("#00a6ff"),
        Color.parseColor("#ffd700"),
        Color.parseColor("#e000ff"),
        Color.parseColor("#00ffd5"),
        Color.parseColor("#ff4365"),
        Color.parseColor("#00ff9f"),
        Color.parseColor("#ffaa00"),
        Color.parseColor("#3a00ff"),
        Color.parseColor("#ff003c"),
        Color.parseColor("#00e5ff"),
        Color.parseColor("#d4ff00"),
        Color.parseColor("#ff007f")
    )

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val glowTargetBounds = mutableMapOf<String, RectF>()

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        isClickable = false
        isFocusable = false
    }

    fun addWave(x: Float, y: Float) {
        val density = context.resources.displayMetrics.density
        val w = if (width > 0) width.toFloat() else 1080f
        val h = if (height > 0) height.toFloat() else 1920f
        val maxR = hypot(w.toDouble(), h.toDouble()).toFloat() * 1.2f

        val color = colors[colorIndex]
        colorIndex = (colorIndex + 1) % colors.size

        synchronized(waves) {
            waves.add(
                Wave(
                    x = x,
                    y = y,
                    r = 10f * density,
                    maxR = maxR,
                    color = color,
                    lineWidth = 12f * density,
                    speed = 9f * density,
                    alpha = 1.0f
                )
            )
        }
        postInvalidateOnAnimation()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN || event.actionMasked == MotionEvent.ACTION_POINTER_DOWN) {
            val pointerIndex = event.actionIndex
            addWave(event.getX(pointerIndex), event.getY(pointerIndex))
        }
        return false // Don't consume touch, let buttons work
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val density = context.resources.displayMetrics.density

        synchronized(waves) {
            if (waves.isEmpty()) return

            val iterator = waves.iterator()
            while (iterator.hasNext()) {
                val w = iterator.next()
                w.r += w.speed
                w.alpha = (1.0f - (w.r / w.maxR)).coerceIn(0.0f, 1.0f)

                if (w.alpha > 0.001f) {
                    val alphaInt = (w.alpha * 255).toInt().coerceIn(0, 255)
                    val strokeColor = Color.argb(
                        alphaInt,
                        Color.red(w.color),
                        Color.green(w.color),
                        Color.blue(w.color)
                    )

                    wavePaint.color = strokeColor
                    wavePaint.strokeWidth = w.lineWidth
                    wavePaint.setShadowLayer(30f * density, 0f, 0f, strokeColor)

                    canvas.drawCircle(w.x, w.y, w.r, wavePaint)
                } else {
                    iterator.remove()
                }
            }

            drawEdgeGlow(canvas)

            if (waves.isNotEmpty()) {
                postInvalidateOnAnimation()
            }
        }
    }

    fun registerGlowTargetBounds(id: String, bounds: RectF) {
        glowTargetBounds[id] = bounds
    }

    fun unregisterGlowTargetBounds(id: String) {
        glowTargetBounds.remove(id)
    }

    private fun drawEdgeGlow(canvas: Canvas) {
        if (glowTargetBounds.isEmpty()) return

        val density = context.resources.displayMetrics.density

        for ((_, bounds) in glowTargetBounds) {
            val left = bounds.left
            val top = bounds.top
            val right = bounds.right
            val bottom = bounds.bottom

            val centerX = (left + right) / 2f
            val centerY = (top + bottom) / 2f

            var strongestGlow = 0f
            var strongestColor = Color.TRANSPARENT

            for (w in waves) {
                val distance = hypot((centerX - w.x).toDouble(), (centerY - w.y).toDouble()).toFloat()
                val difference = kotlin.math.abs(distance - w.r)
                val glowWidth = 120f * density

                if (difference < glowWidth) {
                    val intensity = (1f - (difference / glowWidth)) * w.alpha
                    if (intensity > strongestGlow) {
                        strongestGlow = intensity
                        strongestColor = w.color
                    }
                }
            }

            if (strongestGlow > 0.01f) {
                val alpha = (strongestGlow * 255f).toInt().coerceIn(0, 255)
                val glowColor = Color.argb(
                    alpha,
                    Color.red(strongestColor),
                    Color.green(strongestColor),
                    Color.blue(strongestColor)
                )

                glowPaint.color = glowColor
                glowPaint.strokeWidth = (3f + strongestGlow * 6f) * density
                glowPaint.setShadowLayer(
                    (20f + strongestGlow * 30f) * density,
                    0f,
                    0f,
                    glowColor
                )

                val cornerRadius = minOf(right - left, bottom - top) * 0.18f

                canvas.drawRoundRect(
                    left - 4f * density,
                    top - 4f * density,
                    right + 4f * density,
                    bottom + 4f * density,
                    cornerRadius,
                    cornerRadius,
                    glowPaint
                )
            }
        }
    }
}
