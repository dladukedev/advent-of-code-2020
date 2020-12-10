package dec8

sealed class Instruction {
    object Noop : Instruction()
    data class Jump(val distance: Int) : Instruction()
    data class Accumulate(val amount: Int) : Instruction()
}

data class State(
    val accumulator: Int = 0,
    val pointer: Int = 0,
    val calledInstructions: List<Int> = emptyList()
)

fun reduce(action: Instruction, state: State): State =
    when (action) {
        Instruction.Noop -> {
            state.copy(pointer = state.pointer + 1, calledInstructions = state.calledInstructions + state.pointer)
        }
        is Instruction.Jump -> {
            state.copy(
                pointer = state.pointer + action.distance,
                calledInstructions = state.calledInstructions + state.pointer
            )
        }
        is Instruction.Accumulate -> {
            state.copy(
                pointer = state.pointer + 1,
                accumulator = state.accumulator + action.amount,
                calledInstructions = state.calledInstructions + state.pointer
            )
        }
    }


fun toInstruction(input: String): Instruction {
    val (instruction, count) = input.split(" ")

    return when (instruction) {
        "acc" -> Instruction.Accumulate(count.toInt())
        "jmp" -> Instruction.Jump(count.toInt())
        "nop" -> Instruction.Noop
        else -> throw Exception("Unknown Instruction: $instruction")
    }
}

tailrec fun evaluate(instructions: List<Instruction>, state: State = State()): Int {
    if (state.calledInstructions.contains(state.pointer)) {
        println("End Infinite Loop")
        return state.accumulator
    }

    if(state.pointer >= instructions.count()) {
        println("Pointer ${state.pointer} exceeded instruction bounds ${instructions.count() -1}")
        return state.accumulator
    }

    val newState = reduce(instructions[state.pointer], state)

    println(newState.pointer)

    return evaluate(instructions, newState)
}

fun main() {

    println("------------ PART 1 ------------")
    val instructions = input.map { toInstruction(it) }
    val result = evaluate(instructions)
    println("result: $result")

    println("------------ PART 2 ------------")
    val instructions2 = input2.map { toInstruction(it) }
    val result2 = evaluate(instructions2)
    println("result: $result2")

}