package games.game2048

import board.Cell
import board.Direction
import board.Direction.*
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val p = initializer.nextValue(this)
    if (p != null) {
        this[p.first] = p.second
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val after = rowOrColumn.map { this[it] }.moveAndMergeEqual { it*2 }
    rowOrColumn.forEachIndexed { idx, cell -> this[cell] = if (idx < after.size) after[idx] else null }
    return after.isNotEmpty() && after.size < rowOrColumn.size
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    var base = 1..width
    var range = if (direction in listOf(UP, LEFT)) base else base.reversed()
    var moved = false

    when(direction) {
        UP, DOWN ->
            for (col in base) {
                val colMoved = moveValuesInRowOrColumn(getColumn(range, col))
                moved = moved || colMoved
            }
        LEFT, RIGHT ->
            for (row in base) {
                val rowMoved = moveValuesInRowOrColumn(getRow(row, range))
                moved = moved || rowMoved
            }
    }
    return moved
}