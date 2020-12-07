package dec6

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Dec6Test {
    private val testInput = """
abc

a
b
c

ab
ac

a
a
a
a

b 
"""

    @Test
    fun chunkInput_splitsIntoCorrectNumberOfParts() {
        val expected = 5
        val actual = chunkInput(testInput).count()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getCharactersFromChunk_returnsAFlatSetOfCharacters() {
        val chunks = chunkInput(testInput)

        val expected = 3
        val actual1 = getUniqueCharactersFromChunk(chunks[0]).count()
        val actual2 = getUniqueCharactersFromChunk(chunks[1]).count()
        val actual3 = getUniqueCharactersFromChunk(chunks[2]).count()

        Assertions.assertEquals(expected, actual1)
        Assertions.assertEquals(expected, actual2)
        Assertions.assertEquals(expected, actual3)
    }

    @Test
    fun getTotalCharacterCountForInput_returnsSumOfAllUniqueCharactersInChunks() {
        val expected = 11
        val actual = getTotalCharacterCountForInput(testInput)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getCharacterCountInAllGroups_returnsSumOfCharactersInAllLines() {
        val expected = 6
        val actual = getCharacterCountInAllGroups(testInput)

        Assertions.assertEquals(expected, actual)
    }
}