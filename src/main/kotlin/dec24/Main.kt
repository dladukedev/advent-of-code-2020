package dec24

enum class Direction {
    NORTHWEST,
    NORTHEAST,
    SOUTHWEST,
    SOUTHEAST,
    EAST,
    WEST
}

fun parseInput(input: String): List<List<Direction>> {
    return input.lines()
        .map { it.toCharArray().toList() }
        .map { line ->
            line.foldIndexed(emptyList<Direction>()) { index, acc, direction ->
                when (direction) {
                    'n' -> {
                        when (val foundChar = line.getOrNull(index + 1)) {
                            'e' -> acc + Direction.NORTHEAST
                            'w' -> acc + Direction.NORTHWEST
                            null -> throw IndexOutOfBoundsException(index + 1)
                            else -> throw Exception("Invalid Character $foundChar")
                        }
                    }
                    's' -> {
                        when (val foundChar = line.getOrNull(index + 1)) {
                            'e' -> acc + Direction.SOUTHEAST
                            'w' -> acc + Direction.SOUTHWEST
                            null -> throw IndexOutOfBoundsException(index + 1)
                            else -> throw Exception("Invalid Character $foundChar")
                        }
                    }
                    'e' -> {
                        if (index == 0 || (line[index - 1] != 'n' && line[index - 1] != 's')) {
                            acc + Direction.EAST
                        } else {
                            acc
                        }
                    }
                    'w' -> {
                        if (index == 0 || (line[index - 1] != 'n' && line[index - 1] != 's')) {
                            acc + Direction.WEST
                        } else {
                            acc
                        }
                    }
                    else -> throw Exception("Invalid Character $direction")
                }
            }
        }
}

data class TileLocation(val x: Int, val y: Int) {
    val neighbors by lazy {
        listOf(
            TileLocation(x - 1, y + 2),
            TileLocation(x - 1, y - 2),
            TileLocation(x + 1, y + 2),
            TileLocation(x + 1, y - 2),
            TileLocation(x + 2, y),
            TileLocation(x - 2, y)
        )
    }
}

tailrec fun findTile(directions: List<Direction>, currentLocation: TileLocation = TileLocation(0,0)): TileLocation {
    if(directions.isEmpty()) {
        return currentLocation
    }

    val updatedLocation = when(directions.first()) {
        Direction.NORTHWEST -> TileLocation(currentLocation.x - 1, currentLocation.y + 2)
        Direction.NORTHEAST -> TileLocation(currentLocation.x + 1, currentLocation.y + 2)
        Direction.SOUTHWEST -> TileLocation(currentLocation.x - 1, currentLocation.y - 2)
        Direction.SOUTHEAST -> TileLocation(currentLocation.x + 1, currentLocation.y - 2)
        Direction.EAST -> currentLocation.copy(x = currentLocation.x + 2)
        Direction.WEST -> currentLocation.copy(x = currentLocation.x - 2)
    }

    return findTile(directions.drop(1), updatedLocation)
}

fun flipTile(directions: List<Direction>, blackTiles: HashSet<TileLocation>): HashSet<TileLocation>{
    val tileLocation = findTile(directions)

    if(blackTiles.contains(tileLocation)) {
        blackTiles.remove(tileLocation)
    } else {
        blackTiles.add(tileLocation)
    }

    return blackTiles
}

fun getBlackTiles(tileLocations: List<List<Direction>>): HashSet<TileLocation> {
    return tileLocations.fold(HashSet()) { acc, tileLocationDirections ->
        flipTile(tileLocationDirections, acc)
    }
}

fun countBlackTiles(tileLocations: List<List<Direction>>): Int {
    return getBlackTiles(tileLocations).size
}

sealed class Tile(val location: TileLocation) {
    data class Black(val tileLocation: TileLocation): Tile(tileLocation)
    data class White(val tileLocation: TileLocation): Tile(tileLocation)
}

fun updateFloor(blackTiles: HashSet<TileLocation>): HashSet<TileLocation> {
    return blackTiles.flatMap {
        it.neighbors
    }.map {
        if(blackTiles.contains(it)) {
            Tile.Black(it)
        } else {
            Tile.White(it)
        }
    }.fold(HashSet<TileLocation>()) { acc, tile ->
        val blackNeighborsCount = tile.location.neighbors.count {
            blackTiles.contains(it)
        }

        when(tile) {
            is Tile.Black -> {
                if(blackNeighborsCount in 1..2) {
                    acc.add(tile.location)
                }
                acc
            }
            is Tile.White -> {
                if(blackNeighborsCount == 2) {
                    acc.add(tile.location)
                }
                acc
            }
        }
    }
}

fun livingFloor(days: Int, blackTiles: HashSet<TileLocation>): HashSet<TileLocation> {
    return (1..days).fold(blackTiles) {acc, i ->
        updateFloor(acc)
    }
}

fun main() {
    val tileLocations = parseInput(input)

    println("------------ PART 1 ------------")
    val blackTilesCount = countBlackTiles(tileLocations)

    println("result: $blackTilesCount")

    println("------------ PART 2 ------------")
    val livingFloorBlackTilesCount = livingFloor(100, getBlackTiles(tileLocations)).size

    println("result: $livingFloorBlackTilesCount")
}