package com.yama.sudokoo.viewmodel

import androidx.lifecycle.ViewModel
import com.yama.sudokoo.game.Board
import com.yama.sudokoo.game.Cell
import com.yama.sudokoo.game.SudokooGame

class SudokooViewModel : ViewModel() {
    // Initialize the SudokooGame instance
    val sudokooGame = SudokooGame()
}