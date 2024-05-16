package com.yama.sudokoo.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

open class BoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var sqrSize = 3 // Each square consists of 3 rows and 3 columns
    private var size = 9    // The entire board consists of 9 rows and 9 columns
    private var cellSizePixels = 0F

    private var selectedRow = 0
    private var selectedCol = 0

    private var listener: OnTouchListener? = null

    // Thick line for the square borders
    private val thickLinePaint = Paint().apply {
        style =  Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    // Thin line for the cell borders
    private val thinLinePaint = Paint().apply {
        style =  Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    // Paint for the selected cell background
    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

    // Paint for conflicting cells (same row, column, or square)
    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
    }

    // Maintain board aspect ratio
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    // Draw the board
    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
    }

    // Fill the selected and conflicting cells
    private fun fillCells(canvas: Canvas) {

        if (selectedRow == -1 || selectedCol == -1) return

        for (r in 0..size) {
            for (c in 0..size) {
                if (r == selectedRow && c == selectedCol) {
                    fillCell(canvas, r, c, selectedCellPaint)
                }
                else if (r == selectedRow || c == selectedCol)
                {
                    fillCell(canvas, r, c, conflictingCellPaint)
                }
                else if (r / sqrSize == selectedRow / sqrSize && c / sqrSize == selectedCol / sqrSize)
                {
                    fillCell(canvas, r, c, conflictingCellPaint)
                }
            }
        }
    }

    // Fill a specific cell with the given paint
    private fun fillCell(canvas: Canvas, row: Int, col: Int, cellPaint: Paint) {
        canvas.drawRect(col * cellSizePixels, row * cellSizePixels, (col + 1) * cellSizePixels,  (row + 1) * cellSizePixels, cellPaint)
    }

    // Draw the cell and square borders
    // thinLinePaint for the cells and thickLinePaint for the squares
    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size) {
            val paintToUse = when (i % sqrSize) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }
            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                height.toFloat(),
                paintToUse
            )
            canvas.drawLine(
                0F,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                paintToUse
            )
        }
    }

    // Handle touch events to select cells
    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        Log.d("BoardView", "Touch event at row $possibleSelectedRow, col $possibleSelectedCol")
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    // Override onTouchEvent to handle touch events
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    // Update the selected cell and redraw the board
    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    // Register the touch listener
    fun registerListener(listener: BoardView.OnTouchListener) {
        this.listener = listener
    }

    // Interface for handling cell touch events
    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}