package day02

import println
import readInput

fun getLevels(input: List<String>): List<List<Int>> {
    return input.map { line ->
        line.split(" ").map {
            it.toInt()
        }
    }
}

fun checkLevels(levels: List<Int>): Boolean {
    return levels.windowed(2).all { (a, b) -> b - a in 1..3 } || levels.windowed(2).all { (a, b) -> a - b in 1..3 }
}

fun part1(input: List<String>): Int {
    return getLevels(input).count {
        checkLevels(it)
    }
}

fun part2(input: List<String>): Int {
    return getLevels(input).count {
        it.indices.any { i ->
            val dampenedLevels = it.toMutableList()
            dampenedLevels.removeAt(i)
            checkLevels(dampenedLevels)
        }
    }
}

fun main() {
    // Read a test input from the `src/day02/Day02_test.txt` file:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/day02/Day02.txt` file:
    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}
