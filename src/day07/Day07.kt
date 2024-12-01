package day07

import println
import readInput

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

fun main() {
    // Read a test input from the `src/day07/Day07_test.txt` file:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == testInput.size)
    check(part2(testInput) == testInput.size)

    // Read the input from the `src/day07/Day07.txt` file:
    val input = readInput("day07/Day07")
    part1(input).println()
    part2(input).println()
}
