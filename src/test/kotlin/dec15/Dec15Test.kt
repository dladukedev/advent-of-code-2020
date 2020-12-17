package dec15

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Dec15Test {
    @Test
    fun test() {
        // Given
        val input = listOf(0,3,6)
        val expected = 175594

        // When
        val initialState = getInitialState(input)
        val finalState = (input.count()..29_999_998).fold(initialState) { accum, i ->
            if(i > 29_999_990) {
                println(accum.nextNumber)
            }
            shoutGame(i, accum)
        }
        val actual = finalState.nextNumber

        // Then
        Assertions.assertEquals(expected, actual)
    }
    @Test
    fun test2() {
        // Given
        val input = listOf(1,3,2)
        val expected = 2578

        // When
        val initialState = getInitialState(input)
        val finalState = (input.count()..29_999_998).fold(initialState) { accum, i ->
            if(i > 29_999_990) {
                println(accum.nextNumber)
            }
            shoutGame(i, accum)
        }
        val actual = finalState.nextNumber

        // Then
        Assertions.assertEquals(expected, actual)
    }
}