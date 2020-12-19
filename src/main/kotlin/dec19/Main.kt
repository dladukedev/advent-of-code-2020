package dec19

sealed class Rule {
    data class Terminator(val character: String): Rule()
    data class Step(val paths: List<List<Int>>): Rule()
}

fun parseRule(input: String): Pair<Int, Rule> {
    val (ruleId, rule) = input.split(": ")

    return if(rule.contains("\"")) {
        ruleId.toInt() to Rule.Terminator(rule[1].toString())
    } else {
        val paths = rule.split(" | ")
            .map { path -> path.split(" ").map { value -> value.toInt() } }
        ruleId.toInt() to Rule.Step(paths)
    }
}

fun parseRules(input: String): HashMap<Int, Rule> {
    return input
        .lines()
        .map {
           parseRule(it)
        }.toMap() as HashMap
}

fun getValidResults(rules: HashMap<Int, Rule>, ruleId: Int = 0): List<String> {
    return when(val currentRule = rules[ruleId]) {
        is Rule.Step -> {
           return currentRule.paths.map {
               it.map{ pathRuleId ->
                    getValidResults(rules, pathRuleId)
                }.joinToString("")
            }

        }
        is Rule.Terminator -> {
            listOf(currentRule.character)
        }
        else -> throw Exception("Failure")
    }
}

fun buildRegex(rules: HashMap<Int, Rule>, ruleId: Int = 0): String {
    return when(val currentRule = rules[ruleId]) {
        is Rule.Step -> {
            when {
                currentRule.paths.count() == 1 -> {
                    currentRule.paths.first().joinToString("") { buildRegex(rules, it) }
                }
                currentRule.paths.count() > 1 -> {
                    "(${currentRule.paths.joinToString("|") { it.joinToString("") { rule -> buildRegex(rules, rule) } }})"
                }
                else -> {
                    ""
                }
            }
        }
        is Rule.Terminator -> {
            currentRule.character
        }
        else -> throw Exception("Failure")
    }
}

fun buildRegex2(rules: HashMap<Int, Rule>, ruleId: Int = 0): String {
    if(ruleId == 8) {
        val regex42 = buildRegex2(rules, 42)
        return "($regex42+)"
    }

    if(ruleId == 11) {
        val regex42 = buildRegex2(rules, 42)
        val regex31 = buildRegex2(rules, 31)
        val regex = (0..100).map {
            (0..it).joinToString("") { regex42 } + (0..it).joinToString("") { regex31 }
        }.joinToString("|") { "($it)" }

        return "($regex)"
    }

    return when(val currentRule = rules[ruleId]) {
        is Rule.Step -> {
            when {
                currentRule.paths.count() == 1 -> {
                    currentRule.paths.first().joinToString("") { buildRegex2(rules, it) }
                }
                currentRule.paths.count() > 1 -> {
                    "(${currentRule.paths.joinToString("|") { it.joinToString("") { rule -> buildRegex2(rules, rule) } }})"
                }
                else -> {
                    ""
                }
            }
        }
        is Rule.Terminator -> {
            currentRule.character
        }
        else -> throw Exception("Failure")
    }
}

fun main() {
    val parsed = parseRules(rulesInput)

    println("------------ PART 1 ------------")
    val regex = Regex(buildRegex(parsed))
    val result = input.lines().filter {
        it.matches(regex)
    }.count()

    println("result: $result")

    println("------------ PART 2 ------------")
    parsed.replace(8, parseRule("8: 42 | 42 8").second)
    parsed.replace(11, parseRule("11: 42 31 | 42 11 31").second)

    val regex2 = Regex(buildRegex2(parsed))
    val result2 = input.lines().filter {
        it.matches(regex2)
    }.count()


    println("result: $result2")

}