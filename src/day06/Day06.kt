package day06

import println
import readInput
import kotlin.time.measureTime

fun part1(input: List<String>): Int {
    val rows = input.size
    val cols = input[0].length
    val chars = input.joinToString("").toCharArray()
    var (px, py) = 0 to 0
    var (dx, dy) = 0 to -1
    outer@for (y in 0..<rows) {
        for (x in 0..<cols) {
            if (chars[y * rows + x] == '^') {
                px = x
                py = y
                break@outer
            }
        }
    }
    while (true) {
        val nx = px + dx
        val ny = py + dy
        if (ny !in 0..<rows || nx !in 0..<cols) {
            break
        }
        if (chars[ny * rows + nx] == '#') {
            val temp = dx
            dx = -dy
            dy = temp
            continue
        }
        chars[py * rows + px] = 'X'
        px = nx
        py = ny
    }
    return chars.count { it == 'X' } + 1
}

fun detectLoop(chars: CharArray, rows: Int, cols: Int,
               visited: MutableSet<Int>, spx: Int, spy: Int, sdx: Int, sdy: Int): Boolean {
    var (px, py) = spx to spy
    var (dx, dy) = sdx to sdy
    while (true) {
        val nx = px + dx
        val ny = py + dy
        if (ny !in 0..<rows || nx !in 0..<cols) {
            return false
        }
        if (chars[ny * rows + nx] == '#') {
            val temp = dx
            dx = -dy
            dy = temp
            continue
        }
        if (!visited.add(hash(rows, chars.size, px, py, dx, dy)))
            return true
        px = nx
        py = ny
    }
}

fun hash(rows: Int, size: Int, px: Int, py: Int, dx: Int, dy: Int) =
    (px * rows + py) + (dx * 2 + dy) * size

fun part2(input: List<String>): Int {
    val rows = input.size
    val cols = input[0].length
    val chars = input.joinToString("").toCharArray()
    val visited = mutableSetOf<Int>()
    var count = 0
    var (px, py) = 0 to 0
    var (dx, dy) = 0 to -1
    outer@for (y in 0..<rows) {
        for (x in 0..<cols) {
            if (chars[y * rows + x] == '^') {
                px = x
                py = y
                break@outer
            }
        }
    }
    while (true) {
        val nx = px + dx
        val ny = py + dy
        if (ny !in 0..<rows || nx !in 0..<cols) {
            break
        }
        if (chars[ny * rows + nx] == '#') {
            val temp = dx
            dx = -dy
            dy = temp
            continue
        }
        visited.add(hash(rows, chars.size, px, py, dx, dy))
        chars[py * rows + px] = 'X'
        if (chars[ny * rows + nx] != 'X') {
            chars[ny * rows + nx] = '#'
            if (detectLoop(chars, rows, cols, visited.toMutableSet(), px, py, -dy, dx)) {
                count++
            }
        }
        px = nx
        py = ny
    }
    return count
}

fun main() {
    // Read a test input from the `src/day06/Day06_test.txt` file:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 41)
//    check(part2(testInput) == 6)

    // Read the input from the `src/day06/Day06.txt` file:
    val input = readInput("day06/Day06")
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}
