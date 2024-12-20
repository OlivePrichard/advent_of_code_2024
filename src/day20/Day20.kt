package day20

import println
import readInput
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

data class Node(val x: Int, val y: Int) {
    fun neighbors(grid: List<List<Char>>, distance: Int) = listOf(
        0 to 1,
        1 to 0,
        0 to -1,
        -1 to 0
    ).mapNotNull { (dx, dy) ->
        val neighborX = x + dx
        val neighborY = y + dy
        if (neighborY in grid.indices && neighborX in grid[neighborY].indices && grid[neighborY][neighborX] == '.') {
            Node(neighborX, neighborY) to distance + 1
        } else {
            null
        }
    }
}

fun Pair<Int, Int>.neighbors(grid: List<List<Char>>, distances: List<List<Int?>>) = listOf(
    0 to 1,
    1 to 0,
    0 to -1,
    -1 to 0
).mapNotNull { (dx, dy) ->
    val neighborX = this.first + dx
    val neighborY = this.second + dy
    if (neighborY in grid.indices && neighborX in grid[neighborY].indices &&
        grid[neighborY][neighborX] == '.' && distances[neighborY][neighborX] == null) {
        neighborX to neighborY
    } else {
        null
    }
}

fun findStart(grid: List<List<Char>>): Node {
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] == 'S') {
                return Node(x, y)
            }
        }
    }
    throw IllegalStateException("No start found")
}

fun findEnd(grid: List<List<Char>>): Node {
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] == 'E') {
                return Node(x, y)
            }
        }
    }
    throw IllegalStateException("No end found")
}

fun heuristic(current: Node, target: Node) =
    (current.x - target.x).absoluteValue + (current.y - target.y).absoluteValue

fun pathLength(grid: List<List<Char>>, start: Node, end: Node): Int {
    val unvisitedSet = PriorityQueue<Pair<Node, Pair<Int, Int>>> { node1, node2 ->
        node1.second.second - node2.second.second
    }
    val seenSet = mutableSetOf(start)
    unvisitedSet.add(start to (0 to heuristic(start, end)))
    while (unvisitedSet.isNotEmpty()) {
        val node = unvisitedSet.remove()
        val neighbors = node.first.neighbors(grid, node.second.first)
        for (neighbor in neighbors) {
            if (neighbor.first in seenSet) {
                continue
            }
            seenSet.add(neighbor.first)
            unvisitedSet.add(neighbor.first to (neighbor.second to neighbor.second + heuristic(start, end)))
            if (end == neighbor.first) {
                return neighbor.second
            }
        }
    }
    throw IllegalStateException("No end found")
}

fun labelDistances(grid: List<List<Char>>, end: Pair<Int, Int>): List<List<Int?>> {
    val distances: List<MutableList<Int?>> = grid.map { it.map { null }.toMutableList() }
    distances[end.second][end.first] = 0
    var tiles = end.neighbors(grid, distances).toSet()
    var distance = 0
    while (tiles.isNotEmpty()) {
        distance++
        val nextTiles = mutableSetOf<Pair<Int, Int>>()
        for (tile in tiles) {
            distances[tile.second][tile.first] = distance
        }
        for (tile in tiles) {
            nextTiles.addAll(tile.neighbors(grid, distances))
        }
        tiles = nextTiles
    }
    return distances
}

fun findCheats(distances: List<List<Int?>>, start: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
    val xLower = max(1, start.first - length)
    val xUpper = min(distances[0].size - 2, start.first + length)
    val yLower = max(1, start.second - length)
    val yUpper = min(distances.size - 2, start.second + length)
    val target = Node(start.first, start.second)
    val cheats = mutableListOf<Pair<Int, Int>>()
    for (y in yLower..yUpper) {
        for (x in xLower..xUpper) {
            if (heuristic(Node(x, y), target) <= length && distances[y][x] != null) {
                cheats.add(x to y)
            }
        }
    }
    return cheats
}

fun part1(input: List<String>): Int {
    val grid = input.map { it.toCharArray().toMutableList() }
    val start = findStart(grid)
    val end = findEnd(grid)
    grid[start.y][start.x] = '.'
    grid[end.y][end.x] = '.'
    val normalLength = pathLength(grid, start, end)
    var count = 0
    for (row in 1 until grid.size - 1) {
        for (column in 1 until grid[0].size - 1) {
            if (grid[row][column] == '#' &&
                Node(column, row).neighbors(grid, 0).count { grid[it.first.y][it.first.x] == '.' } >= 2) {
                grid[row][column] = '.'
                if (pathLength(grid, start, end) + 100 <= normalLength) {
                    count++
                }
                grid[row][column] = '#'
            }
        }
    }
    return count
}

fun part2(input: List<String>): Int {
    val grid = input.map { it.toCharArray().toMutableList() }
    val start = findStart(grid)
    val end = findEnd(grid)
    grid[start.y][start.x] = '.'
    grid[end.y][end.x] = '.'
    val distances = labelDistances(grid, end.x to end.y)
    var count = 0
    for (yStart in 1 until grid.size - 1) {
        for (xStart in 1 until grid[0].size - 1) {
            val startDistance = distances[yStart][xStart] ?: continue
            for ((xEnd, yEnd) in findCheats(distances, xStart to yStart, 20)) {
                val endDistance = distances[yEnd][xEnd]!!
                val cheatDistance = heuristic(Node(xStart, yStart), Node(xEnd, yEnd))
                val timeSaved = startDistance - endDistance - cheatDistance
                if (timeSaved >= 100) count++
            }
        }
    }
    return count
}

fun main() {
    // Read a test input from the `src/day20/Day20_test.txt` file:
    val testInput = readInput("day20/Day20_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    // Read the input from the `src/day20/Day20.txt` file:
    val input = readInput("day20/Day20")
    part1(input).println()
    part2(input).println()
}
