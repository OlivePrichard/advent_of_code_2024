package day12

import println
import readInput

fun floodFill(grid: List<List<Char>>,
              filled: List<MutableList<Boolean>>, start: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val char = grid[start.first][start.second]
    val fillSet = mutableSetOf<Pair<Int, Int>>()
    val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
    val adjacentSet = mutableSetOf(start)
    while (adjacentSet.size > 0) {
        val tile = adjacentSet.first()
        adjacentSet.remove(tile)
        fillSet.add(tile)
        filled[tile.first][tile.second] = true
        for (dir in directions) {
            val newTile = tile.first + dir.first to tile.second + dir.second
            if (newTile.first in grid.indices && newTile.second in grid[0].indices &&
                !filled[newTile.first][newTile.second] && grid[newTile.first][newTile.second] == char) {
                adjacentSet.add(newTile)
            }
        }
    }
    return fillSet
}

fun cost(patch: Set<Pair<Int, Int>>): Int {
    val area = patch.size
    var perimeter = 0
    val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
    for (tile in patch) {
        for (dir in directions) {
            val newTile = tile.first + dir.first to tile.second + dir.second
            if (newTile !in patch) {
                perimeter++
            }
        }
    }
    return area * perimeter
}

fun bulkCost(patch: Set<Pair<Int, Int>>): Int {
    val area = patch.size
    val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
    val edges = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    for (tile in patch) {
        for (dir in directions) {
            val newTile = tile.first + dir.first to tile.second + dir.second
            if (newTile !in patch) {
                edges.add(tile to dir)
            }
        }
    }
    var edgeCount = 0
    while (edges.size > 0) {
        val edge = edges.first()
        edgeCount++
        edges.remove(edge)
        val tile = edge.first
        val dir = edge.second
        val parallel = dir.second to -dir.first
        var nextTile = tile.first + parallel.first to tile.second + parallel.second
        while (nextTile to dir in edges) {
            edges.remove(nextTile to dir)
            nextTile = nextTile.first + parallel.first to nextTile.second + parallel.second
        }
        nextTile = tile.first - parallel.first to tile.second - parallel.second
        while (nextTile to dir in edges) {
            edges.remove(nextTile to dir)
            nextTile = nextTile.first - parallel.first to nextTile.second - parallel.second
        }
    }
    return edgeCount * area
}

fun part1(input: List<String>): Int {
    val grid = input.map { it.toList() }
    val filled = List(grid.size) { MutableList(grid[0].size) { false } }
    var total = 0
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (!filled[i][j]) {
                total += cost(floodFill(grid, filled, i to j))
            }
        }
    }
    return total
}

fun part2(input: List<String>): Int {
    val grid = input.map { it.toList() }
    val filled = List(grid.size) { MutableList(grid[0].size) { false } }
    var total = 0
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (!filled[i][j]) {
                total += bulkCost(floodFill(grid, filled, i to j))
            }
        }
    }
    return total
}

fun main() {
    // Read a test input from the `src/day12/Day12_test.txt` file:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 140)
    check(part2(testInput) == 80)

    // Read the input from the `src/day12/Day12.txt` file:
    val input = readInput("day12/Day12")
    part1(input).println()
    part2(input).println()
}
