package day03

import println
import readInput

fun execute(code: String) =
    "mul\\((\\d+),(\\d+)\\)".toRegex().findAll(code).sumOf {
        it.groupValues[1].toInt() * it.groupValues[2].toInt()
    }

fun part1(input: List<String>) =
    input.joinToString("").let(::execute)

fun part2(input: List<String>) =
    input
        .joinToString("")
        .replace("don't\\(\\).*?(?:do\\(\\)|$)".toRegex(), "")
        .let(::execute)

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
