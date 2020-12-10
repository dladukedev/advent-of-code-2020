package dec9

// Rewritten in Kotlin from: https://gist.github.com/axelpale/3118596
fun <T> combinations(list: List<T>, length: Int): List<List<T>> {
    val combinations = mutableListOf<List<T>>()

    if(length > list.count() || length <= 0) {
        return emptyList()
    }

    if(length == list.count()) {
        return listOf(list)
    }

    if(length == 1) {
        for(i in 0 until list.count()) {
            combinations.add(listOf(list[i]))
        }
        return combinations
    }

    for(i in 0 until list.count() - length) {
        val head = list.subList(i, i+1)
        val tailCombinations = combinations(list.drop(i+1), length-1)
        for(j in 0 until tailCombinations.count()) {
            combinations.add(head + tailCombinations[j])
        }
    }
    return combinations
}

fun <T> List<T>.toCombinations(length: Int): List<List<T>> {
    return combinations(this, length)
}