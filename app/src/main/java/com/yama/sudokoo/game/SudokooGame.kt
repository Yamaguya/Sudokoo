package com.yama.sudokoo.game

import androidx.lifecycle.MutableLiveData

class SudokooGame {

    // LiveData notifies active Observers about updates to the selected cell, ensuring that UI
    // matches the data state and preventing memory leaks since Observers are bound to Lifecycle
    // objects and clean up after themselves when their associated lifecycle is destroyed
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    val cellsLiveData = MutableLiveData<List<Cell>>()

    private var selectedRow = -1
    private var selectedCol = -1

    private val board: Board

    // Right when we create the object we post it so the SudokooBoardView
    // knows what the selected row and column is.
    // Initialize the selected cell with an invalid position (-1, -1)
    init {
        // Initialize all cells with value 0
        val cells = MutableList(9 * 9) { i -> Cell(i / 9, i % 9, 0 )}

        board = Board(9, cells)

        solveSudoku(board, 0, 0)

        cellsLiveData.postValue(cells)

        // Temporary starting cells for testing purposes
        cells[15].isStartingCell = true
        cells[24].isStartingCell = true
        cells[39].isStartingCell = true
        cells[72].isStartingCell = true

        //selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
    }

    private fun solveSudoku(board: Board, row: Int, col: Int): Boolean {
        if (row == board.size) return true // Entire board has been filled

        var nextRow = row
        var nextCol = col
        if (col == board.size - 1) {
            nextRow++
            nextCol = 0
        } else {
            nextCol++
        }

        if (board.getCell(row, col).value != 0) {
            return solveSudoku(board, nextRow, nextCol)
        }

        for (num in 1..9) {
            if (isValidNumber(board, row, col, num)) {
                board.getCell(row, col).value = num
                if (solveSudoku(board, nextRow, nextCol)) {
                    return true
                }
                board.getCell(row, col).value = 0 // Backtrack
            }
        }

        return false // No valid number found
    }

    private fun isValidNumber(board: Board, row: Int, col: Int, number: Int): Boolean {
        // Check if the number already exists in the same row, column, or square
        for (i in 0 until board.size) {
            if (board.getCell(row, i).value == number ||
                board.getCell(i, col).value == number ||
                board.getCell((row / 3) * 3 + i / 3, (col / 3) * 3 + i % 3).value == number) {
                return false
            }
        }
        return true
    }

    /*
    private fun isValidNumber(cell: Cell, number: Int): Boolean {
        // Check if the number already exists in the same row
        for (col in 0 until 9) {
            if (col != cell.col && board.getCell(cell.row, col).value == number) {
                return false
            }
        }

        // Check if the number already exists in the same column
        for (row in 0 until 9) {
            if (row != cell.row && board.getCell(row, cell.col).value == number) {
                return false
            }
        }

        // Check if the number already exists in the same 3x3 square
        val startRow = (cell.row / 3) * 3
        val startCol = (cell.col / 3) * 3
        for (i in startRow until startRow + 3) {
            for (j in startCol until startCol + 3) {
                if ((i != cell.row || j != cell.col) && board.getCell(i, j).value == number) {
                    return false
                }
            }
        }

        return true
    }
    */

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        if (board.getCell(selectedRow, selectedCol).isStartingCell) return

        board.getCell(selectedRow, selectedCol).value = number
        cellsLiveData.postValue(board.cells)
    }

    // Update the selected cell and notify observers
    fun updateSelectedCell(row: Int, col: Int) {
        if (!board.getCell(row, col).isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        }
    }

}