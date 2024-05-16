package com.yama.sudokoo.game

import androidx.lifecycle.MutableLiveData

class SudokooGame {

    // LiveData notifies active Observers about updates to the selected cell, ensuring that UI
    // matches the data state and preventing memory leaks since Observers are bound to Lifecycle
    // objects and clean up after themselves when their associated lifecycle is destroyed
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    var cellsLiveData = MutableLiveData<List<Cell>>()

    val isTakingNotesLiveData = MutableLiveData<Boolean>()

    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()


    private var selectedRow = -1
    private var selectedCol = -1

    private var isTakingNotes = false

    private val board: Board

    // Right when we create the object we post it so the SudokooBoardView
    // knows what the selected row and column is.
    // Initialize the selected cell with an invalid position (-1, -1)
    init {
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, i % 9 )} // 0 to 80
        cells[11].notes = mutableSetOf(1,2,3,4,5,6,7,8,9)
        board = Board(9, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return

        val cell = board.getCell(selectedRow, selectedCol)

        if (cell.isStartingCell) return

        if (isTakingNotes) {
            if (cell.notes.contains(number)) {
                cell.notes.remove(number)
            } else {
                cell.notes.add(number)
            }
            // Once we've updated the internal state of notes
            // we post it back to the UI
            highlightedKeysLiveData.postValue(cell.notes)
        }
        else
        {
            cell.value = number
        }
        cellsLiveData.postValue(board.cells)
    }

    // Update the selected cell and notify observers
    fun updateSelectedCell(row: Int, col: Int) {
        val cell = board.getCell(row, col)
        if (!cell.isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))

            if (isTakingNotes) {
                highlightedKeysLiveData.postValue(cell.notes)
            }
        }
    }

    fun toggleNoteTakingState() {
        isTakingNotes = !isTakingNotes
        isTakingNotesLiveData.postValue(isTakingNotes)

        val curNotes = if (isTakingNotes) {
            board.getCell(selectedRow, selectedCol).notes
        } else {
            setOf<Int>()
        }
        highlightedKeysLiveData.postValue(curNotes)
    }

}