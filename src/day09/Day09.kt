package day09

import println
import readInput
import kotlin.time.measureTime

fun setupBlocks(input: String): Pair<IntArray, Pair<Int, Int>> {
    val digits = input.toCharArray().map { it.toString().toByte() }
    val blocks = IntArray(digits.size * 5 + 10) { -1 }
    var idx = 0
    var isFile = true
    var id = 0
    for (digit in digits) {
        if (isFile) {
            for (i in idx until idx + digit) {
                blocks[i] = id
            }
            id++
        }
        idx += digit
        isFile = !isFile
    }
    return blocks to (idx to id - 1)
}

fun part1(input: List<String>): Long {
    val (blocks, ptrs) = setupBlocks(input[0])
    var (idx, _) = ptrs
    var front = 0
    while (front < idx) {
        if (blocks[front] != -1) {
            front++
            continue
        }
        if (blocks[idx] == -1) {
            idx--
            continue
        }
        blocks[front] = blocks[idx]
        blocks[idx] = -1
        front++
        idx--
    }
    return blocks.withIndex().sumOf { (i, id) -> if (id == -1) 0L else i.toLong() * id }
}

fun findFreeSector(blocks: IntArray, start: Int, end: Int, size: Int): Int {
    var idx = start
    var freeCount = 0
    while (idx < end) {
        idx++
        if (blocks[idx] == -1) {
            freeCount++
            if (freeCount == size) {
                return idx - size + 1
            }
        } else {
            freeCount = 0
        }
    }
    return -1
}

fun findFile(blocks: IntArray, start: Int, fileId: Int): Pair<Int, Int> {
    var idx = start
    while (blocks[idx] != fileId) {
        idx--
        if (idx == -1) return -1 to -1
    }
    val end = idx + 1
    while (blocks[idx] == fileId) {
        idx--
        if (idx == -1) break
    }
    idx++
    return idx to end - idx
}

fun part2(input: List<String>): Long {
    val (blocks, ptrs) = setupBlocks(input[0])
    var (idx, id) = ptrs
    val freeSectors = (1..9).map { findFreeSector(blocks, 0, idx, it) }.toMutableList()
    while (id > 0) {
        val (start, size) = findFile(blocks, idx, id)
        idx = start
        if (start == -1) throw Exception("Something went wrong")
        val freeSector = freeSectors[size - 1]
        if (freeSector != -1 && freeSector < start) {
            for (i in 0 until size) {
                blocks[freeSector + i] = blocks[start + i]
                blocks[start + i] = -1
            }
            for (i in freeSectors.indices) {
                if (freeSectors[i] == freeSector) {
                    freeSectors[i] = findFreeSector(blocks, freeSector, idx, i + 1)
                }
            }
        }
        id--
    }
    return blocks.withIndex().sumOf { (i, id) -> if (id == -1) 0L else i.toLong() * id }
}

fun main() {
    // Read a test input from the `src/day09/Day09_test.txt` file:
    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/day09/Day09.txt` file:
    val input = readInput("day09/Day09")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}
