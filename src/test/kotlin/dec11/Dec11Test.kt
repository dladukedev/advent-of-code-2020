package dec11

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Dec11Test {
    @Test
    fun countFullChairsInStableLobbyWithSightlines_countsCorrectly() {
        // Given
        val input = """
L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL
        """.trimIndent()

        val expected = 26
        val lobby = Lobby(parseInput(input))

        // When
        val actual = countFullChairsInStableLobbyWithSightlines(lobby)

        // Then
        Assertions.assertEquals(expected, actual)
    }
}