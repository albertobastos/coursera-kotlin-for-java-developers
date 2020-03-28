package nicestring

fun String.isNice(): Boolean {
    fun String.condition1(): Boolean {
        return setOf("ba", "be", "bu").none { this.contains(it) }
    }
    fun String.condition2(): Boolean {
        return count { it in "aeiou" } >= 3
    }
    fun String.condition3(): Boolean {
        return zipWithNext().any { it.first == it.second }
    }
    return listOf(condition1(), condition2(), condition3())
        .count { it } >= 2
}