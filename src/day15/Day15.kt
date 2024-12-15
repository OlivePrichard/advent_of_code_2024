package day15

import println
import readInput

fun move(grid: List<MutableList<Char>>, i: Int, j: Int, di: Int, dj: Int): Boolean {
    val (iNew, jNew) = i + di to j + dj
    when (grid[iNew][jNew]) {
        '#' -> return false
        '.' -> {
            grid[iNew][jNew] = grid[i][j]
            grid[i][j] = '.'
            return true
        }
        else -> {
            if (move(grid, iNew, jNew, di, dj)) {
                grid[iNew][jNew] = grid[i][j]
                grid[i][j] = '.'
                return true
            }
            return false
        }
    }
}

fun wideMoveVerticalTest(grid: List<MutableList<Char>>, i: Int, j: Int, di: Int): Boolean {
    return when (grid[i + di][j]) {
        '#' -> false
        '.' -> true
        '[' -> wideMoveVerticalTest(grid, i + di, j, di) &&
                (grid[i + di * 2][j] == '[' || wideMoveVerticalTest(grid, i + di, j + 1, di))
        ']' -> wideMoveVerticalTest(grid, i + di, j - 1, di) &&
                (grid[i + di * 2][j - 1] == '[' || wideMoveVerticalTest(grid, i + di, j, di))
        else -> throw IllegalStateException("Unexpected character in grid: ${grid[i + di][j]}")
    }
}

fun doVerticalWideMove(grid: List<MutableList<Char>>, i: Int, j: Int, di: Int) {
    when (grid[i][j]) {
        '.' -> return
        '#' -> throw IllegalStateException("Tried to move into a wall at $i, $j: $grid")
        '@' -> {
            doVerticalWideMove(grid, i + di, j, di)
            grid[i][j] = '.'
            grid[i + di][j] = '@'
        }
        '[' -> {
            doVerticalWideMove(grid, i + di, j, di)
            doVerticalWideMove(grid, i + di, j + 1, di)
            grid[i][j] = '.'
            grid[i][j + 1] = '.'
            grid[i + di][j] = '['
            grid[i + di][j + 1] = ']'
        }
        ']' -> {
            doVerticalWideMove(grid, i + di, j - 1, di)
            doVerticalWideMove(grid, i + di, j, di)
            grid[i][j - 1] = '.'
            grid[i][j] = '.'
            grid[i + di][j - 1] = '['
            grid[i + di][j] = ']'
        }
    }
}

fun gps(grid: List<List<Char>>): Int {
    var acc = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] in "O[") {
                acc += i * 100 + j
            }
        }
    }
    return acc
}

fun findRobot(grid: List<List<Char>>): Pair<Int, Int> {
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '@') {
                return i to j
            }
        }
    }
    throw IllegalStateException("No robot found")
}

fun part1(input: List<String>): Int {
    val split = input.indexOf("")
    val grid = input.subList(0, split).map {
        it.toCharArray().toMutableList()
    }
    val instructions = input.drop(split + 1).joinToString("").toCharArray()
    var (iRobot, jRobot) = findRobot(grid)
    for (instruction in instructions) {
        val (di, dj) = when (instruction) {
            '>' -> 0 to 1
            '<' -> 0 to -1
            '^' -> -1 to 0
            'v' -> 1 to 0
            else -> throw IllegalStateException("Unexpected character $instruction")
        }
        if (move(grid, iRobot, jRobot, di, dj)) {
            iRobot += di
            jRobot += dj
        }
    }
    return gps(grid)
}

fun part2(input: List<String>): Int {
    val split = input.indexOf("")
    val grid = input.subList(0, split).map {
        it.toCharArray().flatMap { c ->
            when (c) {
                '#' -> "##"
                '.' -> ".."
                'O' -> "[]"
                '@' -> "@."
                else -> throw IllegalStateException("Unexpected character $it")
            }.toCharArray().toList()
        }.toMutableList()
    }
    val instructions = input.drop(split + 1).joinToString("").toCharArray()
    var (iRobot, jRobot) = findRobot(grid)
    for (instruction in instructions) {
        val (di, dj) = when (instruction) {
            '>' -> 0 to 1
            '<' -> 0 to -1
            '^' -> -1 to 0
            'v' -> 1 to 0
            else -> throw IllegalStateException("Unexpected character $instruction")
        }
        if (di == 0) {
            if (move(grid, iRobot, jRobot, di, dj)) {
                iRobot += di
                jRobot += dj
            }
        } else {
            if (wideMoveVerticalTest(grid, iRobot, jRobot, di)) {
                doVerticalWideMove(grid, iRobot, jRobot, di)
                iRobot += di
                jRobot += dj
            }
        }
    }
    return gps(grid)
}

fun main() {
    // Read a test input from the `src/day15/Day15_test.txt` file:
    val testInput = readInput("day15/Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    // Read the input from the `src/day15/Day15.txt` file:
    val input = readInput("day15/Day15")
    part1(input).println()
    part2(input).println()
}
