package com.yama.sudokoo.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.widget.Button
import com.yama.sudokoo.R.*
import com.yama.sudokoo.game.Cell
import com.yama.sudokoo.view.custom.BoardView
import com.yama.sudokoo.viewmodel.SudokooViewModel

class MainActivity : ComponentActivity(), BoardView.OnTouchListener {

    private lateinit var viewModel: SudokooViewModel
    private lateinit var boardView: BoardView

    // Initialize the activity
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(layout.activity_play_sudokoo)
         // Initialize boardView
         boardView = findViewById(id.boardView)
         boardView.registerListener(this)

         // Initialize the ViewModel
         viewModel = ViewModelProvider(this)[SudokooViewModel::class.java]

         // Observe changes in the selected cell and update the UI
         viewModel.sudokooGame.selectedCellLiveData.observe(
             this,
             Observer { cell -> updateCellLiveUI(cell) }
         )

         viewModel.sudokooGame.cellsLiveData.observe(
             this,
             Observer { updateCells(it) }
         )

         // Initialize buttons
         val oneButton: Button = findViewById(id.oneButton)
         val twoButton: Button = findViewById(id.twoButton)
         val threeButton: Button = findViewById(id.threeButton)
         val fourButton: Button = findViewById(id.fourButton)
         val fiveButton: Button = findViewById(id.fiveButton)
         val sixButton: Button = findViewById(id.sixButton)
         val sevenButton: Button = findViewById(id.sevenButton)
         val eightButton: Button = findViewById(id.eightButton)
         val nineButton: Button = findViewById(id.nineButton)

         val buttons = listOf(oneButton, twoButton, threeButton, fourButton,
             fiveButton, sixButton, sevenButton, eightButton, nineButton)

        // Set click listener for each button
        buttons.forEachIndexed{ index, button ->
            button.setOnClickListener {
                viewModel.sudokooGame.handleInput(index + 1)
            }
        }

     }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        boardView.updateCells(cells)
    }

    // Update the UI when the selected cell changes
    private fun updateCellLiveUI(cell: Pair<Int,Int>?) = cell?.let {
        boardView.updateSelectedCellUI(cell.first, cell.second)
    }

    // Handle cell touch events
    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokooGame.updateSelectedCell(row, col)
    }

}
