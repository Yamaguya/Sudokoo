package com.yama.sudokoo.game

import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

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

        // Parse each column first, then move onto the next row
        if (col == board.size - 1) {
            nextRow++
            nextCol = 0
        } else {
            nextCol++
        }

        if (board.getCell(row, col).value != 0) {
            return solveSudoku(board, nextRow, nextCol)
        }

        val numbers = (1..9).shuffled()

        for (num in numbers) {
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

    // Check if the number already exists in the column
    private fun isInRow(board: Board, row: Int, number: Int): Boolean {
        for (i in 0 until board.size) {
            if(board.getCell(row, i).value == number) {
                return true
            }
        }
        return false
    }

    // Check if the number already exists in the row
    private fun isInColumn(board: Board, col: Int, number: Int): Boolean {
        for (i in 0 until board.size) {
            if(board.getCell(i, col).value == number) {
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
                if (board.getCell(i, j).value == number) {
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