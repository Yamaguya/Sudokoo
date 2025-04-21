package com.yama.sudokoo.game

import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint

class Cell(
    val row: Int,
    val col: Int,
    var true_value: Int,
    var current_value: Int,
    var isStartingCell: Boolean = false,
    var textPaint: TextPaint = TextPaint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.BLACK
            textSize = 0F
    })
{

    fun updateCellTextPaint(cell: Cell, color: Int, textSize: Float, style: Paint.Style) {
        cell.textPaint.apply {
            this.color = color
            this.textSize = textSize
            this.style = style
        }
    }
}