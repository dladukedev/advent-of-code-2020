package dec2

data class PasswordItem(
    val minInt: Int,
    val maxInt: Int,
    val targetChar: Char,
    val password: String
)

fun parseInput(input: String): PasswordItem {
    val (rangeBlock, targetBlock, password) = input.split(' ')

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

fun partOneValidation(item: PasswordItem): Boolean {
    val targetCount = item.password.count { it == item.targetChar }

    return targetCount in item.minInt..item.maxInt
}

fun partTwoValidtion(item: PasswordItem): Boolean {
    val indexOne = item.minInt - 1
    val indexTwo = item.maxInt - 1

    val firstChar = if(item.password.length > indexOne) item.password[indexOne] else null
    val secondChar = if(item.password.length > indexTwo) item.password[indexTwo] else null

    if(firstChar == null || secondChar == null) {
        return false
    }

    val firstCharMatchesTarget = firstChar == item.targetChar
    val secondCharMatchesTarget = secondChar == item.targetChar

    return firstCharMatchesTarget && !secondCharMatchesTarget || !firstCharMatchesTarget && secondCharMatchesTarget
}

fun main() {
    val passwords = input.map { parseInput(it) }

    println("------------ PART 1 ------------")
    val partOneValidCount = passwords.count { partOneValidation(it) }

    println(partOneValidCount)


    println("------------ PART 2 ------------")
    val partTwoValidCount = passwords.count { partTwoValidtion(it) }

    println(partTwoValidCount)

}