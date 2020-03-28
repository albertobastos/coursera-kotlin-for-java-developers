package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var secret2 = ""
    var guess2 = ""
    var rights =  0
    var wrongs = 0
    for(i in secret.indices) {
        if(secret[i] == guess[i]) {
            rights++
        } else {
            secret2 += secret[i]
            guess2 += guess[i]
        }
    }
    for(c in guess2) {
        val pos = secret2.indexOf(c)
        if(pos >= 0) {
            wrongs++
            secret2 = secret2.removeRange(pos, pos+1)
        }
    }
    return Evaluation(rights, wrongs)
}
