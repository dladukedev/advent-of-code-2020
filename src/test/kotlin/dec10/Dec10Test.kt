package dec10

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Dec10Test {
    @Test
    fun countJumps_countsCorrectly() {
        // Given
        val input = listOf(
            16,
            10,
            15,
            5,
            1,
            11,
            7,
            19,
            6,
            12,
            4
        )
        val expectedOneJumps = 7
        val expectedThreeJumps = 5

        // When
        val actual = countJumps(input)

        // Then
        Assertions.assertEquals(expectedOneJumps, actual.oneJumps)
        Assertions.assertEquals(expectedThreeJumps, actual.threeJumps)
    }

    @Test
    fun countPaths_countsCorrectly() {
        // Given
        val input = listOf(
            16,
            10,
            15,
            5,
            1,
            11,
            7,
            19,
            6,
            12,
            4
        )
        val expected = 8L

        // When
        val actual = countValidPaths(input)

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun countPaths_countsCorrectly2() {
        // Given
        val input = listOf(
            28,
            33,
            18,
            42,
            31,
            14,
            46,
            20,
            48,
            47,
            24,
            23,
            49,
            45,
            19,
            38,
            39,
            11,
            1,
            32,
            25,
            35,
            8,
            17,
            7,
            9,
            4,
            2,
            34,
            10,
            3
        )
        val expected = 19208L

        // When
        val actual = countValidPaths(input)

        // Then
        Assertions.assertEquals(expected, actual)
    }
}
