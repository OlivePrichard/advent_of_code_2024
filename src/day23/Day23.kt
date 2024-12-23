package day23

import println
import readInput

fun Set<String>.pairs(): Set<Pair<String, String>> {
    val set = mutableSetOf<Pair<String, String>>()
    for (first in this) {
        for (second in this) {
            if (first == second) continue
            if (first < second) {
                set.add(first to second)
            } else {
                set.add(second to first)
            }
        }
    }
    return set
}

fun getNeighborMap(input: List<String>): Map<String, Set<String>> {
    val neighbors = mutableMapOf<String, MutableSet<String>>()
    for (line in input) {
        val (first, second) = line.split("-")
        if (first !in neighbors) {
            neighbors[first] = mutableSetOf()
        }
        neighbors[first]!!.add(second)
        if (second !in neighbors) {
            neighbors[second] = mutableSetOf()
        }
        neighbors[second]!!.add(first)
    }
    return neighbors
}

fun bronKerbosch(graph: Map<String, Set<String>>, cliques: MutableSet<Set<String>>,
                 r: Set<String>, p: MutableSet<String>, x: MutableSet<String>) {
    if (p.isEmpty() && x.isEmpty()) {
        cliques.add(r)
        return
    }
    for (v in p.toSet()) {
        val n = graph[v]!!
        bronKerbosch(graph, cliques, r + v, p.intersect(n).toMutableSet(), x.intersect(n).toMutableSet())
        p.remove(v)
        x.add(v)
    }
}

fun part1(input: List<String>): Int {
    val neighbors = getNeighborMap(input)
    val groups = mutableSetOf<Set<String>>()
    for (key in neighbors.keys) {
        if (!key.startsWith('t')) continue
        for (pair in neighbors[key]!!.pairs()) {
            if (pair.first in neighbors[pair.second]!!) {
                groups.add(setOf(pair.first, pair.second, key))
            }
        }
    }
    return groups.size
}

fun part2(input: List<String>): String {
    val neighbors = getNeighborMap(input)
    val groups = mutableSetOf<Set<String>>()
    bronKerbosch(neighbors, groups, setOf(), neighbors.keys.toMutableSet(), mutableSetOf())
    val largest = groups.maxBy { it.size }
    return largest.sorted().joinToString(",")
}

fun main() {
    // Read a test input from the `src/day23/Day23_test.txt` file:
    val testInput = readInput("day23/Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    // Read the input from the `src/day23/Day23.txt` file:
    val input = readInput("day23/Day23")
    part1(input).println()
    part2(input).println()
}
