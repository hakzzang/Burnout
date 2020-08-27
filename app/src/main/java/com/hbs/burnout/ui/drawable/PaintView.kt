package com.hbs.burnout.ui.drawable


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hbs.burnout.tfml.classifier.ImageClassifier
import java.util.*


/**
 * Custom view class to draw sketches using bitmaps
 */
class PaintView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    // original size of PaintView
    var WIDTH = 0
    var HEIGHT = 0
    private var mX = 0f
    private var mY = 0f
    private lateinit var mPath: Path
    private var mPaint = Paint()
    private val paths: ArrayList<PenPath> = ArrayList()
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    fun initView() {
        WIDTH = this.layoutParams.width
        HEIGHT = this.layoutParams.height
        mBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        mBitmap.let {
            mCanvas = Canvas(it)
        }
    }

    fun clear() {
        paths.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        mCanvas.drawColor(DEFAULT_BG_COLOR)
        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mPaint.maskFilter = null
            mCanvas.drawPath(fp.path, mPaint)
        }
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = PenPath(DEFAULT_COLOR, BRUSH_SIZE, mPath)
        paths.add(fp)
        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath.lineTo(mX, mY)
    }

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

    fun getmBitmap(): Bitmap? {
        return mBitmap
    }

    // scale given bitmap by a factor and return a new bitmap
    fun scaleBitmap(scaleFactor: Int, bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * scaleFactor), (bitmap.height * scaleFactor), true
        )
    }// todo: cut empty space around sketch

    // scale original bitmap down to network size (28x28)
    val normalizedBitmap: Bitmap
        get() {
            val scaleFactor: Float = ImageClassifier.DIM_IMG_SIZE_HEIGHT / mBitmap.height
                .toFloat()
            // todo: cut empty space around sketch
            return Bitmap.createScaledBitmap(
                mBitmap,
                (mBitmap.width * scaleFactor).toInt(),
                (mBitmap.height * scaleFactor).toInt(), true
            )
        }

    companion object {
        var BRUSH_SIZE = 50
        const val DEFAULT_COLOR = Color.WHITE
        const val DEFAULT_BG_COLOR = Color.BLACK
        private const val TOUCH_TOLERANCE = 4f
    }

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
}