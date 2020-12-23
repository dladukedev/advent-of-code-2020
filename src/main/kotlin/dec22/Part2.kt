package dec22


data class RecursiveGameState(
    val player1Deck: List<Int>,
    val player2Deck: List<Int>,
    val previousStates: List<GameState> = emptyList()
) {
    val gameState = GameState(player1Deck, player2Deck)
}

enum class Player {
    ONE,
    TWO
}

sealed class GameState2 {
    data class Finished(val winner: Player, val winningDeck: List<Int>) : GameState2()
    data class InProgress(val lastGameState: RecursiveGameState) : GameState2()
}

fun updateStateForWinner(state: RecursiveGameState, winner: Player): RecursiveGameState {
    val (player1Card, player1Deck) = state.player1Deck.headTail()
    val (player2Card, player2Deck) = state.player2Deck.headTail()

    return when (winner) {
        Player.ONE -> {
            state.copy(
                player1Deck = player1Deck + listOf(player1Card, player2Card),
                player2Deck = player2Deck,
                previousStates = state.previousStates + listOf(
                    GameState(
                        state.player1Deck,
                        state.player2Deck
                    )
                )
            )
        }
        Player.TWO -> {
            state.copy(
                player1Deck = player1Deck,
                player2Deck = player2Deck + listOf(player2Card, player1Card),
                previousStates = state.previousStates + listOf(
                    GameState(
                        state.player1Deck,
                        state.player2Deck
                    )
                )
            )
        }
    }
}

private tailrec fun runRecursiveGame(state: Stack<GameState2>): Pair<Player, List<Int>> {
    val (currentState, remainingState) = state.pop()

    return when(currentState) {
        is GameState2.Finished -> {
            if (remainingState.isEmpty()) {
                return currentState.winner to currentState.winningDeck
            }

            val (pausedGameState, remainingState2) = remainingState.pop()

            return when (pausedGameState) {
                is GameState2.Finished -> throw Exception("Found Back to Back Finished States, Invalid")
                is GameState2.InProgress -> {
                    val updatedState =
                        updateStateForWinner(pausedGameState.lastGameState, currentState.winner)
                    return runRecursiveGame(remainingState2.push(GameState2.InProgress(updatedState)))
                }
            }
        }
        is GameState2.InProgress -> {
            if (currentState.lastGameState.previousStates.contains(currentState.lastGameState.gameState)) {
                return runRecursiveGame(
                    remainingState.push(
                        GameState2.Finished(
                            Player.ONE,
                            currentState.lastGameState.player1Deck
                        )
                    )
                )
            }
            if (currentState.lastGameState.player1Deck.isEmpty()) {
                return runRecursiveGame(
                    remainingState.push(
                        GameState2.Finished(
                            Player.TWO,
                            currentState.lastGameState.player2Deck
                        )
                    )
                )
            }
            if (currentState.lastGameState.player2Deck.isEmpty()) {
                return runRecursiveGame(
                    remainingState.push(
                        GameState2.Finished(
                            Player.ONE,
                            currentState.lastGameState.player1Deck
                        )
                    )
                )
            }

            val player1Card = currentState.lastGameState.player1Deck.first()
            val player2Card = currentState.lastGameState.player2Deck.first()

            if (player1Card < currentState.lastGameState.player1Deck.count() && player2Card < currentState.lastGameState.player2Deck.count()) {
                return runRecursiveGame(
                    remainingState.push(currentState)
                        .push(
                            GameState2.InProgress(
                                RecursiveGameState(
                                    currentState.lastGameState.player1Deck.drop(1)
                                        .subList(0, player1Card),
                                    currentState.lastGameState.player2Deck.drop(1)
                                        .subList(0, player2Card)
                                )
                            )
                        )
                )
            }

            val updatedState = if(player1Card > player2Card) {
                updateStateForWinner(currentState.lastGameState, Player.ONE)
            } else {
                updateStateForWinner(currentState.lastGameState, Player.TWO)
            }

            return runRecursiveGame(
                remainingState.push(GameState2.InProgress(updatedState))
            )

        }
    }
}

fun playRecursiveGame(player1Deck: List<Int>, player2Deck: List<Int>): Pair<Player, List<Int>> {
    val initialGameState = RecursiveGameState(player1Deck, player2Deck)
    val gameStack = Stack<GameState2>()
        .push(GameState2.InProgress(initialGameState))

    return runRecursiveGame(gameStack)
}