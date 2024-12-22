package day22

import println
import readInput

fun nextSecret(secret: Int): Int {
    return secret xor (secret shr 5) xor
            ((secret shl 1)  and 0x07_FF_FE) xor
            ((secret shl 6)  and 0x00_07_C0) xor
            ((secret shl 11) and 0xFF_F8_00) xor
            ((secret shl 12) and 0xFF_F0_00) xor
            ((secret shl 17) and 0xFE_00_00)
}

fun hash(diffs: List<Int>) = diffs[0] + diffs[1] * 20 + diffs[2] * 400 + diffs[3] * 8000

fun getSequencePrices(prices: List<Int>): Map<Int, Int> {
    val sellPoints = mutableMapOf<Int, Int>()
    for ((changes, price) in
        prices.windowed(2) { (first, second) -> second - first }.windowed(4).zip(prices.drop(4))) {
        val h = hash(changes)
        if (h !in sellPoints) {
            sellPoints[h] = price
        }
    }
    return sellPoints
}

fun part1(input: List<String>): Long {
    return input.sumOf {
        var num = it.toInt()
        for (i in 0 until 2000) {
            num = nextSecret(num)
        }
        num.toLong()
    }
}

fun part2(input: List<String>): Int {
    val sequenceProfits = mutableMapOf<Int, Int>()
    for (line in input) {
        var num = line.toInt()
        val prices = mutableListOf(num % 10)
        for (i in 0 until 2000) {
            num = nextSecret(num)
            prices.add(num % 10)
        }
        for (entry in getSequencePrices(prices).entries) {
            sequenceProfits[entry.key] = (sequenceProfits[entry.key] ?: 0) + entry.value
        }
    }
    return sequenceProfits.values.max()
}

fun main() {
    // Read a test input from the `src/day22/Day22_test.txt` file:
    val testInput = readInput("day22/Day22_test")
//    check(part1(testInput) == 37327623L)
    check(part2(testInput) == 23)

    // Read the input from the `src/day22/Day22.txt` file:
    val input = readInput("day22/Day22")
    part1(input).println()
    part2(input).println()
}
