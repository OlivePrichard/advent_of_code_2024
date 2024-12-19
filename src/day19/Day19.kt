package day19

import println
import readInput

fun makePattern(pattern: String, towels: List<String>): Boolean {
    if (pattern.isEmpty()) return true
    for (towel in towels) {
        if (pattern.length >= towel.length && towel == pattern.substring(0, towel.length)
            && makePattern(pattern.substring(towel.length), towels)) {
            return true
        }
    }
    return false
}

fun countPatterns(pattern: String, towels: List<String>, mem: MutableMap<String, Long>): Long {
    if (pattern.isEmpty()) return 1L
    if (mem.containsKey(pattern)) return mem[pattern]!!
    var sum = 0L
    for (towel in towels) {
        if (pattern.length >= towel.length && towel == pattern.substring(0, towel.length)) {
            sum += countPatterns(pattern.substring(towel.length), towels, mem)
        }
    }
    mem[pattern] = sum
    return sum
}

fun part1(input: List<String>): Int {
    val towels = input[0].split(", ")
    return input.drop(2).count {
        makePattern(it, towels)
    }
}

fun part2(input: List<String>): Long {
    val towels = input[0].split(", ")
    val mem = mutableMapOf<String, Long>()
    return input.drop(2).sumOf {
        countPatterns(it, towels, mem)
    }
}

fun main() {
    // Read a test input from the `src/day19/Day19_test.txt` file:
    val testInput = readInput("day19/Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    // Read the input from the `src/day19/Day19.txt` file:
    val input = readInput("day19/Day19")
    part1(input).println()
    part2(input).println()
}
