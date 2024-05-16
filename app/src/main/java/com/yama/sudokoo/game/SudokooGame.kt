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
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, i % 9 )} // 0 to 80
        cells[11].isStartingCell = true
        cells[24].isStartingCell = true
        board = Board(9, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
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