package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.math.BigInteger.ONE

class Rational(_n: BigInteger, _d: BigInteger): Comparable<Rational> {
    val n: BigInteger
    val d: BigInteger

    init {
        require(_d != ZERO) { "Zero denominator" }
        val gcd = _n.gcd(_d)
        val sign = _d.signum().toBigInteger()
        n = _n / gcd * sign
        d = _d / gcd * sign
    }

    private fun isInt() = d == ONE
    override fun toString() = "" + n + if (isInt()) "" else "/${d}"

    operator fun plus(other: Rational) = Rational(n*other.d + d*other.n, d*other.d)
    operator fun minus(other: Rational) = Rational(n*other.d - d*other.n, d*other.d)
    operator fun times(other: Rational) = Rational(n*other.n, d*other.d)
    operator fun div(other: Rational) = Rational(n*other.d, d*other.n)
    operator fun unaryMinus() = Rational(-n, d)

    override operator fun compareTo(other: Rational): Int {
        return (n * other.d).compareTo(d * other.n)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        if (n != other.n) return false
        if (d != other.d) return false

        return true
    }

    override fun hashCode(): Int {
        var result = n.hashCode()
        result = 31 * result + d.hashCode()
        return result
    }
}

fun String.toRational(): Rational {
    fun String.toBigIntegerOrFail() = toBigIntegerOrNull() ?: throw IllegalArgumentException("Invalid rational number: $this@toRational")
    if (!contains('/')) {
        return Rational(toBigIntegerOrFail(), ONE)
    }
    val (nstr, dstr) = split('/')
    return Rational(nstr.toBigIntegerOrFail(), dstr.toBigIntegerOrFail())
}
infix fun Int.divBy(x: Int) = Rational(this.toBigInteger(), x.toBigInteger())
infix fun Long.divBy(x: Long) = Rational(this.toBigInteger(), x.toBigInteger())
infix fun BigInteger.divBy(x: BigInteger) = Rational(this, x)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}