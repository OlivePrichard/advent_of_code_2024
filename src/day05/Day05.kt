package day05

import println
import readInput

fun getData(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val split = input.indexOf("")
    return input.subList(0, split).map { line ->
        val nums = line.split("|")
        nums.first().toInt() to nums.last().toInt()
    } to input.subList(split + 1, input.size).map { line ->
        line.split(",").map { it.toInt() }
    }
}

fun part1(input: List<String>): Int {
    val (orders, pages) = getData(input)
    val orderings = orders.toSet()
    return pages.filter { pageList ->
        for (i in pageList.indices) {
            for (j in pageList.indices) {
                if (j > i && orderings.contains(pageList[j] to pageList[i])) {
                    return@filter false
                }
            }
        }
        true
    }.sumOf {
        it[(it.size - 1) / 2]
    }
}

fun part2(input: List<String>): Int {
    val (orders, pages) = getData(input)
    val orderings = orders.toSet()
    return pages.filter { pageList ->
        for (i in pageList.indices) {
            for (j in pageList.indices) {
                if (j > i && orderings.contains(pageList[j] to pageList[i])) {
                    return@filter true
                }
            }
        }
        false
    }.sumOf { pageList ->
        val sortedList = pageList.sortedWith { first, second ->
            if (orderings.contains(first to second)) -1 else 1
        }
        sortedList[(sortedList.size - 1) / 2]
    }
}

fun main() {
    // Read a test input from the `src/day05/Day05_test.txt` file:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/day05/Day05.txt` file:
    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()
}
