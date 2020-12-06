package dec5

import java.lang.Exception
import kotlin.math.ceil
import kotlin.math.floor

data class BoardingPassDirections(
    val rowDirections: List<Direction>,
    val seatDirections: List<Direction>
)

data class BoardingPass(
    val row: Int,
    val seat: Int
)

enum class Direction {
    FRONT,
    BACK,
    LEFT,
    RIGHT
}

fun getLocationIndex(directions: List<Direction>, min: Int, max: Int): Int {
    val index = directions.fold(Pair(min, max)){ acc, direction ->
        val accMin = acc.first
        val accMax = acc.second
        val accMidCeil = ceil(((accMin + accMax) / 2.0)).toInt()
        val accMidFloor = floor(((accMin + accMax) / 2.0)).toInt()

        when(direction) {
            Direction.LEFT, Direction.FRONT -> Pair(accMin, accMidFloor)
            Direction.BACK, Direction.RIGHT -> Pair(accMidCeil, accMax)
        }
    }

    return when(directions.last()) {
        Direction.LEFT, Direction.FRONT -> index.first
        Direction.BACK, Direction.RIGHT -> index.second
    }
}

fun getLocation(boardingPassDirections: BoardingPassDirections): BoardingPass {
    val row = getLocationIndex(boardingPassDirections.rowDirections, 0, 127)
    val seat = getLocationIndex(boardingPassDirections.seatDirections, 0, 7)

    return BoardingPass(row, seat)
}

fun getBoardingPassFromInput(directionList: String): BoardingPassDirections {
    val rowChunk = directionList.substring(0, 7)
    val seatChunk = directionList.substring(7)

    val rowDirections = rowChunk.map {
        when(it) {
            'F' -> Direction.FRONT
            'B' -> Direction.BACK
            else -> throw Exception("Invalid Row Direction: $it")
        }
    }

    val seatDirections = seatChunk.map {
        when(it) {
            'R' -> Direction.RIGHT
            'L' -> Direction.LEFT
            else -> throw Exception("Invalid Seat Direction: $it")
        }
    }

    return BoardingPassDirections(
        rowDirections,
        seatDirections
    )
}

fun findMissingSeat(seatIds: List<Int>): Int {
    val min = seatIds.min() ?: throw Exception("Set Contains no Minimum")
    val max = seatIds.max() ?: throw Exception("Set Contains no Maximum")
    val missingSeatIds = (min..max).filter {
        !seatIds.contains(it) && seatIds.contains(it+1) && seatIds.contains(it-1)
    }
    return missingSeatIds.single()
}

fun main() {
    val boardingPasses = input.map { getBoardingPassFromInput(it) }
    val seatIds = boardingPasses.map { getLocation(it) }
        .map { (it.row * 8) + it.seat }

    println("------------ PART 1 ------------")
    val seatId = seatIds.max()
    println("result: $seatId")

    println("------------ PART 1 ------------")
    println(findMissingSeat(seatIds))
}