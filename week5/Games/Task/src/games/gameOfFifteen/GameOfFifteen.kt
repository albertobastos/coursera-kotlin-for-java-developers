package games.gameOfFifteen

import board.Direction
import board.Direction.*
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(val initializer: GameOfFifteenInitializer) : Game {
    private val board: GameBoard<Int?> = createGameBoard(4)

    override fun initialize() {
        val perm = initializer.initialPermutation
        board.getAllCells().forEachIndexed { index, cell ->
            if(index < perm.size) board[cell] = perm[index]
        }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean =
        board.getAllCells().all { cell ->
            val expected = if(cell.i * cell.j == board.width) null else cell.i * cell.j
            return board[cell] == expected
        }

    override fun processMove(direction: Direction) {
        val emptyCell = board.getAllCells().find { board[it] == null }
        emptyCell!!
        val sourceCell = when(direction) {
            UP -> board.getCellOrNull(emptyCell.i+1, emptyCell.j)
            DOWN -> board.getCellOrNull(emptyCell.i-1, emptyCell.j)
            LEFT -> board.getCellOrNull(emptyCell.i, emptyCell.j+1)
            RIGHT -> board.getCellOrNull(emptyCell.i, emptyCell.j-1)
        }
        sourceCell!!
        board[emptyCell] = board[sourceCell]
        board[sourceCell] = null
    }

    override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]

}

