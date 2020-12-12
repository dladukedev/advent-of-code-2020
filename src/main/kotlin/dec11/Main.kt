package dec11

import java.lang.IndexOutOfBoundsException

enum class LocationStatus {
    EMPTY,
    FULL,
    FLOOR
}

fun parseInput(input: String): List<List<LocationStatus>> {
    return input
        .lines()
        .map {
            it.toCharArray()
                .toList()
                .map { location ->
                    when(location) {
                        'L' -> LocationStatus.EMPTY
                        '.' -> LocationStatus.FLOOR
                        '#' -> LocationStatus.FULL
                        else -> throw Exception("Unknown character: $location")
                    }
                }
        }
}

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTHEAST,
    NORTHWEST,
    SOUTHEAST,
    SOUTHWEST
}

class Lobby(input: List<List<LocationStatus>>) {
    val seats: List<List<LocationStatus>>
    val rowCount: Int
    val columnCount: Int

    fun getSeat(row: Int, column: Int): LocationStatus {
        if(column > columnCount || row > rowCount) {
            throw IndexOutOfBoundsException("($row, $column) is not a valid location")
        }

        return seats[column][row]
    }

    fun findOccupiedSeatInDirection(row: Int, column: Int, direction: Direction): Boolean {
        var rowIndex = row
        var colIndex = column

        when(direction) {
            Direction.NORTH -> {
                while (colIndex > 0) {
                    colIndex--
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
            Direction.SOUTH -> {
                while (colIndex < columnCount - 1) {
                    colIndex++
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
            Direction.EAST -> {
                while (rowIndex > 0) {
                    rowIndex--
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
            Direction.WEST -> {
                while (rowIndex < rowCount- 1) {
                    rowIndex++
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
            Direction.NORTHEAST -> {
                while (rowIndex > 0 && colIndex > 0) {
                    rowIndex--
                    colIndex--
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
            Direction.NORTHWEST -> {
                while (rowIndex < rowCount - 1 && colIndex > 0) {
                    rowIndex++
                    colIndex--
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
            Direction.SOUTHWEST -> {
                while (rowIndex > 0 && colIndex < columnCount- 1) {
                    rowIndex--
                    colIndex++
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
            Direction.SOUTHEAST -> {
                while (rowIndex < rowCount- 1 && colIndex < columnCount- 1) {
                    rowIndex++
                    colIndex++
                    val seat = getSeat(rowIndex, colIndex)
                    if(seat == LocationStatus.FULL) {
                        return true
                    }
                    if(seat == LocationStatus.EMPTY) {
                        return false
                    }
                }
                return false
            }
        }
    }

    fun occupiedSeatSighlineCount(row: Int, column: Int): Int {
        val visibleSeatOccupance = listOf(
            findOccupiedSeatInDirection(row, column, Direction.NORTH),
            findOccupiedSeatInDirection(row, column, Direction.SOUTH),
            findOccupiedSeatInDirection(row, column, Direction.EAST),
            findOccupiedSeatInDirection(row, column, Direction.WEST),
            findOccupiedSeatInDirection(row, column, Direction.NORTHEAST),
            findOccupiedSeatInDirection(row, column, Direction.NORTHWEST),
            findOccupiedSeatInDirection(row, column, Direction.SOUTHEAST),
            findOccupiedSeatInDirection(row, column, Direction.SOUTHWEST)
        )

        return visibleSeatOccupance.count { it }
    }

    fun occupiedSeatAdjacentCount(row: Int, column: Int): Int {
        var adjacentSeatCount = 0

        val hasSeatAbove = column != 0
        val hasSeatBelow = column != columnCount - 1
        val hasSeatToLeft  = row != 0
        val hasSeatToRight = row != rowCount - 1


        if(hasSeatAbove && getSeat(row, column - 1) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        if(hasSeatAbove && hasSeatToRight && getSeat(row+1, column-1) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        if(hasSeatToRight && getSeat(row+1, column) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        if(hasSeatBelow && hasSeatToRight && getSeat(row+1, column+1) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        if(hasSeatBelow && getSeat(row, column+1) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        if(hasSeatBelow && hasSeatToLeft && getSeat(row-1, column+1) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        if(hasSeatToLeft && getSeat(row-1, column) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        if(hasSeatAbove && hasSeatToLeft && getSeat(row-1, column-1) == LocationStatus.FULL) {
            adjacentSeatCount++
        }

        return adjacentSeatCount
    }

    init {
        rowCount = input.first().count()
        columnCount = input.count()
        seats = input

    }

    override fun toString(): String {
        return seats.joinToString("\r\n") {
            val row = it.map { location ->
                when (location) {
                    LocationStatus.EMPTY -> 'L'
                    LocationStatus.FULL -> '#'
                    LocationStatus.FLOOR -> '.'
                }
            }
            row.toString()
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Lobby || other.rowCount != rowCount || other.columnCount != columnCount) {
            return false
        }

        for (colIndex in 0 until columnCount) {
            for (rowIndex in 0 until rowCount) {
                if(getSeat(rowIndex, colIndex) != other.getSeat(rowIndex, colIndex)) {
                    return false
                }
            }
        }

        return true
    }
}

fun processLobbyRound(lobby: Lobby): Lobby {
    val newLobby = lobby.seats.mapIndexed { colIndex, row ->
        row.mapIndexed { rowIndex, seat ->
            val adjacentCount = lobby.occupiedSeatAdjacentCount(rowIndex, colIndex)
            if (seat == LocationStatus.EMPTY && adjacentCount == 0) {
                LocationStatus.FULL
            } else if (seat == LocationStatus.FULL && adjacentCount >= 4) {
                LocationStatus.EMPTY
            } else {
                seat
            }
        }
    }

    return Lobby(newLobby)
}

tailrec fun processLobbyUntilStable(lobby: Lobby): Lobby {
    val newLobby = processLobbyRound(lobby)

    if (lobby == newLobby) {
        return lobby
    }

    return processLobbyUntilStable(newLobby)
}

fun countFullChairsInStableLobby(lobby: Lobby): Int {
    val stableLobby = processLobbyUntilStable(lobby)

    return stableLobby.seats.sumBy {
        it.sumBy { seat ->
            if (seat == LocationStatus.FULL) 1 else 0
        }
    }
}



fun processLobbyWithSightlinesRound(lobby: Lobby): Lobby {
    val newLobby = lobby.seats.mapIndexed { colIndex, row ->
        row.mapIndexed { rowIndex, seat ->
            val adjacentCount = lobby.occupiedSeatSighlineCount(rowIndex, colIndex)
            if (seat == LocationStatus.EMPTY && adjacentCount == 0) {
                LocationStatus.FULL
            } else if (seat == LocationStatus.FULL && adjacentCount >= 5) {
                LocationStatus.EMPTY
            } else {
                seat
            }
        }
    }

    return Lobby(newLobby)
}

tailrec fun processLobbyWithSightlinesUntilStable(lobby: Lobby): Lobby {
    val newLobby = processLobbyWithSightlinesRound(lobby)

    if (lobby == newLobby) {
        return lobby
    }

    return processLobbyWithSightlinesUntilStable(newLobby)
}

fun countFullChairsInStableLobbyWithSightlines(lobby: Lobby): Int {
    val stableLobby = processLobbyWithSightlinesUntilStable(lobby)

    return stableLobby.seats.sumBy {
        it.sumBy { seat ->
            if (seat == LocationStatus.FULL) 1 else 0
        }
    }
}

fun main() {
    val lobby = Lobby(parseInput(input))

    println("------------ PART 1 ------------")
    val result = countFullChairsInStableLobby(lobby)
    println("result: $result")

    println("------------ PART 2 ------------")
    val result2 = countFullChairsInStableLobbyWithSightlines(lobby)
    println("result: $result2")

}