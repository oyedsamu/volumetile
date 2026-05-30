package com.oyedsamu.volumetile

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * A clean vertical slider drawn from scratch.
 * Drag up = higher progress (louder), drag down = lower.
 */
class VerticalSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    interface OnChangeListener {
        fun onProgressChanged(progress: Int, fromUser: Boolean)
        fun onStartTracking()
        fun onStopTracking()
    }

    var max: Int = 100
        set(value) { field = value; invalidate() }

    var progress: Int = 0
        set(value) { field = value.coerceIn(0, max); invalidate() }

    var listener: OnChangeListener? = null

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x55FFFFFF
        style = Paint.Style.FILL
    }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.FILL
    }
    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.FILL
    }

    private val trackRect = RectF()
    private val fillRect = RectF()
    private val density get() = context.resources.displayMetrics.density
    private val trackHalfWidth get() = 4f * density   // 4dp half-width → 8dp track
    private val thumbRadius get() = 10f * density

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val r = thumbRadius
        val top = r
        val bottom = height.toFloat() - r
        val trackH = bottom - top
        val fraction = if (max > 0) progress.toFloat() / max else 0f
        val thumbY = bottom - fraction * trackH

        // Background track
        trackRect.set(cx - trackHalfWidth, top, cx + trackHalfWidth, bottom)
        canvas.drawRoundRect(trackRect, trackHalfWidth, trackHalfWidth, trackPaint)

        // Filled portion (bottom → thumb)
        if (fraction > 0f) {
            fillRect.set(cx - trackHalfWidth, thumbY, cx + trackHalfWidth, bottom)
            canvas.drawRoundRect(fillRect, trackHalfWidth, trackHalfWidth, fillPaint)
        }

        // Thumb circle
        canvas.drawCircle(cx, thumbY, r, thumbPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                setProgressFromTouch(event.y)
                listener?.onStartTracking()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                setProgressFromTouch(event.y)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                setProgressFromTouch(event.y)
                listener?.onStopTracking()
                parent?.requestDisallowInterceptTouchEvent(false)
                return true
            }
        }
        return false
    }

    private fun setProgressFromTouch(touchY: Float) {
        val r = thumbRadius
        val bottom = height.toFloat() - r
        val top = r
        val fraction = ((bottom - touchY) / (bottom - top)).coerceIn(0f, 1f)
        val newProgress = (fraction * max).toInt()
        if (newProgress != progress) {
            progress = newProgress
            listener?.onProgressChanged(progress, fromUser = true)
        }
    }
}
