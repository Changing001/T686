package com.announcement.comprehension.draw


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max

class ColorPalette(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var colors: List<Int> = emptyList()
    private var selectedIndex = -1
    private var onColorSelectedListener: OnColorSelectedListener? = null
    private val swatchSize = 36 // 稍微减小方块大小以适应更多颜色
    private val swatchMargin = 8  // 稍微减小间距
    private val textSize = 18f    // 稍微减小文字大小

    init {
        textPaint.color = Color.BLACK
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.CENTER
    }

    fun setColors(colorList: List<Int>) {
        this.colors = colorList
        selectedIndex = if (colorList.isNotEmpty()) 0 else -1
        requestLayout()
        invalidate()
    }

    fun setOnColorSelectedListener(listener: OnColorSelectedListener) {
        this.onColorSelectedListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 计算需要的宽度，但不限制视图的最大宽度
        val requiredWidth = colors.size * (swatchSize + swatchMargin) - swatchMargin + paddingLeft + paddingRight
        val requiredHeight = swatchSize + paddingTop + paddingBottom

        // 确保视图至少有一个合理的最小宽度
        val measuredWidth = max(requiredWidth, 200)

        setMeasuredDimension(measuredWidth, requiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in colors.indices) {
            val x = paddingLeft + i * (swatchSize + swatchMargin)
            val y = paddingTop

            // 绘制颜色方块
            paint.color = colors[i]
            canvas.drawRect(
                x.toFloat(),
                y.toFloat(),
                (x + swatchSize).toFloat(),
                (y + swatchSize).toFloat(),
                paint
            )

            // 如果是选中的颜色，绘制边框
            if (i == selectedIndex) {
                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 3f
                canvas.drawRect(
                    x.toFloat() + 2,
                    y.toFloat() + 2,
                    (x + swatchSize).toFloat() - 2,
                    (y + swatchSize).toFloat() - 2,
                    paint
                )
                paint.style = Paint.Style.FILL
            }

            // 绘制颜色编号
            val text = (i + 1).toString()
            // 根据背景颜色选择文字颜色
            textPaint.color = if (isDarkColor(colors[i])) Color.WHITE else Color.BLACK
            val textX = x + swatchSize / 2f
            val textY = y + swatchSize / 2f - (textPaint.descent() + textPaint.ascent()) / 2
            canvas.drawText(text, textX, textY, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                val index = getColorIndexAtPosition(x, y)
                if (index != -1) {
                    selectedIndex = index
                    onColorSelectedListener?.onColorSelected(index)
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getColorIndexAtPosition(x: Float, y: Float): Int {
        if (y < paddingTop || y > paddingTop + swatchSize) {
            return -1
        }

        for (i in colors.indices) {
            val swatchX = paddingLeft + i * (swatchSize + swatchMargin)
            if (x >= swatchX && x <= swatchX + swatchSize) {
                return i
            }
        }
        return -1
    }

    // 判断颜色是否为深色，用于选择合适的文字颜色
    private fun isDarkColor(color: Int): Boolean {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        // 计算亮度 (0-255)
        val luminance = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
        return luminance < 128
    }

    interface OnColorSelectedListener {
        fun onColorSelected(index: Int)
    }
}