package com.yama.sudokoo.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.KeyEventDispatcher.Component
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yama.sudokoo.R
import com.yama.sudokoo.R.*
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
         boardView = findViewById(R.id.boardView)
         boardView.registerListener(this)

         // Initialize the ViewModel
         viewModel = ViewModelProvider(this)[SudokooViewModel::class.java]

         // Observe changes in the selected cell and update the UI
         viewModel.sudokooGame.selectedCellLiveData.observe(
             this,
             Observer { cell -> updateCellLiveUI(cell) }
         )
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
