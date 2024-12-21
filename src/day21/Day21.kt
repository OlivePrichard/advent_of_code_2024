package day21

import println
import readInput
import kotlin.math.absoluteValue

fun<T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>) = first + pair.first to second + pair.second

fun permutations(vals: MutableList<Char>): MutableSet<String> {
    if (vals.isEmpty()) { return mutableSetOf() }
    if (vals.size == 1) return mutableSetOf(vals[0].toString())
    val perms = mutableSetOf<String>()
    for (i in vals.indices) {
        vals.swap(i, 0)
        perms.addAll(permutations(vals.subList(1, vals.size)).map { vals[0] + it })
        vals.swap(i, 0)
    }
    return perms
}

fun getSequences(keys: List<List<Char>>, start: Pair<Int, Int>, target: Pair<Int, Int>): Set<String> {
    val horizontal = (if (start.first > target.first) "<" else ">").repeat((start.first - target.first).absoluteValue)
    val vertical = (if (start.second > target.second) "^" else "v").repeat((start.second - target.second).absoluteValue)
    return permutations((horizontal + vertical).toCharArray().toMutableList()).mapNotNull {
        var pos = start.first to start.second
        for (movement in it) {
            pos += when (movement) {
                '>' -> 1 to 0
                '<' -> -1 to 0
                'v' -> 0 to 1
                '^' -> 0 to -1
                else -> error("Unexpected movement: $movement")
            }
            if (keys[pos.second][pos.first] == ' ') return@mapNotNull null
        }
        it + 'A'
    }.toSet().ifEmpty { setOf("A") }
}

fun find(pad: List<List<Char>>, c: Char) = pad.indexOfFirst { it.contains(c) }.let { pad[it].indexOf(c) to it }

fun strokes(pads: List<List<List<Char>>>, keys: String, mem: MutableMap<Pair<Int, String>, Long>): Long {
    if (pads.isEmpty()) return keys.length.toLong()
    val lookup = pads.size to keys
    mem[lookup]?.let { return it }
    var pos = find(pads[0], 'A')
    var sequenceLength = 0L
    for (key in keys) {
        val nextPos = find(pads[0], key)
        sequenceLength += getSequences(pads[0], pos, nextPos).minOf {
            strokes(pads.subList(1, pads.size), it, mem)
        }
        pos = nextPos
    }
    mem[lookup] = sequenceLength
    return sequenceLength
}

fun part1(input: List<String>): Long {
    val keyPad = listOf(
        "789",
        "456",
        "123",
        " 0A"
    ).map { it.toCharArray().toList() }
    val arrowPad = listOf(
        " ^A",
        "<v>"
    ).map { it.toCharArray().toList() }
    return input.sumOf {
        val numericPart = it.substring(0, it.length - 1).toLong()
        val keyStrokes = strokes(listOf(
            keyPad,
            arrowPad,
            arrowPad
        ), it, mutableMapOf())
        numericPart * keyStrokes
    }
}

fun part2(input: List<String>): Long {
    val keyPad = listOf(
        "789",
        "456",
        "123",
        " 0A"
    ).map { it.toCharArray().toList() }
    val arrowPad = listOf(
        " ^A",
        "<v>"
    ).map { it.toCharArray().toList() }
    return input.sumOf {
        val numericPart = it.substring(0, it.length - 1).toLong()
        val keyStrokes = strokes((0..25).map { i ->
            if (i == 0) keyPad else arrowPad
        }, it, mutableMapOf())
        numericPart * keyStrokes
    }
}

fun main() {
    // Read a test input from the `src/day21/Day21_test.txt` file:
    val testInput = readInput("day21/Day21_test")
    check(part1(testInput) == 126384L)
//    check(part2(testInput) == testInput.size)

    // Read the input from the `src/day21/Day21.txt` file:
    val input = readInput("day21/Day21")
    part1(input).println()
    part2(input).println()
}
