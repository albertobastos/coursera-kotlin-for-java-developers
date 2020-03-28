package games.gameOfFifteen

import java.util.*
import kotlin.random.Random.Default.nextInt

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val range = 1..15
        val l = range.toMutableList()
        l.shuffle()
        if (!isEven(l)) {
            val a = range.random()
            var b: Int? = null
            do {
                b = range.random()
            } while(a != b)

            val tmp = l[a]
            l[a] = l[b!!]
            l[b!!] = tmp
        }
        l
    }
}

