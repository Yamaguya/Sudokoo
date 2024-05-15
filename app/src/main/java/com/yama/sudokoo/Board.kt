package com.yama.sudokoo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

open class Board(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var sqrSize = 3 // Each square consists of 3 rows and 3 columns
    private var size = 9 // The entire board consists of 9 rows and 9 columns
    private var cellSizePixels = 0F

    private var selectedRow = 1
    private var selectedCol = 1

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

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

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

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
    }

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

    private fun fillCell(canvas: Canvas, row: Int, col: Int, cellPaint: Paint) {
        canvas.drawRect(col * cellSizePixels, row * cellSizePixels, (col + 1) * cellSizePixels,  (row + 1) * cellSizePixels, cellPaint)
    }

    // Paint the cells with the thinLinePaint and the squares with the thickLinePaint
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
}