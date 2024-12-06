package day06

import println
import readInput

fun part1(input: List<String>): Int {
    val grid = input.map { it.toCharArray().toMutableList() }
    var position = Pair(0, 0)
    var direction = Pair(0, -1)
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (grid[y][x] == '^') {
                position = Pair(x, y)
            }
        }
    }
    while (position.second in grid.indices && position.first in grid[position.second].indices) {
        grid[position.second][position.first] = 'X'
        val nextPosition = Pair(position.first + direction.first, position.second + direction.second)
        if (nextPosition.second !in grid.indices || nextPosition.first !in grid[nextPosition.second].indices) {
            break
        }
        if (grid[nextPosition.second][nextPosition.first] == '#') {
            direction = Pair(-direction.second, direction.first)
            continue
        }
        position = nextPosition
    }
    return grid.sumOf { row -> row.count { it == 'X' } }
}

fun detectLoop(grid: List<List<Char>>, start: Pair<Int, Int>): Boolean {
    val stateSet = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    var position = start
    var direction = Pair(0, -1)
    while (position.second in grid.indices && position.first in grid[position.second].indices) {
        if (!stateSet.add(Pair(position, direction))) {
            return true
        }
        val nextPosition = Pair(position.first + direction.first, position.second + direction.second)
        if (nextPosition.second !in grid.indices || nextPosition.first !in grid[nextPosition.second].indices) {
            break
        }
        if (grid[nextPosition.second][nextPosition.first] == '#') {
            direction = Pair(-direction.second, direction.first)
            continue
        }
        position = nextPosition
    }
    return false
}

fun part2(input: List<String>): Int {
    val grid = input.map { it.toCharArray().toMutableList() }
    var position = Pair(0, 0)
    var direction = Pair(0, -1)
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (grid[y][x] == '^') {
                position = Pair(x, y)
            }
        }
    }
    val startingPosition = position.first to position.second
    while (position.second in grid.indices && position.first in grid[position.second].indices) {
        grid[position.second][position.first] = 'X'
        val nextPosition = Pair(position.first + direction.first, position.second + direction.second)
        if (nextPosition.second !in grid.indices || nextPosition.first !in grid[nextPosition.second].indices) {
            break
        }
        if (grid[nextPosition.second][nextPosition.first] == '#') {
            direction = Pair(-direction.second, direction.first)
            continue
        }
        position = nextPosition
    }
    grid[startingPosition.second][startingPosition.first] = '^'
    var count = 0
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (grid[y][x] == 'X') {
                grid[y][x] = '#'
                if (detectLoop(grid, startingPosition)) {
                    count++
                }
                grid[y][x] = 'X'
            }
        }
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
