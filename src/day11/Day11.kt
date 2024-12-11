package day11

import println
import readInput
import kotlin.time.measureTime

fun memoizedCalculation(input: Long, blinks: Int, memo: MutableMap<Pair<Long, Int>, Long>): Long {
    if (blinks == 0) return 1
    val key = Pair(input, blinks)
    if (key in memo) return memo[key]!!
    if (input == 0L) return memoizedCalculation(1, blinks - 1, memo)
    val str = input.toString()
    if (str.length % 2 == 0) {
        val left = str.substring(0, str.length / 2).toLong()
        val right = str.substring(str.length / 2).toLong()
        val result = memoizedCalculation(left, blinks - 1, memo) + memoizedCalculation(right, blinks - 1, memo)
        memo[key] = result
        return result
    }
    val result = memoizedCalculation(input * 2024L, blinks - 1, memo)
    memo[key] = result
    return result
}

fun part1(input: List<String>): Long {
    return input[0].split(" ").sumOf { memoizedCalculation(it.toLong(), 25, mutableMapOf()) }
}

fun part2(input: List<String>): Long {
    return input[0].split(" ").sumOf { memoizedCalculation(it.toLong(), 75, mutableMapOf()) }
}

fun main() {
    // Read a test input from the `src/day11/Day11_test.txt` file:
    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 55312L)

    // Read the input from the `src/day11/Day11.txt` file:
    val input = readInput("day11/Day11")
    part1(input).println()
    part2(input).println()
}
