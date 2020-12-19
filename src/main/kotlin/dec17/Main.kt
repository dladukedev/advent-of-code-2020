package dec17

data class Point2(val x: Int, val y: Int)
data class Point3(val x: Int, val y: Int, val z: Int)
data class Point4(val w: Int, val x: Int, val y: Int, val z: Int)

fun getNeighbors(point: Point3): List<Point3> {
    val (x, y, z) = point

    return listOf(
        Point3(x - 1, y - 1, z - 1),
        Point3(x, y - 1, z - 1),
        Point3(x + 1, y - 1, z - 1),

        Point3(x - 1, y, z - 1),
        Point3(x, y, z - 1),
        Point3(x + 1, y, z - 1),

        Point3(x - 1, y + 1, z - 1),
        Point3(x, y + 1, z - 1),
        Point3(x + 1, y + 1, z - 1),

        Point3(x - 1, y - 1, z),
        Point3(x, y - 1, z),
        Point3(x + 1, y - 1, z),

        Point3(x - 1, y, z),
        Point3(x + 1, y, z),

        Point3(x - 1, y + 1, z),
        Point3(x, y + 1, z),
        Point3(x + 1, y + 1, z),


        Point3(x - 1, y - 1, z + 1),
        Point3(x, y - 1, z + 1),
        Point3(x + 1, y - 1, z + 1),

        Point3(x - 1, y, z + 1),
        Point3(x, y, z + 1),
        Point3(x + 1, y, z + 1),

        Point3(x - 1, y + 1, z + 1),
        Point3(x, y + 1, z + 1),
        Point3(x + 1, y + 1, z + 1)
    )
}

fun getNeighbors(point: Point4): List<Point4> {
    val (w, x, y, z) = point

    //w
    val points = (-1..1).flatMap { wMod ->
        //x
        (-1..1).flatMap { xMod ->
            //y
            (-1..1).flatMap { yMod ->
                //z
                (-1..1).map { zMod ->
                    Point4(w + wMod, x + xMod, y + yMod, z + zMod)
                }
            }
        }
    }

    // Remove Point We are Looking for Neighbors of
    return points
        .filter {
            it != point
        }
}


fun parseInput(input: String): HashSet<Point2> {
    return input.lines().reversed()
        .asSequence()
        .mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, cell ->
                val isActive = when (cell) {
                    '.' -> false
                    '#' -> true
                    else -> throw Exception("Invalid Character $cell")
                }

                Point2(colIndex, rowIndex) to isActive
            }
        }.flatten()
        .filter { it.second }
        .map { it.first }
        .toHashSet()

}

fun <T> playConwayRound(state: HashSet<T>, getNeighbors: (T) -> List<T>): HashSet<T> {
    val nextState = HashSet<T>()

    fun getActiveNeighborCount(point: T): Int {
        return getNeighbors(point).sumBy { neighbor ->
            if (state.contains(neighbor)) {
                1
            } else {
                0
            }
        }
    }

    state.map {
        val neighbors = getNeighbors(it)

        val activeNeighborCount = getActiveNeighborCount(it)
        if (activeNeighborCount == 2 || activeNeighborCount == 3) {
            nextState.add(it)
        }


        neighbors
            .filter { point -> !state.contains(point) }
            .filter { point ->
                getActiveNeighborCount(point) == 3
            }.forEach { point ->
                nextState.add(point)
            }
    }

    return nextState
}

fun play3dConwaysGame(iterations: Int): Int {
    val startingState = parseInput(input)
        .map { Point3(it.x, it.y, 0) }
        .toHashSet()

    return (0 until iterations).fold(startingState) { accum, _ ->
        playConwayRound(accum) {
            getNeighbors(it)
        }
    }.count()
}

fun play4dConwaysGame(iterations: Int): Int {
    val startingState = parseInput(input)
        .map { Point4(0, it.x, it.y, 0) }
        .toHashSet()

    return (0 until iterations).fold(startingState) { accum, _ ->
        playConwayRound(accum) {
            getNeighbors(it)
        }
    }.count()
}

fun main() {
    println("------------ PART 1 ------------")
    val result = play3dConwaysGame(6)
    println("result: $result")

    println("------------ PART 2 ------------")
    val result2 = play4dConwaysGame(6)
    println("result: $result2")
}