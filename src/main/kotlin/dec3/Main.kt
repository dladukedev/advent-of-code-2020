package dec3

const val TREE_NODE = '#'

fun mapToResult(row: String, steps: Int, index: Int): Char {
    val location = (index * steps) % row.length
    return row[location]
}

fun countTreesInPath(slope: List<String>, stepsRight: Int, stepsDown: Int): Int {
    return slope
        .filterIndexed { index, _ -> index % stepsDown == 0 }
        .mapIndexed { index, s -> mapToResult(s, stepsRight, index) }
        .count { it == TREE_NODE }
}

fun main() {

    println("------------ PART 1 ------------")
    println(countTreesInPath(input, 3, 1))


    println("------------ PART 2 ------------")
    val path1 =countTreesInPath(input, 1, 1)
    println(path1)
    val path2 =countTreesInPath(input, 3, 1)
    println(path2)
    val path3 =countTreesInPath(input, 5, 1)
    println(path3)
    val path4 =countTreesInPath(input, 7, 1)
    println(path4)
    val path5 =countTreesInPath(input, 1, 2)
    println(path5)

    val result = path1 * path2 * path3 * path4 * path5

    println("result: $result")



}