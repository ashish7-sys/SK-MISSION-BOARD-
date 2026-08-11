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
                    r = 5f * density,
                    maxR = maxR,
                    color = color,
                    lineWidth = 3.5f * density,
                    speed = 8f * density,
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

            // Clip out card interiors so wave rings pass around icons instead of drawing on top of them
            val hasTargetBounds = glowTargetBounds.isNotEmpty()
            if (hasTargetBounds) {
                canvas.save()
                val clipPath = android.graphics.Path()
                for ((_, bounds) in glowTargetBounds) {
                    val cornerRadius = minOf(bounds.width(), bounds.height()) * 0.18f
                    clipPath.addRoundRect(bounds, cornerRadius, cornerRadius, android.graphics.Path.Direction.CW)
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    canvas.clipOutPath(clipPath)
                } else {
                    @Suppress("DEPRECATION")
                    canvas.clipPath(clipPath, android.graphics.Region.Op.DIFFERENCE)
                }
            }

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
                    wavePaint.setShadowLayer(20f * density, 0f, 0f, strokeColor)

                    canvas.drawCircle(w.x, w.y, w.r, wavePaint)
                } else {
                    iterator.remove()
                }
            }

            if (hasTargetBounds) {
                canvas.restore()
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

            val cardWidth = right - left
            val cardHeight = bottom - top
            val cardDiag = hypot(cardWidth.toDouble(), cardHeight.toDouble()).toFloat()

            var strongestGlow = 0f
            var strongestColor = Color.TRANSPARENT

            // Iterate waves in REVERSE order so the most recently spawned wave takes precedence!
            for (i in waves.indices.reversed()) {
                val w = waves[i]

                // Distance from wave center (w.x, w.y) to nearest edge on rectangle
                val dx = maxOf(0f, maxOf(left - w.x, w.x - right))
                val dy = maxOf(0f, maxOf(top - w.y, w.y - bottom))
                val distToNearestEdge = hypot(dx.toDouble(), dy.toDouble()).toFloat()

                // Wave progress relative to icon arrival:
                val diff = w.r - distToNearestEdge
                val pulseWidth = 32f * density

                // Only glow WHEN the expanding wave ring reaches/sweeps across the icon!
                if (diff >= -8f * density && diff <= (cardDiag + pulseWidth)) {
                    val bandRange = cardDiag + pulseWidth
                    val normPos = ((diff + 8f * density) / (bandRange + 8f * density)).coerceIn(0f, 1f)
                    val pulse = kotlin.math.sin(normPos * Math.PI.toFloat())
                    val intensity = pulse * w.alpha

                    if (intensity > strongestGlow) {
                        strongestGlow = intensity
                        strongestColor = w.color // Exact wave color!
                    }
                }
            }

            if (strongestGlow > 0.02f && strongestColor != Color.TRANSPARENT) {
                val alpha = (minOf(1f, strongestGlow) * 235f).toInt().coerceIn(0, 255)
                val glowColor = Color.argb(
                    alpha,
                    Color.red(strongestColor),
                    Color.green(strongestColor),
                    Color.blue(strongestColor)
                )

                glowPaint.color = glowColor
                // Edge glow stroke thickness (reduced by 1 point to 2.5dp)
                glowPaint.strokeWidth = 2.5f * density
                // Increased glow radius for stronger neon illumination
                glowPaint.setShadowLayer(
                    (20f + minOf(1f, strongestGlow) * 25f) * density,
                    0f,
                    0f,
                    glowColor
                )

                val cornerRadius = minOf(cardWidth, cardHeight) * 0.18f

                canvas.drawRoundRect(
                    left - 1.5f * density,
                    top - 1.5f * density,
                    right + 1.5f * density,
                    bottom + 1.5f * density,
                    cornerRadius,
                    cornerRadius,
                    glowPaint
                )
            }
        }
    }
}
