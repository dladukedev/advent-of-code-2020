package dec16

data class ValidationInformation(
    val name: String,
    val ranges: List<IntRange>
)

data class TicketInformation(
    val validation: List<ValidationInformation>,
    val myTicket: List<Int>,
    val otherTickets: List<List<Int>>
)

fun parseValidation(input: String): List<ValidationInformation> {
    return input.lines()
        .map {
            val parts = it.split(": ")
            val name = parts.first()

            val ranges = parts[1].split(" or ")
                .map { range ->
                    val pair = range.split("-")
                        .map { num -> num.toInt() }
                    IntRange(pair.first(), pair.last())
                }

            ValidationInformation(name, ranges)

        }
}

fun parseTicket(input: String): List<Int> {
    return input.split(',').map { it.toInt() }
}

fun parseMyTicket(input: String): List<Int> {
    val ticketString = input.lines().last()
    return parseTicket(ticketString)
}

fun parseOtherTickets(input: String): List<List<Int>> {
    return input
        .lines()
        .drop(1)
        .map {
            parseTicket(it)
        }
}

fun parseInput(input: String): TicketInformation {
    val parts = input.split("\n\n")

    return TicketInformation(
        parseValidation(parts[0]),
        parseMyTicket(parts[1]),
        parseOtherTickets(parts[2])
    )
}

fun isValidTicketNumber(ticketNumber: Int, validation: ValidationInformation): Boolean {
    return validation.ranges.any { range -> range.contains(ticketNumber) }
}

fun getInvalidDigits(ticket: List<Int>, validations: List<ValidationInformation>): List<Int> {
    return ticket.filter {
        !validations.any { validation ->
            isValidTicketNumber(it, validation)
        }
    }
}

fun isValidTicket(ticket: List<Int>, validations: List<ValidationInformation>): Boolean {
    return ticket.all { ticketNumber ->
        validations.any {
            isValidTicketNumber(ticketNumber, it)
        }
    }
}

fun removeKnownPlacements(locations: List<List<String>>): List<String> {
    val singleLocations = locations.filter { it.count() == 1 }.flatten()

    val reducedLocations = locations.map { location ->
        if(location.count() == 1) {
            location
        } else {
            location.filter {
                !singleLocations.contains(it)
            }
        }
    }

    if(reducedLocations.all { it.count() == 1 }) {
        return reducedLocations.flatten()
    }

    return removeKnownPlacements(reducedLocations)
}

fun getTicketInputOrder(info: TicketInformation): List<String> {
    val validTickets = info.otherTickets.filter { isValidTicket(it, info.validation) }

    val names = validTickets.map {
        ticket -> ticket.map {
            info.validation.filter {
                validation -> isValidTicketNumber(it, validation)
            }.map { validation -> validation.name }
        }
    }

    val groupedByTicketNumberLocation = (0 until info.myTicket.count()).map {
        names.map { name -> name[it] }
    }

    val validLocations = groupedByTicketNumberLocation.map {
        info.validation.filter {validation ->
            it.all { x -> x.contains(validation.name) }
        }.map { it.name }
    }

    return removeKnownPlacements(validLocations)
}

fun productOfDepartureFieldValues(myTicket: List<Int>, locations: List<String>): Long {
    val departureIndexed = locations.mapIndexed { index, location ->
        if(location.contains("departure")) {
            index
        } else {
            -1
        }
    }.filter { it != -1 }

    return departureIndexed.fold(1L) { acc, i -> acc * myTicket[i] }
}

fun main() {
    val info = parseInput(input)

    println("------------ PART 1 ------------")
    val result = info.otherTickets
        .map { getInvalidDigits(it, info.validation) }
        .flatten()
        .sum()
    println("result: $result")

    println("------------ PART 2 ------------")

    val ticketOrder = getTicketInputOrder(info)
    val result2 = productOfDepartureFieldValues(info.myTicket, ticketOrder)
    println("result: $result2")
}