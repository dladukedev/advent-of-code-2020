package dec18

fun <T> List<T>.tail(): List<T> = this.drop(1)

const val NESTED_PARENTHESIS_REGEX = "\\(([^()]*|\\([^()]*\\))*\\)"

fun solveParenthesis(input: String, sum: (String) -> String): String {
    val parensRegex = Regex(NESTED_PARENTHESIS_REGEX)
    val paren = parensRegex.find(input) ?: return sum(input)

    val inParens = paren.value.substring(1, paren.value.length -1)

    val updatedInput = parensRegex.replaceFirst(input, solveParenthesis(inParens, sum))

    return solveParenthesis(updatedInput, sum)
}

fun newMath(input: String): String {
    val parts = input.split(" ")

    if(parts.count() == 1) {
        return input
    }

    val firstInput = parts.first().toLong()

    val chunks = parts.tail().chunked(2)

    return chunks.fold(firstInput) { accum, (operation, value) ->
        when(operation) {
            "*" -> accum * value.toLong()
            "+" -> accum + value.toLong()
            else -> throw Exception("Unknown Operation")
        }
    }.toString()
}

fun newAdvancedMath(input: String): String {
    if (input.toIntOrNull() != null) {
        return input
    }

    val parts = input.split(" * ")

    return parts.map {
        it.split(" + ")
            .map { num -> num.toLong() }
            .sum()
    }.reduce { acc, num -> acc * num }
        .toString()
}

fun solveEquation(input: String): Long {
    val flattenedInput = solveParenthesis(input) {
        newMath(it)
    }

    return newMath(flattenedInput).toLong()
}

fun solveAdvancedEquation(input: String): Long {
    val flattenedInput = solveParenthesis(input) {
        newAdvancedMath(it)
    }

    return newAdvancedMath(flattenedInput).toLong()
}

fun main() {
    val x = input
        .lines()
        .map { solveEquation(it) }
        .sum()

    println(x)

    val y = input
        .lines()
        .map { solveAdvancedEquation(it) }
        .sum()

    println(y)
}