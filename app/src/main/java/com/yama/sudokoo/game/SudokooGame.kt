package com.yama.sudokoo.game

import androidx.lifecycle.MutableLiveData

class SudokooGame {

    // LiveData notifies active Observers about updates to the selected cell, ensuring that UI
    // matches the data state and preventing memory leaks since Observers are bound to Lifecycle
    // objects and clean up after themselves when their associated lifecycle is destroyed
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    private var selectedRow = -1
    private var selectedCol = -1

    // Right when we create the object we post it so the SudokooBoardView
    // knows what the selected row and column is.
    // Initialize the selected cell with an invalid position (-1, -1)
    init {
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
    }

    // Update the selected cell and notify observers
    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
    }

}