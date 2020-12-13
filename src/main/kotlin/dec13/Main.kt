package dec13

import kotlin.math.ceil

fun getTimestamp(input: String): Int = input.lines().first().toInt()

fun getBusTimes(input: String): List<Int> = input
    .lines()
    .last()
    .split(",")
    .filter { it != "x" }
    .map { it.toInt() }

fun getQuickestBus(target: Int, buses: List<Int>): Int {
    return buses
        .map {
            val nearestTime = ceil(target.toDouble() / it) * it
            it to nearestTime
        }.minBy { it.second }!!
        .first
}

fun calculateResult(target: Int, bus: Int): Int {
    val nearestTime = ceil(target.toDouble() / bus) * bus
    val waitTime = nearestTime.toInt() - target
    return waitTime * bus
}

fun getBusSchedule(input: String): List<Pair<Int, Int>> {
    return input
        .lines()
        .last()
        .split(",")
        .mapIndexed { index, bus -> Pair(bus, index) }
        .filter { it.first != "x" }
        .map { Pair(it.first.toInt(), it.second) }
}

fun getSequentialTime(buses: List<Pair<Int, Int>>): Long {
    return buses.foldRight(Pair(0L, 1L)) { bus, acc ->
        val (time, step) = acc

        val newTime = (time..Long.MAX_VALUE step step)
            .first { ((it + bus.second) % bus.first == 0L)}

        val newStep = step*bus.first

        Pair(newTime, newStep)
    }.first
}

fun main() {
    println("------------ PART 1 ------------")
    val target = getTimestamp(input)
    val buses = getBusTimes(input)
    val quickestBus = getQuickestBus(target, buses)
    val result = calculateResult(target, quickestBus)
    println("result: $result")

    println("------------ PART 2 ------------")
    val busess = getBusSchedule(input)
    val result2 = getSequentialTime(busess)
    println("result: $result2")
}

