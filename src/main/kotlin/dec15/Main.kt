package dec15

data class ShoutGameState(
    val nextNumber: Int,
    val calledNumbers: HashMap<Int, Int>
)

fun getInitialState(input: List<Int>): ShoutGameState {
    val calledNumbers = input.mapIndexed { index, calledNumber ->
        calledNumber to index
    }.toMap() as HashMap


    return ShoutGameState(0, calledNumbers)
}

fun shoutGame(index: Int, state: ShoutGameState): ShoutGameState {
    val distance = if(state.calledNumbers.containsKey(state.nextNumber)) {
        index - state.calledNumbers.getValue(state.nextNumber)
    } else {
        0
    }

    state.calledNumbers[state.nextNumber] = index

    return ShoutGameState(distance, state.calledNumbers)
}

fun getNextNumber(iterations: Int): Int {
    val initialState = getInitialState(input)
    val finalState = (input.count()..(iterations-2)).fold(initialState.copy()) { accum, index ->
        shoutGame(index, accum)
    }
    return finalState.nextNumber
}

fun main() {
    println("------------ PART 1 ------------")
    val result = getNextNumber(2020)
    println("result: $result")

    println("------------ PART 2 ------------")
    val result2 = getNextNumber(30_000_000)
    println("result: $result2")
}