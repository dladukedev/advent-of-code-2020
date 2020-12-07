package dec6

fun chunkInput(input: String): List<String> {
    return input.split(Regex("\\n\\n"))
}

fun getCharactersFromChunk(chunk: String): List<List<Char>> {
    return chunk.lines()
        .map { it.trim() }
        .map { it.toCharArray().toTypedArray().toList() }
        .filter { it.isNotEmpty() }
}

fun getCharactersInAllPartsOfChunk(charList: List<List<Char>>): List<Char> {
    if(charList.isEmpty()) return emptyList()

    return charList[0].filter { searchChar ->
        charList.all {
            it.contains(searchChar)
        }
    }
}

fun getUniqueCharactersFromChunk(chunk: String): List<Char> {
    return getCharactersFromChunk(chunk)
        .flatten()
        .filter { it.toString().matches(Regex("[a-z]")) }
        .distinct()
}

fun getTotalCharacterCountForInput(input: String): Int {
    return chunkInput(input)
        .map { getUniqueCharactersFromChunk(it) }
        .map { it.count() }
        .sum()
}

fun getCharacterCountInAllGroups(input: String): Int {
    return chunkInput(input)
        .map { getCharactersFromChunk(it) }
        .map { getCharactersInAllPartsOfChunk(it) }
        .map { it.count() }
        .sum()

}

fun main() {
    println("------------ PART 1 ------------")
    val result = getTotalCharacterCountForInput(input)
    println("result: $result")

    println("------------ PART 1 ------------")
    val result2 = getCharacterCountInAllGroups(input)
    println("result: $result2")
}