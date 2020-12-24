package dec24

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Dec24Test {
    @Test
    fun test() {
        // Given
        val input = """
sesenwnenenewseeswwswswwnenewsewsw
neeenesenwnwwswnenewnwwsewnenwseswesw
seswneswswsenwwnwse
nwnwneseeswswnenewneswwnewseswneseene
swweswneswnenwsewnwneneseenw
eesenwseswswnenwswnwnwsewwnwsene
sewnenenenesenwsewnenwwwse
wenwwweseeeweswwwnwwe
wsweesenenewnwwnwsenewsenwwsesesenwne
neeswseenwwswnwswswnw
nenwswwsewswnenenewsenwsenwnesesenew
enewnwewneswsewnwswenweswnenwsenwsw
sweneswneswneneenwnewenewwneswswnese
swwesenesewenwneswnwwneseswwne
enesenwswwswneneswsenwnewswseenwsese
wnwnesenesenenwwnenwsewesewsesesew
nenewswnwewswnenesenwnesewesw
eneswnwswnwsenenwnwnwwseeswneewsenese
neswnwewnwnwseenwseesewsenwsweewe
wseweeenwnesenwwwswnew
        """.trimIndent()
        val expected1 = 15
        val expected2 = 12
        val expected3 = 25
        val expected4 = 14
        val expected5 = 23
        val expected20 = 132
        val expected50 = 566
        val expected100 = 2208

        // When
        val tileLocations = parseInput(input)

        val floorDay1 = livingFloor(1, getBlackTiles(tileLocations)).size
        val floorDay2 = livingFloor(2, getBlackTiles(tileLocations)).size
        val floorDay3 = livingFloor(3, getBlackTiles(tileLocations)).size
        val floorDay4 = livingFloor(4, getBlackTiles(tileLocations)).size
        val floorDay5 = livingFloor(5, getBlackTiles(tileLocations)).size
        val floorDay20 = livingFloor(20, getBlackTiles(tileLocations)).size
        val floorDay50 = livingFloor(50, getBlackTiles(tileLocations)).size
        val floorDay100 = livingFloor(100, getBlackTiles(tileLocations)).size

        // Then
        Assertions.assertEquals(expected1, floorDay1)
        Assertions.assertEquals(expected2, floorDay2)
        Assertions.assertEquals(expected3, floorDay3)
        Assertions.assertEquals(expected4, floorDay4)
        Assertions.assertEquals(expected5, floorDay5)
        Assertions.assertEquals(expected20, floorDay20)
        Assertions.assertEquals(expected50, floorDay50)
        Assertions.assertEquals(expected100, floorDay100)
    }
}