package day14

import println
import readInput
import kotlin.math.max

fun part1(input: List<String>): Int {
    val (width, height) = if (input.size < 400) 11 to 7 else 101 to 103
    val reg = Regex("""^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""")
    val finalPositions = input.map {
        val nums = reg.find(it)!!.groupValues
        val x = nums[1].toInt()
        val y = nums[2].toInt()
        val dx = nums[3].toInt()
        val dy = nums[4].toInt()
        (x + dx * 100).mod(width) to (y + dy * 100).mod(height)
    }
    val (midX, midY) = (width - 1) / 2 to (height - 1) / 2
    val quads = mutableListOf(0, 0, 0, 0)
    for ((x, y) in finalPositions) {
        if (x < midX) {
            if (y < midY) {
                quads[0]++
            } else if (y > midY) {
                quads[1]++
            }
        } else if (x > midX) {
            if (y < midY) {
                quads[2]++
            } else if (y > midY) {
                quads[3]++
            }
        }
    }
    return quads.fold(1) { acc, i -> acc * i }
}

fun getRobots(width: Int, height: Int, time: Int,
              robots: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) =
    robots.map {
        var (x, y) = it.first
        val (dx, dy) = it.second
        x = (x + dx * time).mod(width)
        y = (y + dy * time).mod(height)
        x to y
    }

fun displayRobots(robots: List<Pair<Int, Int>>, width: Int, height: Int) {
    val grid = List(height) { MutableList(width) { 0 } }
    for ((x, y) in robots) {
        grid[y][x]++
    }
    grid.joinToString("\n") {
        line -> line.joinToString("") { if (it == 0) " " else "#" }
    }.println()
}

fun part2(input: List<String>): Int {
    val (width, height) = if (input.size < 400) 11 to 7 else 101 to 103
    val reg = Regex("""^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""")
    val robots = input.map {
        val nums = reg.find(it)!!.groupValues
        val x = nums[1].toInt()
        val y = nums[2].toInt()
        val dx = nums[3].toInt()
        val dy = nums[4].toInt()
        (x to y) to (dx to dy)
    }.toMutableList()
    val initialIndices = (0 until width + height - 1).map {
        if (it < width) 0 to it else width - 1 to it - width + 1
    }
    val bestTimes = (0 until width * height).map { time ->
        val robotPositions = getRobots(width, height, time, robots)
        val grid = BooleanArray(width * height)
        for ((x, y) in robotPositions) {
            grid[y * width + x] = true
        }
        time to initialIndices.maxOf {
            var i = it.second
            var j = it.first
            var longest = 0
            var current = 0
            while (j in 0 until width && i in 0 until height) {
                if (grid[i * width + j]) {
                    current++
                    longest = max(longest, current)
                } else {
                    current = 0
                }
                i++
                j--
            }
            longest
        }
    }.sortedBy { -it.second }.map { it.first }
    displayRobots(getRobots(width, height, bestTimes[0], robots), width, height)
    return bestTimes[0]
}

fun main() {
    // Read a test input from the `src/day14/Day14_test.txt` file:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 12)
//    check(part2(testInput) == testInput.size)

    // Read the input from the `src/day14/Day14.txt` file:
    val input = readInput("day14/Day14")
    part1(input).println()
    part2(input).println()
}
