package day03

import println
import readInput

fun part1(input: List<String>): Int {
    val instruction = "mul\\((\\d+),(\\d+)\\)".toRegex()
    return input.sumOf {
        instruction.findAll(it).map { match ->
            match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }.sum()
    }
}

fun part2(input: List<String>): Int {
    val spliced = input
        .joinToString("")
        .replace("don't\\(\\).*?(?:do\\(\\)|$)".toRegex(), "")

    return "mul\\((\\d+),(\\d+)\\)".toRegex().findAll(spliced).map { match ->
        match.groupValues[1].toInt() * match.groupValues[2].toInt()
    }.sum()
}

fun main() {
    // Read a test input from the `src/day03/Day03_test.txt` file:
    val testInput = readInput("day03/Day03_test")
//    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    // Read the input from the `src/day03/Day03.txt` file:
    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()
}
