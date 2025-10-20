package com.announcement.comprehension.draw


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.floor


class DrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmap: Bitmap? = null
    private var grayScaleBitmap: Bitmap? = null
    private var colorMap: Array<IntArray>? = null
    private var filledMap: Array<BooleanArray>? = null
    private var currentColorIndex = 0
    private var colors: List<Int> = emptyList()
    private var pixelSize = 10
    private var startX = -1f
    private var startY = -1f
    private val path = Path()
    private var onDrawingCompleteListener: OnDrawingCompleteListener? = null
    private var isDrawingComplete = false

    // 定义绘制完成的回调接口
    interface OnDrawingCompleteListener {
        fun onDrawingComplete()
    }

    fun setOnDrawingCompleteListener(listener: OnDrawingCompleteListener) {
        this.onDrawingCompleteListener = listener
    }

    init {
        paint.style = Paint.Style.FILL
        bitmapPaint.style = Paint.Style.FILL
    }

    fun setImageAndColors(originalBitmap: Bitmap, grayScaleBmp: Bitmap, colorList: List<Int>, colorMapping: Array<IntArray>, pixelSize: Int) {
        this.bitmap = originalBitmap
        this.grayScaleBitmap = grayScaleBmp
        this.colors = colorList
        this.colorMap = colorMapping
        this.pixelSize = pixelSize

        // 初始化填充状态数组
        filledMap = Array(colorMapping.size) { BooleanArray(colorMapping[0].size) }

        // 不需要直接设置宽高，由onMeasure自动处理自适应
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (colorMap == null) {
            // 如果没有颜色映射数据，使用默认尺寸
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
            return
        }

        // 获取父容器允许的宽度
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentWidthMode = MeasureSpec.getMode(widthMeasureSpec)

        // 计算原始宽高比
        val originalWidth = colorMap!![0].size * pixelSize
        val originalHeight = colorMap!!.size * pixelSize
        val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

        // 根据父容器宽度计算新高度，保持宽高比
        var newWidth = parentWidth
        var newHeight = (parentWidth / aspectRatio).toInt()

        // 如果父容器宽度是精确尺寸（EXACTLY），则使用该宽度
        // 如果是AT_MOST或UNSPECIFIED，确保不会超过原始宽度
        if (parentWidthMode != MeasureSpec.EXACTLY) {
            newWidth = minOf(parentWidth, originalWidth)
            newHeight = minOf((newWidth / aspectRatio).toInt(), originalHeight)
        }

        // 设置测量尺寸
        setMeasuredDimension(newWidth, newHeight)
    }

    fun setCurrentColorIndex(index: Int) {
        if (index in colors.indices) {
            currentColorIndex = index
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (grayScaleBitmap == null || colorMap == null || filledMap == null) {
            return
        }

        // 绘制灰阶马赛克图
        canvas.drawBitmap(grayScaleBitmap!!, null, Rect(0, 0, width, height), bitmapPaint)

        // 计算适应缩放后的像素大小
        val scaledPixelSizeX = width.toFloat() / colorMap!![0].size
        val scaledPixelSizeY = height.toFloat() / colorMap!!.size

        // 绘制已填充的色块
        for (i in colorMap!!.indices) {
            for (j in colorMap!![i].indices) {
                if (filledMap!![i][j]) {
                    val colorIndex = colorMap!![i][j] - 1 // 减1因为颜色编号从1开始
                    if (colorIndex in colors.indices) {
                        paint.color = colors[colorIndex]
                        canvas.drawRect(
                            j * scaledPixelSizeX,
                            i * scaledPixelSizeY,
                            (j + 1) * scaledPixelSizeX,
                            (i + 1) * scaledPixelSizeY,
                            paint
                        )
                    }
                }
            }
        }

        // 在灰阶方块上绘制编号
        for (i in colorMap!!.indices) {
            for (j in colorMap!![i].indices) {
                if (!filledMap!![i][j]) {
                    paint.color = Color.GREEN
                    paint.textSize = scaledPixelSizeX * 0.6f
                    paint.textAlign = Paint.Align.CENTER

                    val text = colorMap!![i][j].toString()
                    val x = j * scaledPixelSizeX + scaledPixelSizeX / 2f
                    val y = i * scaledPixelSizeY + scaledPixelSizeY / 2f - (paint.descent() + paint.ascent()) / 2

                    canvas.drawText(text, x, y, paint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
                fillPixelAt(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                fillPixelBetween(startX, startY, x, y)
                startX = x
                startY = y
            }
            MotionEvent.ACTION_UP -> {
                fillPixelBetween(startX, startY, x, y)
                startX = -1f
                startY = -1f
            }
        }
        return true
    }

    // 优化版：内部使用，不触发重绘
    private fun fillPixelAtInternal(x: Float, y: Float): Boolean {
        if (colorMap == null || filledMap == null || width == 0 || height == 0) return false

        // 根据视图的实际尺寸计算行列索引
        val col = floor(x * colorMap!![0].size / width).toInt()
        val row = floor(y * colorMap!!.size / height).toInt()

        if (row in colorMap!!.indices && col in colorMap!![0].indices) {
            if (colorMap!![row][col] - 1 == currentColorIndex && !filledMap!![row][col]) {
                filledMap!![row][col] = true
                return true
            }
        }
        return false
    }

    // 公开版本，用于单点触摸
    private fun fillPixelAt(x: Float, y: Float) {
        if (fillPixelAtInternal(x, y)) {
            invalidate()
            checkIfDrawingComplete()
        }
    }

    private fun fillPixelBetween(x1: Float, y1: Float, x2: Float, y2: Float) {
        val dx = kotlin.math.abs(x2 - x1).toFloat()
        val dy = kotlin.math.abs(y2 - y1).toFloat()

        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1

        var err: Float = dx - dy
        var e2: Float

        var currentX = x1.toInt()
        var currentY = y1.toInt()

        var hasChanged = false
        var iterations = 0
        val maxIterations = (dx + dy) * 2 // 安全保障，防止无限循环

        while (iterations < maxIterations) {
            if (fillPixelAtInternal(currentX.toFloat(), currentY.toFloat())) {
                hasChanged = true
            }

            // 检查是否到达目标点或已经非常接近
            val reachedTarget = currentX == x2.toInt() && currentY == y2.toInt()
            val veryClose = kotlin.math.abs(currentX - x2.toInt()) <= 1 && kotlin.math.abs(currentY - y2.toInt()) <= 1
            if (reachedTarget || veryClose) break

            e2 = 2.0f * err
            if (e2 > -dy) {
                err -= dy
                currentX += sx
            }
            if (e2 < dx) {
                err += dx
                currentY += sy
            }

            iterations++
        }

        // 只在有变化时才触发一次重绘
        if (hasChanged) {
            invalidate()
            checkIfDrawingComplete()
        }
    }

    fun resetDrawing() {
        if (filledMap != null) {
            for (i in filledMap!!.indices) {
                for (j in filledMap!![i].indices) {
                    filledMap!![i][j] = false
                }
            }
            isDrawingComplete = false
            invalidate()
        }
    }

    // 检查是否所有方格都被填涂完成
    private fun checkIfDrawingComplete() {
        if (isDrawingComplete || filledMap == null) return

        var allFilled = true
        for (i in filledMap!!.indices) {
            for (j in filledMap!![i].indices) {
                if (!filledMap!![i][j]) {
                    allFilled = false
                    break
                }
            }
            if (!allFilled) break
        }

        if (allFilled) {
            isDrawingComplete = true
            onDrawingCompleteListener?.onDrawingComplete()
        }
    }

}