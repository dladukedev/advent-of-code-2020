package dec7

data class Bag(
    val color: String,
    val count: Int
)

data class BagRule(
    val color: String,
    val bagsInside: List<Bag>
)

fun getBagRules(input: String): List<BagRule> {
    return input.lines()
        .filter { it.isNotEmpty() }
        .map {
            val color = it.split(" ").take(2).joinToString(" ")
            val bags = it.split(" contain ")
                .last()
                .split(", ")
                .filter { str -> str != "no other bags." }
                .map {str ->
                    val bagCount = str.split(" ").first().toInt()
                    val bagColor = str.split(" ").subList(1, 3).joinToString(" ")
                    Bag(bagColor, bagCount)
                }

            BagRule(
                color,
                bags
            )
        }
}

fun countBags(bagColor: String, bagRules: List<BagRule>): Int {
    val bagRule = bagRules.single { it.color == bagColor }
    if (bagRule.bagsInside.isEmpty()) {
        return 1
    }

    return 1 + bagRule.bagsInside.sumBy {
        it.count * countBags(it.color, bagRules)
    }
}

fun getBagCountInside(bagColor: String, bagRules: List<BagRule>): Int {
    return countBags(bagColor, bagRules) - 1
}

fun getBagsThatCanContain(bagColor: String, bagRules: List<BagRule>, accum: List<BagRule> = emptyList()): List<BagRule> {
    val bagsThatCanHoldColor = bagRules.filter { it.bagsInside.any { bag -> bag.color == bagColor} }
    val newColors = bagsThatCanHoldColor.filter { !accum.contains(it) }

    if(newColors.isEmpty()) {
        return accum
    }

    val newList = accum + newColors

    return newColors.map {
        getBagsThatCanContain(it.color, bagRules, newList)
    }.flatten()
        .distinctBy { it.color }
}

fun main() {
    val bagRules = getBagRules(input)
    println("------------ PART 1 ------------")
    val bags = getBagsThatCanContain("shiny gold", bagRules).count()
    println("result $bags") // 370

    println("------------ PART 2 ------------")
    val bagCount = getBagCountInside("shiny gold", bagRules)
    println("result $bagCount") // 25300
}
