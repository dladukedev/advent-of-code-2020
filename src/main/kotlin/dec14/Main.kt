package dec14

data class Mask(
    val characters: Map<Int, Char> = emptyMap()
)

fun parseMask(input: String): Mask {
    val characters = input
        .toCharArray()
        .toList()
        .mapIndexed { index, character ->
            Pair(index, character)
        }
        .filter { it.second != 'X' }
        .map { it.first to it.second }
        .toMap()

    return Mask(characters)
}

sealed class Action {
    data class UpdateMask(val mask: Mask) : Action()
    data class UpdateMemory(val index: Int, val value: Long) : Action()
}

fun parseInput(input: String): List<Action> {
    return input
        .lines()
        .map {
            val (action, value) = it.split(" = ")
            when {
                action == "mask" -> {
                    val mask = parseMask(value)
                    Action.UpdateMask(mask)
                }
                action.matches(Regex("mem\\[.+\\]")) -> {
                    val memIndex = Regex("\\d+").find(action)?.value?.toInt()
                        ?: throw Exception("Invalid action $action")

                    Action.UpdateMemory(memIndex, value.toLong())
                }
                else -> {
                    throw Exception("Invalid action: $action")
                }
            }
        }
}

data class State(val memory: Map<Int, Long> = emptyMap(), val mask: Mask = Mask())

fun applyMask(mask: Mask, value: Long): Long {
    val binaryInput = value.toString(2).padStart(36, '0')

    return binaryInput
        .toCharArray()
        .toList()
        .mapIndexed { index, character -> mask.characters.getOrDefault(index, character) }
        .joinToString("")
        .toLong(2)
}

fun <K, V> Map<K, V>.replace(key: K, value: V): Map<K,V> {
    return this.mapValues {
        if(it.key == key) {
            value
        } else {
            it.value
        }
    }
}

fun <K, V> Map<K, V>.addOrReplace(key: K, value: V): Map<K,V> {
    if(this.containsKey(key)) {
        return this.replace(key, value)
    }

    return this + mapOf(key to value)
}

fun reducer(action: Action, state: State): State {
    return when(action) {
        is Action.UpdateMask -> {
            state.copy(mask = action.mask)
        }
        is Action.UpdateMemory -> {
            val maskedValue = applyMask(state.mask, action.value)
            state.copy(memory = state.memory.addOrReplace(action.index, maskedValue))
        }
    }
}


sealed class DecoderAction {
    data class UpdateMask(val mask: List<Char>) : DecoderAction()
    data class UpdateMemory(val index: Int, val value: Long) : DecoderAction()
}

fun splitMask(input: String): List<Char> {
   return input.toCharArray().toList()
}

fun parseInputForDecoder(input: String): List<DecoderAction> {
    return input
        .lines()
        .map {
            val (action, value) = it.split(" = ")
            when {
                action == "mask" -> {
                    val mask = splitMask(value)
                    DecoderAction.UpdateMask(mask)
                }
                action.matches(Regex("mem\\[.+\\]")) -> {
                    val memIndex = Regex("\\d+").find(action)?.value?.toInt()
                        ?: throw Exception("Invalid action $action")

                    DecoderAction.UpdateMemory(memIndex, value.toLong())
                }
                else -> {
                    throw Exception("Invalid action: $action")
                }
            }
        }
}

fun <T> List<T>.replaceAt(index: Int, value: T): List<T> {
    val start = if(index < this.count())  this.subList(0, index) else emptyList()
    val end = if(index+1 < this.count()) this.subList(index+1, this.count()) else emptyList()

    return start + listOf(value) + end
}

fun applyMaskToAddress(mask: List<Char>, address: Int): List<Char> {
    val binaryInput = address.toString(2).padStart(36, '0')

    return binaryInput.mapIndexed { index, character ->
        when(mask[index]) {
            'X' -> {
                'X'
            }
            '0' -> {
                character
            }
            '1' -> {
                '1'
            }
            else -> throw Exception("Unknown Character: ${mask[index]}")
        }
    }
}

fun getMemoryAddressForMaskedAddress(address: List<Char>): List<Long> {
    val firstIndex = address.indexOf('X')

    if(firstIndex == -1) {
        val value = address.joinToString("").toLong(2)
        return listOf(value)
    }

    val withZero = address.replaceAt(firstIndex, '0')
    val withOne = address.replaceAt(firstIndex, '1')

    return getMemoryAddressForMaskedAddress(withZero) + getMemoryAddressForMaskedAddress(withOne)
}

data class DecoderState(val memory: Map<Long, Long> = emptyMap(), val mask: List<Char> = emptyList())

fun decoderReducer(action: DecoderAction, state: DecoderState): DecoderState {
    return when(action) {
        is DecoderAction.UpdateMask -> {
            state.copy(mask = action.mask)
        }
        is DecoderAction.UpdateMemory -> {
            val maskedAddress = applyMaskToAddress(state.mask, action.index)
            val addresses = getMemoryAddressForMaskedAddress(maskedAddress)
            val newMemory = addresses.fold(state.memory) {
                acc, address -> acc.addOrReplace(address, action.value)
            }
            state.copy(memory = newMemory)
        }
    }
}

fun main() {
    println("------------ PART 1 ------------")
    val state = parseInput(input)
        .fold(State()) { stateAccum, action ->
            reducer(action, stateAccum)
        }
    val result = state.memory.values.sum()
    println("result: $result")

    println("------------ PART 2 ------------")
    val decoderState = parseInputForDecoder(input)
        .fold(DecoderState()) { stateAccum, action ->
            decoderReducer(action, stateAccum)
        }

    val decoderResult = decoderState.memory.values.sum()
    println("result: $decoderResult")
}
