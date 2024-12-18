package day18

import println
import readInput
import java.util.*

data class Node(val x: Int, val y: Int) {
    fun neighbors(grid: List<List<Boolean>>, distance: Int) = listOf(
            0 to 1,
            1 to 0,
            0 to -1,
            -1 to 0
        ).mapNotNull { (dx, dy) ->
            val neighborX = x + dx
            val neighborY = y + dy
            if (neighborY in grid.indices && neighborX in grid[neighborY].indices && grid[neighborY][neighborX]) {
                Node(neighborX, neighborY) to distance + 1
            } else {
                null
            }
        }
}

fun part1(input: List<String>): Int {
    val gridSize = if (input.size < 50) 7 else 71
    val time = if (input.size < 50) 12 else 1024
    val grid = List(gridSize) { MutableList(gridSize) { true } }
    val coordinates = input.map { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        x to y
    }
    for ((x, y) in coordinates.subList(0, time)) {
        grid[y][x] = false
    }
    val start = Node(0, 0)
    val unvisitedSet = PriorityQueue<Pair<Node, Int>> { node1, node2 -> node1.second - node2.second }
    val seenSet = mutableSetOf(start)
    unvisitedSet.add(start to 0)
    val end = gridSize - 1 to gridSize - 1
    while (unvisitedSet.isNotEmpty()) {
        val node = unvisitedSet.remove()
        val neighbors = node.first.neighbors(grid, node.second)
        for (neighbor in neighbors) {
            if (neighbor.first in seenSet) {
                continue
            }
            seenSet.add(neighbor.first)
            unvisitedSet.add(neighbor)
            if (end == neighbor.first.x to neighbor.first.y) {
                return neighbor.second
            }
        }
    }
    throw IllegalStateException("No end found")
}

fun part2(input: List<String>): String {
    val gridSize = if (input.size < 50) 7 else 71
    val grid = List(gridSize) { MutableList(gridSize) { true } }
    val coordinates = input.map { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        x to y
    }
    outer@for ((x, y) in coordinates) {
        grid[y][x] = false
        val start = Node(0, 0)
        val unvisitedSet = PriorityQueue<Pair<Node, Int>> { node1, node2 -> node1.second - node2.second }
        val seenSet = mutableSetOf(start)
        unvisitedSet.add(start to 0)
        val end = gridSize - 1 to gridSize - 1
        while (unvisitedSet.isNotEmpty()) {
            val node = unvisitedSet.remove()
            val neighbors = node.first.neighbors(grid, node.second)
            for (neighbor in neighbors) {
                if (neighbor.first in seenSet) {
                    continue
                }
                seenSet.add(neighbor.first)
                unvisitedSet.add(neighbor)
                if (end == neighbor.first.x to neighbor.first.y) {
                    continue@outer
                }
            }
        }
        return "$x,$y"
    }
    throw IllegalStateException("No blocking byte found")
}

fun main() {
    // Read a test input from the `src/day18/Day18_test.txt` file:
    val testInput = readInput("day18/Day18_test")
    check(part1(testInput) == 22)
    check(part2(testInput) == "6,1")

    // Read the input from the `src/day18/Day18.txt` file:
    val input = readInput("day18/Day18")
    part1(input).println()
    part2(input).println()
}
