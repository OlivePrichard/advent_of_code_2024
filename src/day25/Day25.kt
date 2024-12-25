package day25

import println
import readInput

fun part1(input: List<String>): Int {
    val schematics = input.joinToString("\n").split("\n\n").map { line ->
        line.split("\n").map { it.toCharArray().toList() }
    }
    val locks = mutableListOf<List<List<Char>>>()
    val keys = mutableListOf<List<List<Char>>>()
    for (schematic in schematics) {
        if (schematic[0][0] == '.')
            keys.add(schematic)
        else
            locks.add(schematic)
    }
    var count = 0
    for (key in keys) {
        pair@for (lock in locks) {
            for (row in lock.indices) {
                for (col in lock[row].indices) {
                    if (lock[row][col] == '#' && key[row][col] == '#') {
                        continue@pair
                    }
                }
            }
            count++
        }
    }
    return count
}

fun main() {
    // Read a test input from the `src/day25/Day25_test.txt` file:
    val testInput = readInput("day25/Day25_test")
    check(part1(testInput) == 3)

    // Read the input from the `src/day25/Day25.txt` file:
    val input = readInput("day25/Day25")
    part1(input).println()
}
