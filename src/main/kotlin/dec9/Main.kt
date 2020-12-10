package dec9

import java.lang.Exception

@ExperimentalStdlibApi
fun findRangeThatSums(target: Long, digits: List<Long>): List<Long> {
     val result = digits.scan(0L) { acc, l -> acc + l }
         .indexOf(target)

    return if(result == -1) {
        findRangeThatSums(target, digits.drop(1))
    } else {
        digits.subList(0, result+1)
    }
}

fun findMinAndMaxSummed(numbers: List<Long>): Long {
    val min = numbers.min() ?: throw Exception("List must Have min")
    val max = numbers.max() ?: throw Exception("List must Have max")

    return min + max
}

tailrec fun findValueThatDoesntSum(digits: List<Long>): Long {
    val target = digits[25]
    val prelude = digits.take(25)

    val hasMatch = prelude.toCombinations(2)
        .map { it[0] + it[1] }
        .any { it == target }

    return if (!hasMatch) {
        target
    } else {
        findValueThatDoesntSum(digits.drop(1))
    }
}

fun List<Int>.multiplyList(): Int {
    return this.reduce { acc, i -> acc * i }
}

@ExperimentalStdlibApi
fun main() {
    println("------------ PART 1 ------------")
    val result = findValueThatDoesntSum(input)
    println("result: $result")


    println("------------ PART 2 ------------")
    val range = findRangeThatSums(result, input)
    val result2 = findMinAndMaxSummed(range)
    println(result2)
}
