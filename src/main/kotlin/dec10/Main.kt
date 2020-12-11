package dec10

import java.lang.Exception

data class JumpResults(
    val oneJumps: Int = 0,
    val threeJumps: Int = 0
)

tailrec fun countJumps(input: List<Int>, currentJolts: Int = 0, accum: JumpResults = JumpResults()): JumpResults {
    return when {
        input.contains(currentJolts + 1) -> {
            countJumps(input, currentJolts + 1, accum.copy(oneJumps = accum.oneJumps + 1))
        }
        input.contains(currentJolts + 3) -> {
            countJumps(input, currentJolts + 3, accum.copy(threeJumps = accum.threeJumps + 1))
        }
        else -> {
            accum.copy(threeJumps = accum.threeJumps + 1)
        }
    }
}


fun reduceToChunks(input: List<Int>): List<Int> {
    val sortedInput = input.sorted()

    return sortedInput.foldIndexed(listOf(1)) { index, acc, number ->
        if (index == 0 || number - 1 == sortedInput[index - 1]) {
            val currentSequenceCount = acc.last()
            acc.dropLast(1) + listOf(currentSequenceCount + 1)
        } else {
            acc + listOf(1)
        }
    }
}

fun countValidPaths(input: List<Int>): Long {
    return reduceToChunks(input)
        .map {
            // map to number of paths based on sequence length
            when(it) {
                1, 2 -> 1L
                3 -> 2L
                4 -> 4L
                5 -> 7L
                else -> throw Exception("Sequence of unknown length: $it")
            }
        }
        .reduce { acc, number -> acc * number }
}

fun main() {
    println("------------ PART 1 ------------")
    val jumpResults = countJumps(input)
    val result = jumpResults.oneJumps * jumpResults.threeJumps
    println("result: $result")

    println("------------ PART 2 ------------")
    val result1 = countValidPaths(input)
    println("result: $result1")

}