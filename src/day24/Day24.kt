package day24

import println
import readInput

data class Gate(val operation: String, val inputs: Pair<String, String>, val output: String) {
    fun evaluate(inputValues: Pair<Boolean, Boolean>) = when(operation) {
        "AND" -> inputValues.first && inputValues.second
        "OR" -> inputValues.first || inputValues.second
        "XOR" -> inputValues.first xor inputValues.second
        else -> throw IllegalArgumentException("Invalid operation")
    }
}

fun kahnsAlgorithm(graph: Map<Gate, Pair<MutableSet<Gate>, Set<Gate>>>): List<Gate> {
    val s = graph
        .mapNotNull { (gate, connections) -> if (connections.first.isEmpty()) gate else null }
        .toMutableList()
    val sorted = mutableListOf<Gate>()
    while (s.isNotEmpty()) {
        val n = s.removeFirst()
        sorted.add(n)
        for (m in graph[n]!!.second) {
            graph[m]!!.first.remove(n)
            if (graph[m]!!.first.isEmpty()) {
                s.add(m)
            }
        }
    }
    return sorted
}

fun part1(input: List<String>): Long {
    val (initialWires, gateLines) = input
        .joinToString("\n")
        .split("\n\n")
        .map { it.split("\n") }
    val gateRegex = Regex("""^(...) (AND|OR|XOR) (...) -> (...)$""")
    val gates = gateLines.map {
        val match = gateRegex.matchEntire(it)!!
        Gate(match.groupValues[2], match.groupValues[1] to match.groupValues[3], match.groupValues[4])
    }
    val wireRegex = Regex("""^(...): ([10])$""")
    val wires = initialWires.associate {
        val match = wireRegex.matchEntire(it)!!
        match.groupValues[1] to (match.groupValues[2].toInt() == 1)
    }.toMutableMap()
    val predecessors = mutableMapOf<String, Gate>()
    for (gate in gates) {
        predecessors[gate.output] = gate
    }
    val gateGraph = mutableMapOf<Gate, Pair<MutableSet<Gate>, MutableSet<Gate>>>()
    for (gate in gates) {
        val earlier = mutableSetOf<Gate>()
        if (predecessors.containsKey(gate.inputs.first)) {
            earlier.add(predecessors[gate.inputs.first]!!)
        }
        if (predecessors.containsKey(gate.inputs.second)) {
            earlier.add(predecessors[gate.inputs.second]!!)
        }
        gateGraph[gate] = earlier to mutableSetOf()
    }
    for (gate in gates) {
        val left = predecessors[gate.inputs.first]
        if (left != null) {
            gateGraph[left]!!.second.add(gate)
        }
        val right = predecessors[gate.inputs.second]
        if (right != null) {
            gateGraph[right]!!.second.add(gate)
        }
    }
    val sortedGates = kahnsAlgorithm(gateGraph)
    for (gate in sortedGates) {
        wires[gate.output] = gate.evaluate(wires[gate.inputs.first]!! to wires[gate.inputs.second]!!)
    }
    val zs = mutableMapOf<Int, Boolean>()
    for (wire in wires.keys) {
        if (wire.startsWith('z')) {
            val num = wire.substring(1).toInt()
            zs[num] = wires[wire]!!
        }
    }
    var acc = 0L
    for (i in (zs.size - 1) downTo 0) {
        acc *= 2
        if (zs[i]!!) {
            acc++
        }
    }
    return acc
}

fun isValidFullAdder(index: Int, into: Map<Set<String>, Gate>, from: Map<String, Gate>): Pair<String, String>? {
    val indexString = index.toString().padStart(2, '0')
    val x = "x$indexString"
    val y = "y$indexString"
    val halfOut = into[setOf(x, y, "XOR")]!!.output // could be swapped
    val halfCarry = into[setOf(x, y, "AND")]!!.output // could be swapped
    val z = "z$indexString" // could be swapped
    val secondXor = from[z]!!
    val secondXorInputs = setOf(secondXor.inputs.first, secondXor.inputs.second)
    if (halfOut !in secondXorInputs) return null
    val carryIn = secondXorInputs.subtract(setOf(halfOut)).first()
    val fullCarry = into[secondXorInputs.union(setOf("AND"))]?.output ?: return null
    val carryOut = into[setOf(halfCarry, fullCarry, "OR")]?.output ?: return null
    return carryIn to carryOut
}

fun findSwapped(index: Int, into: Map<Set<String>, Gate>, from: Map<String, Gate>,
                cin: String, cout: String): Set<String> {
    val indexString = index.toString().padStart(2, '0')
    val x = "x$indexString"
    val y = "y$indexString"
    val z = "z$indexString" // could be swapped
    val firstXor = into[setOf(x, y, "XOR")]!! // could be swapped
    val firstAnd = into[setOf(x, y, "AND")]!! // could be swapped
    val outputGate = from[z]!!
    if (outputGate.operation == "OR") return setOf(z, cout)
    if (outputGate.operation == "XOR") return setOf(firstAnd.output, firstXor.output)
    val secondXor = into[setOf(firstXor.output, cin, "XOR")]!!
    return setOf(z, secondXor.output)
}

fun part2(input: List<String>): String {
    val (_, gateLines) = input
        .joinToString("\n")
        .split("\n\n")
        .map { it.split("\n") }
    val gateRegex = Regex("""^(...) (AND|OR|XOR) (...) -> (...)$""")
    val gates = gateLines.map {
        val match = gateRegex.matchEntire(it)!!
        Gate(match.groupValues[2], match.groupValues[1] to match.groupValues[3], match.groupValues[4])
    }
    val into = gates.associateBy { setOf(it.inputs.first, it.inputs.second, it.operation) }
    val from = gates.associateBy { it.output }
    val adders = (1..44).map {
        isValidFullAdder(it, into, from)
    }
    var wrongWires = setOf<String>()
    for (i in 0..43) {
        if (adders[i] != null) continue
        val cin = adders[i - 1]!!.second
        val cout = adders[i + 1]!!.first
        wrongWires = wrongWires.union(findSwapped(i + 1, into, from, cin, cout))
    }
    return wrongWires.sorted().joinToString(",")
}

fun main() {
    // Read a test input from the `src/day24/Day24_test.txt` file:
    val testInput = readInput("day24/Day24_test")
    check(part1(testInput) == 4L)
//    check(part2(testInput) == testInput.size)

    // Read the input from the `src/day24/Day24.txt` file:
    val input = readInput("day24/Day24")
    part1(input).println()
    part2(input).println()
}
