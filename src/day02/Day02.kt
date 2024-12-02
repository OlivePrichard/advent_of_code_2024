package day02

import println
import readInput

fun part1(input: List<String>): Int {
    val levels = input.map { line ->
        line.split(" ").map {
            it.toInt()
        }
    }
    return levels.count {
        val increasing = it.windowed(2).all { (a, b) -> a - b in 1..3 }
        val decreasing = it.windowed(2).all { (a, b) -> b - a in 1..3 }
        increasing || decreasing
    }
}

fun part2(input: List<String>): Int {
    val levels = input.map { line ->
        line.split(" ").map {
            it.toInt()
        }
    }
    return levels.count {
        for (i in it.indices) {
            val dampenedLevels = it.toMutableList()
            dampenedLevels.removeAt(i)
            val increasing = dampenedLevels.windowed(2).all { (a, b) -> a - b in 1..3 }
            val decreasing = dampenedLevels.windowed(2).all { (a, b) -> b - a in 1..3 }

            if (increasing || decreasing) {
                return@count true
            }
        }

        false
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
