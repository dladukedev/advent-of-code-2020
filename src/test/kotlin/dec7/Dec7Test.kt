package dec7

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Dec7Test {
    @Test
    fun getBagsThatCanContain_returnsCountOfAllValidBags() {
        // Given
        val expected = 4
        val bagRules = getBagRules(testInput)
        val shinyGoldBagColor = "shiny gold"

        // When
        val actual = getBagsThatCanContain(shinyGoldBagColor, bagRules).count()

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getBagCountInside_input1_returnsCountOfAllContainedBags() {
        // Given
        val expected = 32
        val bagRules = getBagRules(testInput)
        val shinyGoldBagColor = "shiny gold"

        // When
        val actual = getBagCountInside(shinyGoldBagColor, bagRules)

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getBagCountInside_input2_returnsCountOfAllContainedBags() {
        // Given
        val expected = 126
        val bagRules = getBagRules(testInput2)
        val shinyGoldBagColor = "shiny gold"

        // When
        val actual = getBagCountInside(shinyGoldBagColor, bagRules)

        // Then
        Assertions.assertEquals(expected, actual)
    }
}