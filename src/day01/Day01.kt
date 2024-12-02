package day01

import println
import readInput
import kotlin.math.abs

fun getLists(input: List<String>): Pair<List<Int>, List<Int>> {
    val locations = input.map { it.split("   ") }
    val firstLocations = locations.map { it.first().toInt() }.sorted()
    val secondLocations = locations.map { it.last().toInt() }.sorted()
    return firstLocations to secondLocations
}

fun part1(input: List<String>): Int {
    val (firstLocations, secondLocations) = getLists(input)
    return firstLocations.zip(secondLocations).sumOf { abs(it.first - it.second) }
}

fun part2(input: List<String>): Int {
    val (firstLocations, secondLocations) = getLists(input)
    return firstLocations.sumOf {
        it * secondLocations.count { x -> x == it }
    }
}

fun main() {
    // Read a test input from the `src/day01/Day01_test.txt` file:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/day01/Day01.txt` file:
    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
