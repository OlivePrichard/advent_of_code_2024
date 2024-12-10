package day10

import println
import readInput

fun trailheadScore(heightMap: List<List<Int>>, i: Int, j: Int): Int {
    val directions = listOf(
        0 to 1,
        1 to 0,
        0 to -1,
        -1 to 0
    )
    var reachable = mutableSetOf(i to j)
    for (height in 1..9) {
        val newReachable = mutableSetOf<Pair<Int, Int>>()
        for ((i, j) in reachable) {
            for ((di, dj) in directions) {
                val newI = i + di
                val newJ = j + dj
                if (newI in heightMap.indices && newJ in heightMap[0].indices) {
                    if (heightMap[newI][newJ] == height) {
                        newReachable.add(newI to newJ)
                    }
                }
            }
        }
        reachable = newReachable
    }
    return reachable.size
}

fun trailheadRating(heightMap: List<List<Int>>, i: Int, j: Int): Int {
    val directions = listOf(
        0 to 1,
        1 to 0,
        0 to -1,
        -1 to 0
    )
    var reachable = mutableMapOf((i to j) to 1)
    for (height in 1..9) {
        val newReachable = mutableMapOf<Pair<Int, Int>, Int>()
        for (tile in reachable) {
            val (i, j) = tile.key
            val numPaths = tile.value
            for ((di, dj) in directions) {
                val newI = i + di
                val newJ = j + dj
                if (newI in heightMap.indices && newJ in heightMap[0].indices) {
                    if (heightMap[newI][newJ] == height) {
                        if (!newReachable.contains(newI to newJ)) {
                            newReachable[newI to newJ] = numPaths
                        } else {
                            newReachable[newI to newJ] = newReachable[newI to newJ]!! + numPaths
                        }
                    }
                }
            }
        }
        reachable = newReachable
    }
    return reachable.values.sum()
}

fun part1(input: List<String>): Int {
    val heightMap = input.map { it.map { c -> c.toString().toInt() } }
    var scores = 0
    for (i in heightMap.indices) {
        for (j in heightMap[0].indices) {
            if (heightMap[i][j] == 0) {
                scores += trailheadScore(heightMap, i, j)
            }
        }
    }
    return scores
}

fun part2(input: List<String>): Int {
    val heightMap = input.map { it.map { c -> c.toString().toInt() } }
    var ratings = 0
    for (i in heightMap.indices) {
        for (j in heightMap[0].indices) {
            if (heightMap[i][j] == 0) {
                ratings += trailheadRating(heightMap, i, j)
            }
        }
    }
    return ratings
}

fun main() {
    // Read a test input from the `src/day10/Day10_test.txt` file:
    val testInput = readInput("day10/Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    // Read the input from the `src/day10/Day10.txt` file:
    val input = readInput("day10/Day10")
    part1(input).println()
    part2(input).println()
}
