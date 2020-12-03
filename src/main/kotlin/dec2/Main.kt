package dec2

data class PasswordItem(
    val minCount: Int,
    val maxCount: Int,
    val targetChar: Char,
    val password: String
) {
    fun isValid(): Boolean {
        val targetCount = password.count { it == targetChar }

        return targetCount in minCount..maxCount
    }
}

fun parseInput(input: String): PasswordItem {
    val (rangeBlock, targetBlock, password)  = input.split(' ')

    val (minCountString, maxCountString) = rangeBlock.split('-')

    val minCount = minCountString.toInt()
    val maxCount = maxCountString.toInt()

    val targetChar = targetBlock.first()

    return PasswordItem(
        minCount,
        maxCount,
        targetChar,
        password
    )
}

fun main() {
    val result = input
        .map { parseInput(it) }
        .count { it.isValid() }

    println(result)
}