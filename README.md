# Sudoku Android App

This is a simple Sudoku game implemented for Android. The app allows users to play Sudoku by selecting cells and inputting numbers. The initial Sudoku puzzle displays only the starting cells, with all other cells hidden until the user inputs the correct values.
## Features

 - Initialize Sudoku board with starting cells.
 - Highlight selected cell.
 - Validate Sudoku input to ensure it adheres to Sudoku rules.
 - Save and load game state.
 - Customize cell text styles dynamically.

## Installation

   1. Clone the repository:

```sh
git clone https://github.com/yourusername/sudoku-android-app.git
```
 2. Open the project in Android Studio:
      Open Android Studio.
      Select `File > Open` and navigate to the cloned repository.

   3. Build and run the app:
        Connect your Android device or start an emulator.
        Click the `Run` button in Android Studio.

## Usage
### MainActivity

The MainActivity initializes the app and sets up the Sudoku board. It observes changes to the board and updates the UI accordingly.
SudokooViewModel

### The SudokooViewModel manages the state of the Sudoku board. It initializes the board and provides methods to get cell values for display.
Cell

### The Cell class represents a single cell in the Sudoku board. It includes properties for the row, column, value, and text styling using TextPaint.

### Board

The Board class initializes a 9x9 grid of Cell objects and sets up the starting cells. The Board initialization is done through a Backtracking algorithm that complies with the rules of Sudoku.
