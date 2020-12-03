package dec1

private fun findValuesThatSum(arr: List<Int>, target: Int, valueCount: Int = 2): List<Int> {
    val match = arr.toCombinations(valueCount)
        .find { it.sum() == target } ?: emptyList()

    return match
}

fun List<Int>.multiplyList(): Int {
    return this.reduce { acc, i -> acc * i }
}

fun main() {
    println("------------ PART 1 ------------")
    val sumOfTwo = findValuesThatSum(input, 2020).multiplyList()
    println("result: $sumOfTwo")


    println("------------ PART 2 ------------")
    val sumOfThree = findValuesThatSum(input, 2020, 3).multiplyList()
    println("result: $sumOfThree")
}
