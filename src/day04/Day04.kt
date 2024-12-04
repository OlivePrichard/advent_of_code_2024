package day04

import println
import readInput

fun getString(grid: List<List<Char>>, x: Int, y: Int, dx: Int, dy: Int, size: Int): String {
    val string = mutableListOf<Char>()
    for (i in 0 until size) {
        val iNew = y + i * dy
        val jNew = x + i * dx
        if (iNew !in grid.indices || jNew !in grid[iNew].indices) {
            return string.joinToString("")
        }
        string.add(grid[y + i * dy][x + i * dx])
    }
    return string.joinToString("")
}

fun part1(input: List<String>): Int {
    val letters = input.map { it.toList() }
    var count = 0
    val word = "XMAS"
    val ways = listOf(
        Pair(1, 0),
        Pair(0, 1),
        Pair(1, 1),
        Pair(1, -1),
        Pair(-1, 0),
        Pair(0, -1),
        Pair(-1, -1),
        Pair(-1, 1),
    )
    for (i in letters.indices) {
        for (j in letters[i].indices) {
            for (way in ways) {
                if (getString(letters, j, i, way.first, way.second, word.length) == word) {
                    count++
                }
            }
        }
    }
    return count
}

fun part2(input: List<String>): Int {
    val grid = input.map { it.toList() }
    val search = "MAS"
    var count = 0
    for (i in 0..<(grid.size - 2)) {
        for (j in 0..<(grid[i].size - 2)) {
            val primary = (0..2).map { grid[i + it][j + it] }.joinToString("")
            val secondary = (0..2).map { grid[i + it][j + 2 - it] }.joinToString("")
            if (primary == search || primary.reversed() == search) {
                if (secondary == search || secondary.reversed() == search) {
                    count++
                }
            }
        }
    }
    return count
}

fun main() {
    // Read a test input from the `src/day04/Day04_test.txt` file:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/day04/Day04.txt` file:
    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}
