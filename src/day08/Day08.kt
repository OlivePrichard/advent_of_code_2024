package day08

import println
import readInput

fun findAntiNodes(input: List<String>, antiNodeFinder: (List<MutableList<Boolean>>, Pair<Int, Int>, Pair<Int, Int>) -> Unit): Int {
    val grid = input.map { it.toCharArray().toList() }
    val frequencies = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    val antiNodes = grid.map { it.map { false }.toMutableList() }
    for (row in grid.indices) {
        for (col in grid[row].indices) {
            val char = grid[row][col]
            if (char == '.') continue
            if (frequencies[char] == null) frequencies[char] = mutableListOf()
            frequencies[char]!!.add(row to col)
        }
    }
    for (frequency in frequencies) {
        val emitters = frequency.value
        for (a in emitters) {
            for (b in emitters) {
                if (a == b) continue
                antiNodeFinder(antiNodes, a, b)
            }
        }
    }
    return antiNodes.sumOf { line -> line.count { it } }
}

fun part1(input: List<String>): Int {
    return findAntiNodes(input) { antiNodes, a, b ->
        val diff = 2 * a.first - b.first to 2 * a.second - b.second
        if (diff.first in antiNodes.indices && diff.second in antiNodes[diff.first].indices) {
            antiNodes[diff.first][diff.second] = true
        }
    }
}

fun part2(input: List<String>): Int {
    return findAntiNodes(input) { antiNodes, a, b ->
        for (i in 0..Int.MAX_VALUE) {
            val diff = (i + 1) * a.first - i * b.first to (i + 1) * a.second - i * b.second
            if (diff.first in antiNodes.indices && diff.second in antiNodes[diff.first].indices) {
                antiNodes[diff.first][diff.second] = true
            } else break
        }
    }
}

fun main() {
    // Read a test input from the `src/day08/Day08_test.txt` file:
    val testInput = readInput("day08/Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    // Read the input from the `src/day08/Day08.txt` file:
    val input = readInput("day08/Day08")
    part1(input).println()
    part2(input).println()
}
