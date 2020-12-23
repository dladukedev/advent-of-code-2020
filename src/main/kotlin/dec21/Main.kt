package dec21

data class Recipe(
    val ingredients: List<String>,
    val allergens: List<Allergen>
)

enum class Allergen {
    DAIRY,
    FISH,
    SOY,
    NUTS,
    SESAME,
    PEANUTS,
    WHEAT,
    EGGS
}

fun parseInput(input: String): List<Recipe> {
    return input.lines()
        .map {
            val (ingredientsPart, allergensPart) = it.split(" (contains ")

            val ingredients = ingredientsPart.split(" ")
            val allergens = allergensPart.dropLast(1)
                .split(", ")
                .map { allergen ->
                    when(allergen) {
                        "dairy" -> Allergen.DAIRY
                        "soy" -> Allergen.SOY
                        "fish" -> Allergen.FISH
                        "nuts" -> Allergen.NUTS
                        "sesame" -> Allergen.SESAME
                        "peanuts" -> Allergen.PEANUTS
                        "wheat" -> Allergen.WHEAT
                        "eggs" -> Allergen.EGGS
                        else -> throw Exception("Unknown Allergen $allergen")
                    }
                }

            Recipe(ingredients, allergens)
        }
}

fun findFoodWithAllergenCandidates(allergen: Allergen, recipes: List<Recipe>): List<String> {
    val recipesWithIngredient = recipes.filter {
        it.allergens.contains(allergen)
    }

    return recipesWithIngredient.first().ingredients.filter { ingredient ->
        recipesWithIngredient.all {
            it.ingredients.contains(ingredient)
        }
    }
}

tailrec fun reduceCandidates(candidates: List<Pair<Allergen, List<String>>>): List<Pair<Allergen, String>> {
    if(candidates.all { it.second.count() == 1 }) {
        return candidates.map { it.first to it.second.single() }
    }

    val haveOne = candidates.filter { it.second.count() == 1 }

    val updatedCandidates = candidates.map { candidate ->
        candidate.first to candidate.second.filter { potentialMatch ->
            haveOne.all {
                candidate.first == it.first || !it.second.contains(potentialMatch)
            }
        }
    }

    return reduceCandidates(updatedCandidates)
}

fun findFoodAllergenPairs(recipes: List<Recipe>): List<Pair<Allergen, String>> {
    val dairyCandidates = findFoodWithAllergenCandidates(Allergen.DAIRY, recipes)
    val fishCandidates = findFoodWithAllergenCandidates(Allergen.FISH, recipes)
    val soyCandidates = findFoodWithAllergenCandidates(Allergen.SOY, recipes)
    val nutsCandidates = findFoodWithAllergenCandidates(Allergen.NUTS, recipes)
    val sesameCandidates = findFoodWithAllergenCandidates(Allergen.SESAME, recipes)
    val peanutsCandidates = findFoodWithAllergenCandidates(Allergen.PEANUTS, recipes)
    val wheatCandidates = findFoodWithAllergenCandidates(Allergen.WHEAT, recipes)
    val eggsCandidates = findFoodWithAllergenCandidates(Allergen.EGGS, recipes)

    return reduceCandidates(listOf(
        Allergen.DAIRY to dairyCandidates,
        Allergen.EGGS to eggsCandidates,
        Allergen.FISH to fishCandidates,
        Allergen.NUTS to nutsCandidates,
        Allergen.PEANUTS to peanutsCandidates,
        Allergen.SESAME to sesameCandidates,
        Allergen.SOY to soyCandidates,
        Allergen.WHEAT to wheatCandidates
    ))
}

fun main() {
    val recipes = parseInput(input)

    val allergens = findFoodAllergenPairs(recipes).map { it.second }

    val result = recipes.flatMap { it.ingredients }.filter { !allergens.contains(it) }.count()

    println("------------ PART 1 ------------")
    println("result: $result")

    println("------------ PART 2 ------------")
    println("result: " + allergens.joinToString(","))
}