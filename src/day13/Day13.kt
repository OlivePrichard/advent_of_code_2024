package day13

import println
import readInput

fun calculation(input: List<String>, offset: Long) =
    Regex("^.+X.(\\d+), Y.(\\d+)$").run {
        input.joinToString("\n").split("\n\n").sumOf { block ->
            val vectors = block.split("\n").map {
                val matches = matchEntire(it)!!
                matches.groupValues[1].toInt() to matches.groupValues[2].toInt()
            }
            // matrix: [
            //   [ Ax, Bx ]
            //   [ Ay, By ]
            // ]
            val ax = vectors[0].first
            val ay = vectors[0].second
            val bx = vectors[1].first
            val by = vectors[1].second
            val x = vectors[2].first + offset
            val y = vectors[2].second + offset
            val det = ax * by - bx * ay
            val inv = listOf(
                listOf(by, -bx),
                listOf(-ay, ax)
            )
            val aPresses = (x * inv[0][0] + y * inv[0][1])
            val bPresses = (x * inv[1][0] + y * inv[1][1])

            if (aPresses % det != 0L || bPresses % det != 0L) {
                return@sumOf 0L
            }
            (3 * aPresses + bPresses) / det
        }
    }

fun part1(input: List<String>) = calculation(input, 0L)

fun part2(input: List<String>) = calculation(input, 10000000000000L)

fun main() {
    // Read a test input from the `src/day13/Day13_test.txt` file:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 480L)
//    println(part2(testInput))
//    check(part2(testInput) == testInput.size)

    // Read the input from the `src/day13/Day13.txt` file:
    val input = readInput("day13/Day13")
    part1(input).println()
    part2(input).println()
}
