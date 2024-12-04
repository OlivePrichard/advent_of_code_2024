package day02

import println
import readInput

fun getLevels(input: List<String>) =
    input.map { line ->
        line.split(" ").map {
            it.toInt()
        }
    }

fun checkLevels(levels: List<Int>) =
    levels.windowed(2).map { (a, b) -> a - b }.let { diffs ->
        diffs.all { it in 1..3 } || diffs.all { -it in 1..3 }
    }

fun part1(input: List<String>) =
    getLevels(input).count(::checkLevels)

fun part2(input: List<String>) =
    getLevels(input).count {
        it.indices.any { i ->
            val dampenedLevels = it.toMutableList()
            dampenedLevels.removeAt(i)
            checkLevels(dampenedLevels)
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
