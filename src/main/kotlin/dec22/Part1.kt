package dec22

data class GameState(
    val player1Deck: List<Int>,
    val player2Deck: List<Int>
)

tailrec fun playGame(state: GameState): List<Int> {
    if (state.player1Deck.isEmpty()) {
        return state.player2Deck
    }

    if (state.player2Deck.isEmpty()) {
        return state.player1Deck
    }

    val player1Play = state.player1Deck.first()
    val player2Play = state.player2Deck.first()

    val updatedState = when {
        player1Play > player2Play -> {
            val updatedPlayer1Deck = state.player1Deck.drop(1) + listOf(player1Play, player2Play)
            val updatedPlayer2Deck = state.player2Deck.drop(1)
            state.copy(player1Deck = updatedPlayer1Deck, player2Deck = updatedPlayer2Deck)
        }
        player2Play > player1Play -> {
            val updatedPlayer1Deck = state.player1Deck.drop(1)
            val updatedPlayer2Deck = state.player2Deck.drop(1) + listOf(player2Play, player1Play)
            state.copy(player1Deck = updatedPlayer1Deck, player2Deck = updatedPlayer2Deck)
        }
        else -> {
            throw Exception("Invalid state update, matching values 1:$player1Play - 2:$player2Play")
        }
    }

    return playGame(updatedState)
}