package dec12

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Dec12Test {
    @Test
    fun calculateManhattanDistanceUsingWaypointMethod() {
        // Given
        val input = """
            F10
            N3
            F7
            R90
            F11
        """.trimIndent()
        val expected = 286

        // When
        val actions = parseInput(input)
        val endWaypointState = actions.fold(WaypointState()) { acc, action ->
            waypointReducer(action, acc)
        }
        val actual = abs(endWaypointState.horizontalUnits) + abs(endWaypointState.verticalUnits)

        // Then
        Assertions.assertEquals(expected, actual)
    }
}