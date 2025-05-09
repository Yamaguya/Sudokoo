package com.yama.sudokoo.game

import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.MutableLiveData
import com.yama.sudokoo.common.DEBUG


class SudokooGame {

    // LiveData notifies active Observers about updates to the selected cell, ensuring that UI
    // matches the data state and preventing memory leaks since Observers are bound to Lifecycle
    // objects and clean up after themselves when their associated lifecycle is destroyed
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    val cellsLiveData = MutableLiveData<List<Cell>>()

    private val horizontal_cells = 9
    private val vertical_cells = 9

    private val board: Board

    private var selectedRow = -1
    private var selectedCol = -1
    private val NUM_OF_STARTING_CELLS = 7 // Add difficulty settings, Easy = 9 starting numbers, Medium = 6, Hard = 4
    private var remaining_cells = (horizontal_cells * vertical_cells)  - NUM_OF_STARTING_CELLS

    // Right when we create the object we post it so the SudokooBoardView
    // knows what the selected row and column is.
    // Initialize the selected cell with an invalid position (-1, -1)
    init {

        // Initialize all cells with value 0
        val cells = MutableList(9 * 9) { i -> Cell(i / 9, i % 9, 0, 0)}

        board = Board(9, cells)

        solveSudoku(board, 0, 0)

        cellsLiveData.postValue(cells)

        setStartingCells(cells)
        //selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
    }

    private fun setStartingCells(cells: MutableList<Cell>) {
        for (i in 0..NUM_OF_STARTING_CELLS) {
            var x = (0 until 81).random()
            while (cells[x].isStartingCell) { // Ensure cells[x] wasn't already a starting cell
                x = (0 until 81).random()
            }
            cells[x].isStartingCell = true
        }
    }

    private fun solveSudoku(board: Board, row: Int, col: Int): Boolean {
        if (row == board.size) return true // Entire board has been filled

        var nextRow = row
        var nextCol = col

        // Parse each column first, then move onto the next row
        if (col == board.size - 1) {
            nextRow++
            nextCol = 0
        } else {
            nextCol++
        }

        if (board.getCell(row, col).true_value != 0) {
            return solveSudoku(board, nextRow, nextCol)
        }

        val numbers = (1..9).shuffled()

        for (num in numbers) {
            if (isValidNumber(board, row, col, num)) {
                board.getCell(row, col).true_value = num
                if (solveSudoku(board, nextRow, nextCol)) {
                    return true
                }
                board.getCell(row, col).true_value = 0 // Backtrack
            }
        }

        return false // No valid number found
    }

    // Check if the number already exists in the column
    private fun isInRow(board: Board, row: Int, number: Int): Boolean {
        for (i in 0 until board.size) {
            if(board.getCell(row, i).true_value == number) {
                return true
            }
        }
        return false
    }

    // Check if the number already exists in the row
    private fun isInColumn(board: Board, col: Int, number: Int): Boolean {
        for (i in 0 until board.size) {
            if(board.getCell(i, col).true_value == number) {
                return true
            }
        }
        return false
    }

    // Check if number already exists in the 3x3 box
    private fun isInBox(board: Board, row: Int, col: Int, number: Int): Boolean {
        val localRow = row - row % 3
        val localCol = col - col % 3

        for (i in localRow until localRow+3) {
            for (j in localCol until localCol+3) {
                if (board.getCell(i, j).true_value == number) {
                    return true
                }
            }
        }
        return false
    }

    // Check if the number already exists in the same row, column, or square
    private fun isValidNumber(board: Board, row: Int, col: Int, number: Int): Boolean {
        return !(isInBox(board, row, col, number) || isInRow(board, row, number) || isInColumn(board, col, number))
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        if (board.getCell(selectedRow, selectedCol).isStartingCell) return
        if (board.getCell(selectedRow, selectedCol).current_value != number) {
            if (number == -1) {
                clearCell(selectedRow, selectedCol)
            }
            else
            {
                fillCell(selectedRow, selectedCol, number)
            }
        }
        cellsLiveData.postValue(board.cells)
    }

    // Clear the selected cell
    private fun clearCell(row: Int, col: Int) {
        if (!board.getCell(row, col).isStartingCell) {
            board.getCell(row, col).current_value = 0
            remaining_cells += 1
            selectedCellLiveData.postValue(Pair(row, col))
        }
    }

    private fun fillCell(selectedRow: Int, selectedCol: Int, number: Int) {
        board.getCell(selectedRow, selectedCol).updateCellTextPaint(board.getCell(selectedRow, selectedCol), Color.BLACK, 42F, Paint.Style.FILL_AND_STROKE)
        board.getCell(selectedRow, selectedCol).current_value = number

        if (DEBUG) {
            if (!checkCell(selectedRow, selectedCol)) {
                board.getCell(selectedRow, selectedCol).updateCellTextPaint(board.getCell(selectedRow, selectedCol), Color.RED, 42F, Paint.Style.FILL_AND_STROKE)
            }
            else
            {
                board.getCell(selectedRow, selectedCol).updateCellTextPaint(board.getCell(selectedRow, selectedCol), Color.BLUE, 42F, Paint.Style.FILL_AND_STROKE)
            }
        }

        remaining_cells -= 1

        if (remaining_cells == 0) {
            if (checkCurrentState(board)) {
                println("Game Over!")
            }
            else
            {
                println("Game not Over!")
            }
        }
    }

    // Update the selected cell and notify observers
    fun updateSelectedCell(row: Int, col: Int) {
        if (!board.getCell(row, col).isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        }
    }

    private fun checkCell(row: Int, col: Int): Boolean {
        if (board.getCell(row, col).current_value == board.getCell(row, col).true_value) {
            return true
        }
        else {
            return false
        }
    }

    private fun checkCurrentState(board: Board): Boolean {
        for (i in 0 until board.size) {
            for (j in 0 until board.size) {
                if (board.getCell(i, j).current_value != board.getCell(i, j).true_value) {
                    return false
                }
            }
        }
        return true
    }
}