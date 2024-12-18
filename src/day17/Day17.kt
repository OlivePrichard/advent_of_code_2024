package day17

import println
import readInput

fun combo(operand: Long, regA: Long, regB: Long, regC: Long) = when (operand) {
    in 0..3 -> operand
    4L -> regA
    5L -> regB
    6L -> regC
    else -> throw IllegalArgumentException("Invalid operand: $operand")
}

fun getBits(register: Long, output: List<Int>, operation: (Long) -> Int): Long? {
    if (output.isEmpty()) return register
    val target = output.last()
    val next = output.subList(0, output.size - 1)
    for (i in 0..7) {
        val nextRegister = (register shl 3) or i.toLong()
        if (operation(nextRegister) == target) {
            return getBits(nextRegister, next, operation) ?: continue
        }
    }
    return null
}

fun part1(input: List<String>): String {
    val getReg = Regex("""^Register .: (\d+)$""")
    val getInstr = Regex("""^Program: (.+)$""")
    var regA = getReg.matchEntire(input[0])?.groupValues?.get(1)?.toLong()!!
    var regB = getReg.matchEntire(input[1])?.groupValues?.get(1)?.toLong()!!
    var regC = getReg.matchEntire(input[2])?.groupValues?.get(1)?.toLong()!!
    var ip = 0
    val outputs = mutableListOf<Int>()
    val program = getInstr
        .matchEntire(input[4])
        ?.groupValues
        ?.get(1)
        ?.split(",")
        ?.map { it.toInt() }
        ?: emptyList()

    while (ip < program.size) {
        val instruction = program[ip]
        val operand = program[ip + 1]
        when (instruction) {
            0 -> regA = regA shr combo(operand.toLong(), regA, regB, regC).toInt()
            1 -> regB = regB xor operand.toLong()
            2 -> regB = combo(operand.toLong(), regA, regB, regC) % 8
            3 -> if (regA != 0L) ip = operand - 2
            4 -> regB = regB xor regC
            5 -> outputs.add((combo(operand.toLong(), regA, regB, regC) % 8).toInt())
            6 -> regB = regA shr combo(operand.toLong(), regA, regB, regC).toInt()
            7 -> regC = regA shr combo(operand.toLong(), regA, regB, regC).toInt()
            else -> throw IllegalArgumentException("Invalid instruction: $instruction")
        }
        ip += 2
    }
    return outputs.joinToString(",")
}

fun part2(input: List<String>): Long {
    val getInstr = Regex("""^Program: (.+)$""")
    val program = getInstr
        .matchEntire(input[4])
        ?.groupValues
        ?.get(1)
        ?.split(",")
        ?.map { it.toInt() }
        ?: emptyList()
    val instructions = (0 until program.size / 2 - 1).map { program[it * 2] to program[it * 2 + 1] }
    return getBits(0, program) {
            var regA = it
            var regB = 0L
            var regC = 0L
            for ((instruction, operand) in instructions) {
                when (instruction) {
                    0 -> regA = regA shr combo(operand.toLong(), regA, regB, regC).toInt()
                    1 -> regB = regB xor operand.toLong()
                    2 -> regB = combo(operand.toLong(), regA, regB, regC) % 8
                    4 -> regB = regB xor regC
                    5 -> return@getBits if (regA == it shr 3)
                            (combo(operand.toLong(), regA, regB, regC) % 8).toInt()
                        else
                            throw IllegalStateException("Invalid register value")
                    6 -> regB = regA shr combo(operand.toLong(), regA, regB, regC).toInt()
                    7 -> regC = regA shr combo(operand.toLong(), regA, regB, regC).toInt()
                    else -> throw IllegalArgumentException("Invalid instruction: $instruction")
                }
            }
            throw IllegalStateException("Didn't return properly")
        } ?: throw IllegalStateException("No output found")
}

fun main() {
    // Read a test input from the `src/day17/Day17_test.txt` file:
//    val testInput = readInput("day17/Day17_test")
//    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
//    check(part2(testInput) == 117440L)

    // Read the input from the `src/day17/Day17.txt` file:
    val input = readInput("day17/Day17")
    part1(input).println()
    part2(input).println()
}
