package day07

import println
import readInput
import java.lang.Math.pow
import kotlin.math.pow

fun part1(input: List<String>): Long {
    return input.sumOf { line ->
        val split = line.indexOf(":")
        val total = line.substring(0, split).toLong()
        val inputs = line.substring(split + 2).split(" ").map { it.toLong() }
        for (i in 0..<(1 shl (inputs.size - 1))) {
            var acc = inputs[0]
            for (j in 1 until inputs.size) {
                if (((i shr (j - 1)) and 1) == 1) {
                    acc += inputs[j]
                } else {
                    acc *= inputs[j]
                }
            }
            if (acc == total) return@sumOf total
        }
        0
    }
}

fun part2(input: List<String>): Long {
    return input.sumOf { line ->
        val split = line.indexOf(":")
        val total = line.substring(0, split).toLong()
        val inputs = line.substring(split + 2).split(" ").map { it.toLong() }
        for (i in 0..<3.0.pow(inputs.size.toDouble() - 1.0).toInt()) {
            var acc = inputs[0]
            var operators = i
            for (j in 1 until inputs.size) {
                if (operators % 3 == 0) {
                    acc += inputs[j]
                } else if (operators % 3 == 1) {
                    acc *= inputs[j]
                }
                else {
                    val digits = inputs[j].toString().length
                    acc *= 10.0.pow(digits).toLong()
                    acc += inputs[j]
                }
                operators /= 3
            }
            if (acc == total) return@sumOf total
        }
        0
    }
}

fun main() {
    // Read a test input from the `src/day07/Day07_test.txt` file:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    // Read the input from the `src/day07/Day07.txt` file:
    val input = readInput("day07/Day07")
    part1(input).println()
    part2(input).println()
}
