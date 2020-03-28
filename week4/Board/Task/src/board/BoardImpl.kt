package board

import board.Direction.*

open class SquareBoardImpl(override val width: Int): SquareBoard {
    protected val cells: List<Cell> = (1..width).flatMap { i -> (1..width).map { j -> Cell(i, j) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? =
            if (i !in 1..width || j !in 1..width) null else cells[(i-1)*width+j-1]

    override fun getCell(i: Int, j: Int): Cell =
            getCellOrNull(i, j) ?: throw IllegalArgumentException("Cell out of board: $i, $j")

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> = jRange.mapNotNull { j -> getCellOrNull(i, j) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> = iRange.mapNotNull { i -> getCellOrNull(i, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when (direction) {
        UP -> getCellOrNull(i - 1, j)
        LEFT -> getCellOrNull(i, j - 1)
        DOWN -> getCellOrNull(i + 1, j)
        RIGHT -> getCellOrNull(i, j + 1)
    }
}

class GameBoardImpl<T>(private val gbWidth: Int): SquareBoardImpl(gbWidth), GameBoard<T> {
    protected val values: MutableMap<Cell, T?> = cells.map { it to null }.toMap().toMutableMap()

    override fun get(cell: Cell): T? = values.getOrDefault(cell, null)

    override fun set(cell: Cell, value: T?) {
        values[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = values.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate).first()

    override fun any(predicate: (T?) -> Boolean): Boolean = filter(predicate).isNotEmpty()

    override fun all(predicate: (T?) -> Boolean): Boolean = filter(predicate).size == values.size
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)

