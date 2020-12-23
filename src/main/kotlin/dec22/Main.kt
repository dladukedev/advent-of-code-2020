package dec22

fun parsePlayer(input: String): List<Int> {
    return input
        .lines()
        .map { it.toInt() }
}


fun calculateScore(deck: List<Int>): Long {
    return deck.reversed().foldIndexed(0L) { index, acc, card ->
        acc + ((index + 1) * card)
    }
}

fun main() {
    val player1 = parsePlayer(inputPlayer1)
    val player2 = parsePlayer(inputPlayer2)

    println("------------ PART 1 ------------")
    val firstGameResult = playGame(GameState(player1, player2))
    val firstGameScore = calculateScore(firstGameResult)
    println("result: $firstGameScore")

    println("------------ PART 2 ------------")
    val (_, secondGameResult) = playRecursiveGame(player1, player2)
    val secondGameScore = calculateScore(secondGameResult)
    println("result: $secondGameScore")
}