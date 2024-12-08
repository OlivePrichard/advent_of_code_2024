package day06

import println
import readInput
import kotlin.time.measureTime

inline fun runSimulation(chars: CharArray, rows: Int, cols: Int,
                         px: Int, py: Int, dx: Int, dy: Int,
                         func: (Int, Int, Int, Int, Int, Int) -> Boolean): Boolean {
    var (px, py) = px to py
    var (dx, dy) = dx to dy
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
        if (func(px, py, dx, dy, nx, ny)) {
            return true
        }
        px = nx
        py = ny
    }
}

inline fun simulate(input: List<String>,
                    func: (Int, Int, Int, Int, Int, Int, CharArray, Int, Int) -> Boolean) {
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
    runSimulation(chars, rows, cols, px, py, dx, dy) {
            px, py, dx, dy, nx, ny ->
        func(px, py, dx, dy, nx, ny, chars, rows, cols)
    }
}

fun detectLoop(chars: CharArray, rows: Int, cols: Int,
               visited: MutableSet<Int>, px: Int, py: Int, dx: Int, dy: Int) =
    runSimulation(chars, rows, cols, px, py, dx, dy) {
            px, py, dx, dy, _, _ ->
        !visited.add(hash(rows, chars.size, px, py, dx, dy))
    }

fun hash(rows: Int, size: Int, px: Int, py: Int, dx: Int, dy: Int) =
    (px * rows + py) + (dx * 2 + dy) * size

fun part1(input: List<String>): Int {
    var count = 1

    simulate(input) {
        px, py, _, _, _, _, chars, rows, _ ->
        if (chars[py * rows + px] != 'X') {
            count++
            chars[py * rows + px] = 'X'
        }
        false
    }

    return count
}

fun part2(input: List<String>): Int {
    val visited = mutableSetOf<Int>()
    var count = 0

    simulate(input) {
        px, py, dx, dy, nx, ny, chars, rows, cols ->
        visited.add(hash(rows, chars.size, px, py, dx, dy))
        chars[py * rows + px] = 'X'
        if (chars[ny * rows + nx] != 'X') {
            chars[ny * rows + nx] = '#'
            if (detectLoop(chars, rows, cols, visited.toMutableSet(), px, py, -dy, dx)) {
                count++
            }
        }
        false
    }

    return count
}

fun main() {
    // Read a test input from the `src/day06/Day06_test.txt` file:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/day06/Day06.txt` file:
    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}
