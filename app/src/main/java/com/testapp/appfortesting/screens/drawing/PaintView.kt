package com.testapp.appfortesting.screens.drawing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View


@Suppress("DEPRECATION")
class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_COLOR = Color.RED
        const val DEFAULT_BG_COLOR = Color.WHITE
        const val TOUCH_TOLERANCE = 4
    }

    private val BRUSHSIZE = 10
    private var aX = 0f
    private var aY = 0f
    private var mPath: Path? = null
    private var mPaint: Paint = Paint()
    private var paths = ArrayList<FingerPath>()
    private var currentColor: Int = 0
    private var mBackgroundColor = DEFAULT_BG_COLOR
    private var strokeWidth: Int = 0
    private var emboss: Boolean = false
    private var blur: Boolean = false
    private var mEmboss: MaskFilter = EmbossMaskFilter(floatArrayOf(1f, 1f, 1f), 0.4f, 6f, 3.5f)
    private var mBlur: MaskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
    private lateinit var mBitmap: Bitmap
    private var mCanvas: Canvas? = null
    private var mBitmapPaint = Paint(Paint.DITHER_FLAG)

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = DEFAULT_COLOR
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.xfermode = null
        mPaint.alpha = 0xff
    }

    fun init(metrics: DisplayMetrics) {
        val height = metrics.heightPixels
        val width = metrics.widthPixels

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)

        currentColor = DEFAULT_COLOR
        strokeWidth = BRUSHSIZE
    }

    fun normal() {
        emboss = false
        blur = false
    }

    fun emboss() {
        emboss = true
        blur = false
    }

    fun blur() {
        emboss = false
        blur = true
    }

    fun clear() {
        mBackgroundColor = DEFAULT_BG_COLOR
        paths.clear()
        normal()
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.save()
        mCanvas?.drawColor(mBackgroundColor)

        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mPaint.maskFilter = null

            if (fp.isEmboss)
                mPaint.maskFilter = mEmboss
            else if (fp.isBlur)
                mPaint.maskFilter = mBlur

            mCanvas?.drawPath(fp.path, mPaint)

        }
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = FingerPath(currentColor, emboss, blur, strokeWidth, mPath!!)
        paths.add(fp)

        mPath?.reset()
        mPath?.moveTo(x, y)
        aX = x
        aY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - aX)
        val dy = Math.abs(y - aY)

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath?.quadTo(aX, aY, (x + aX) / 2, (y + aY) / 2)
            aX = x
            aY = y
        }
    }

    private fun touchUp() {
        mPath?.lineTo(aX, aY)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }

        return true
    }
}