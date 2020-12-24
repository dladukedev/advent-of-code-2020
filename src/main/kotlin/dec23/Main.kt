package dec23

import kotlin.collections.HashMap

class CupList<T>(input: List<T>) {
    val map = input.mapIndexed { index, self ->
        val next = if(index+1 == input.count()) {
            0
        } else {
            index+1
        }

        self to input[next]
    }.toMap() as HashMap


    data class Node<T> (
        val self: T,
        val next: T
    )

    operator fun get(value: T): Node<T> {
        val next = map[value] ?: throw Exception("Invalid State, List Missing Link - $value not in $map")
        return Node(value, next)
    }

    fun removeCups(removeAfter: T, count: Int): List<T> {
        val removed = mutableListOf<T>()

        var pointer = this[removeAfter].next
        while(removed.count() < count) {
            val toRemove = pointer
            removed.add(pointer)
            pointer = this[pointer].next
            map.remove(toRemove)
        }
        map[removeAfter] = pointer

        return removed
    }

    fun insertCups(insertAfter: T, cups: List<T>) {
        val end = this[insertAfter].next
        map[insertAfter] = cups.first()

        cups.forEachIndexed { index, cup ->
            when(index) {
                cups.count() - 1 -> {
                    map[cup] = end
                }
                else -> {
                    map[cup] = cups[index + 1]
                }

            }
        }
    }
}

data class CupGameState(
    val currentCup: Int,
    val cups: CupList<Int>
) {
    constructor(input: List<Int>): this(input.first(), CupList(input))
}

fun findDestination(currentCup: Int, removedCups: List<Int>, max: Int): Int {
    val distanceFromCurrentCup = (1 until currentCup).find {
        removedCups.all { cup -> cup != currentCup - it }
    }


    return if(distanceFromCurrentCup != null) {
        currentCup - distanceFromCurrentCup
    } else {
        val distanceFromMax = (0..removedCups.count()).first {
            !removedCups.contains(max - it)
        }

        return max - distanceFromMax
    }
}

fun getNextCurrentCup(currentCup: Int, cupList: CupList<Int>): Int {
    return cupList[currentCup].next
}

fun playGame(state: CupGameState, max: Int): CupGameState {
    val removedCups = state.cups.removeCups(state.currentCup, 3)
    val destination = findDestination(state.currentCup, removedCups, max)
    state.cups.insertCups(destination, removedCups)

    val nextCup = getNextCurrentCup(state.currentCup, state.cups)

    return state.copy(currentCup = nextCup)
}

fun getResultPart1(cupList: CupList<Int>): String {
    var result = ""
    var next = cupList[1].next
    while(next != 1) {
        result += next
        next = cupList[next].next
    }
    return result
}

fun getResultPart2(cupList: CupList<Int>): Long {
    val nextCup = cupList[1].next
    val twoOver = cupList[nextCup].next

    return nextCup.toLong() * twoOver.toLong()
}

fun main() {
    println("------------ PART 1 ------------")
    val initialGameState = CupGameState(input)

    val finalState = (1..100).fold(initialGameState) { acc, i ->
        playGame(acc, 9)
    }

    val result = getResultPart1(finalState.cups)

    println("result: $result")

    println("------------ PART 2 ------------")
    val updatedInput = input + (input.max()!!+1..1_000_000)
    val updatedInitialState = CupGameState(updatedInput)

    val finalUpdatedState = (1..10_000_000).fold(updatedInitialState) { acc, i ->
        playGame(acc, 1_000_000)
    }

    val result2 = getResultPart2(finalUpdatedState.cups)

    println("result: $result2")
}