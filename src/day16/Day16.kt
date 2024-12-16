package day16

import println
import readInput
import java.util.*

data class Node(val x: Int, val y: Int, val dx: Int, val dy: Int) {
    fun neighbors(grid: List<List<Char>>, distance: Int): List<Pair<Node, Int>> {
        if (dx == 0 && dy == 0) return listOf()
        val forward = Node(x + dx, y + dy, dx, dy)
        val turnA = Node(x, y, dy, -dx)
        val turnB = Node(x, y, -dy, dx)
        if (grid[forward.y][forward.x] == '#') {
            return listOf(turnA to distance + 1000, turnB to distance + 1000)
        }
        return listOf(turnA to distance + 1000, turnB to distance + 1000, forward to distance + 1)
    }
}

fun findStart(grid: List<List<Char>>): Node {
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] == 'S') {
                return Node(x, y, 1, 0)
            }
        }
    }
    throw IllegalStateException("No start found")
}

fun findEnd(grid: List<List<Char>>): Pair<Int, Int> {
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] == 'E') {
                return x to y
            }
        }
    }
    throw IllegalStateException("No end found")
}

fun paintPaths(visited: MutableSet<Node>, map: Map<Node, Pair<Int, List<Node>>>, current: Node): Set<Node> {
    if (current in visited) return visited
    visited.add(current)
    for (node in map[current]!!.second) {
        paintPaths(visited, map, node)
    }
    return visited
}

fun part1(input: List<String>): Int {
    val grid = input.map {
        it.toCharArray().toList()
    }
    val unvisitedSet = PriorityQueue<Pair<Node, Int>> { node1, node2 -> node1.second - node2.second }
    val start = findStart(grid)
    val seenSet = mutableSetOf(start)
    unvisitedSet.add(start to 0)
    val end = findEnd(grid)
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

fun part2(input: List<String>): Int {
    val grid = input.map {
        it.toCharArray().toMutableList()
    }
    val unvisitedSet = PriorityQueue<Pair<Node, Int>> { node1, node2 -> node1.second - node2.second }
    val start = findStart(grid)
    val seenSet = mutableMapOf(start to (0 to mutableListOf<Node>()))
    unvisitedSet.add(start to 0)
    val end = findEnd(grid)
    while (unvisitedSet.isNotEmpty()) {
        val node = unvisitedSet.remove()
        val neighbors = node.first.neighbors(grid, node.second)
        for (neighbor in neighbors) {
            val first = if (end == neighbor.first.x to neighbor.first.y) {
                Node(neighbor.first.x, neighbor.first.y, 0, 0)
            } else {
                neighbor.first
            }
            if (first in seenSet) {
                val seen = seenSet[first]!!
                if (neighbor.second == seen.first) {
                    seen.second.add(node.first)
                }
                continue
            }
            seenSet[first] = neighbor.second to mutableListOf(node.first)
            if (first.dx != 0 || first.dy != 0) {
                unvisitedSet.add(neighbor)
            }
        }
    }
    val visited = paintPaths(mutableSetOf(), seenSet, Node(end.first, end.second, 0, 0))
    for (node in visited) {
        grid[node.y][node.x] = 'O'
    }
    return grid.sumOf { line -> line.count { it == 'O' } }
}

fun main() {
    // Read a test input from the `src/day16/Day16_test.txt` file:
    val testInput = readInput("day16/Day16_test")
    check(part1(testInput) == 7036)
    check(part2(testInput) == 45)

    // Read the input from the `src/day16/Day16.txt` file:
    val input = readInput("day16/Day16")
    part1(input).println()
    part2(input).println()
}
