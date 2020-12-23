package dec20

import java.lang.IndexOutOfBoundsException

fun <T>  List<List<T>>.rotate90(): List<List<T>> {
    return this.mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, _ ->
            this[row.count() - colIndex - 1][rowIndex]
        }
    }
}
fun <T>  List<List<T>>.rotate180(): List<List<T>> {
    return this.reversed()
}
fun <T>  List<List<T>>.rotate270(): List<List<T>> {
    return this.mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, _ ->
            this[colIndex][this.count() - rowIndex - 1]
        }
    }
}
fun <T>  List<List<T>>.flipH(): List<List<T>> {
    return this.map { it.reversed() }
}
fun <T>  List<List<T>>.permutations(): List<List<List<T>>> {
    return listOf(
        this,
        this.rotate90(),
        this.rotate180(),
        this.rotate270(),

        this.flipH(),
        this.flipH().rotate90(),
        this.flipH().rotate180(),
        this.flipH().rotate270()
    )
}

data class PuzzlePiece(val id: Int, val piece: List<List<Char>>) {
    val top = piece.first()
    val bottom = piece.last()
    val right = piece.map { it.last() }
    val left = piece.map { it.first() }

    val sides: List<List<Char>> = listOf(
        top,
        bottom,
        right,
        left
    )

    fun rotate90(): PuzzlePiece {
        val pieces = piece.rotate90()
        return PuzzlePiece(id, pieces)
    }

    fun rotate180(): PuzzlePiece {
        val pieces = piece.rotate180()
        return PuzzlePiece(id, pieces)
    }

    fun rotate270(): PuzzlePiece {
        val pieces = piece.rotate270()
        return PuzzlePiece(id, pieces)
    }


    fun flipH(): PuzzlePiece {
        val pieces = piece.flipH()
        return PuzzlePiece(id, pieces)
    }

    fun removeBorder(): PuzzlePiece {
        val pieces = piece.drop(1).dropLast(1)
            .map {
                it.drop(1).dropLast(1)
            }
        return PuzzlePiece(id, pieces)
    }
}



fun puzzleSidePermutations(puzzlePiece: PuzzlePiece): List<List<Char>> {
    return listOf(
        puzzlePiece.sides,
        puzzlePiece.flipH().rotate90().sides
    ).flatten()
}

fun puzzlePiecePermutations(puzzlePiece: PuzzlePiece): List<PuzzlePiece> {
    return puzzlePiece.piece.permutations()
        .map { PuzzlePiece(puzzlePiece.id, it) }
}

fun parseInput(input: String): List<PuzzlePiece> {
    return input
        .lines()
        .filter { it.isNotEmpty() }
        .chunked(11)
        .map {
            val title = it.first().substring(5, 9).toInt()
            val piece = it.drop(1)
                .map { row ->
                    row.toCharArray().toList()
                }

            PuzzlePiece(title, piece)
        }
}

fun getUniqueEdges(puzzlePieces: List<PuzzlePiece>): List<List<Char>> {
    return puzzlePieces.flatMap {
        puzzleSidePermutations(it)
    }.groupingBy { it }
        .eachCount()
        .filter {
            it.value == 1
        }
        .map { it.key }
}

fun findCorners(puzzlePieces: List<PuzzlePiece>): List<PuzzlePiece> {
    val uniqueEdges = getUniqueEdges(puzzlePieces)

    return puzzlePieces.filter { piece ->
        val countOfEdges = uniqueEdges.count { edge ->
            piece.sides.contains(edge)
        }

        countOfEdges == 2
    }
}

val ROW_MAX = 11
val COL_MAX = 11

fun buildPuzzleRecur(
    remainingPieces: List<PuzzlePiece>,
    outsideEdges: List<List<Char>>,
    location: Pair<Int, Int> = Pair(0, 0),
    puzzle: MutableList<MutableList<PuzzlePiece>> = mutableListOf()
): List<List<PuzzlePiece>> {
    if (remainingPieces.isEmpty()) {
        return puzzle
    }

    val (rowIndex, colIndex) = location

    val matchx = remainingPieces.flatMap {
        val matches = puzzlePiecePermutations(it)
            .filter { piece ->
                val topValid = if (rowIndex == 0) {
                    outsideEdges.contains(piece.top)
                } else {
                    puzzle[rowIndex - 1][colIndex].bottom == piece.top
                }

                val leftValid = if (colIndex == 0) {
                    outsideEdges.contains(piece.left)
                } else {
                    puzzle[rowIndex][colIndex - 1].right == piece.left
                }

                topValid && leftValid
            }

        matches
    }

    val match = matchx.first()

    if (colIndex == 0) {
        puzzle.add(mutableListOf(match))
    } else {
        puzzle[rowIndex].add(match)
    }

    val newRemainPieces = remainingPieces.filter { it.id != match.id }

    val nextPoint = if(colIndex == COL_MAX) Pair(rowIndex + 1, 0) else Pair(rowIndex,colIndex + 1)

    return buildPuzzleRecur(newRemainPieces, outsideEdges, nextPoint, puzzle)
}

fun buildPuzzle(puzzlePieces: List<PuzzlePiece>): List<List<Char>> {
    val outsideEdges = getUniqueEdges(puzzlePieces)

    val assembledPieces =  buildPuzzleRecur(puzzlePieces, outsideEdges)
    val puzzle = assembledPieces.map {row ->
        row.map { it.removeBorder() }
    }

    return puzzle.map {row ->
        (0 until 8).map { index ->
            row.fold(emptyList<Char>()) { acc, puzzlePiece -> acc + puzzlePiece.piece[index] }
        }
    }.flatten()
        .permutations()
        .single {
            countSeaSerpents(it) > 0
        }

}

fun findSeaSerpentAtPoint(x: Int, y: Int, puzzle: List<List<Char>>): Boolean {
    return try {
        val points = listOf(
            Pair(x, y),
            Pair(x+1, y+1),
            Pair(x+4, y+1),
            Pair(x+5, y),
            Pair(x+6, y),
            Pair(x+7, y+1),
            Pair(x+10, y+1),
            Pair(x+11, y),
            Pair(x+12, y),
            Pair(x+13, y+1),
            Pair(x+16, y+1),
            Pair(x+17, y),
            Pair(x+18, y),
            Pair(x+18, y-1),
            Pair(x+19, y)
        )

        points.all {
            puzzle[it.first][it.second] == '#'
        }
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun countSeaSerpents(puzzle: List<List<Char>>): Int {
    return puzzle.mapIndexed { index, row ->
        row.mapIndexed { colIndex, _ ->
            findSeaSerpentAtPoint(colIndex, index, puzzle)
        }
    }.flatten()
        .count { it }
}

fun main() {
    val pieces = parseInput(input)
    val corners = findCorners(pieces)
    val result = corners.fold(1L) { acc, corner ->
        acc * corner.id
    }
    println("------------ PART 1 ------------")
    println("result: $result")

    val puzzle = buildPuzzle(pieces)

    val countOfHashes = puzzle.sumBy { it.count { it == '#' } }
    val countOfSeaSerpents = countSeaSerpents(puzzle)
    val countOfSeaSerpentTiles = countOfSeaSerpents * 15
    val nonSeaSerpentHashes = countOfHashes - countOfSeaSerpentTiles

    println("------------ PART 2 ------------")
    println("result: $nonSeaSerpentHashes")
}