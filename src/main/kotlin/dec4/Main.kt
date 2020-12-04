package dec4

const val BIRTH_YEAR = "byr"
const val ISSUE_YEAR = "iyr"
const val EXPIRATION_YEAR = "eyr"
const val HEIGHT = "hgt"
const val HAIR_COLOR = "hcl"
const val EYE_COLOR = "ecl"
const val PASSPORT_ID = "pid"
const val COUNTRY_ID = "cid"

data class Passport(
    val birthYear: String?,
    val issueYear: String?,
    val expirationYear: String?,
    val height: String?,
    val hairColor: String?,
    val eyeColor: String?,
    val passportId: String?,
    val countryId: String?
)

private fun parseInput(input: String): Passport {
    val chunks = input
        .lines()
        .map {
            it.split(' ')
        }
        .flatten()
        .filter { it.isNotEmpty() }
        .map {
            it.split(':')
        }
        .map {
            if(it.count() < 2) println(it)
            it[0] to it[1]
        }

    var birthYear: String? = null
    var issueYear: String? = null
    var expirationYear: String? = null
    var height: String? = null
    var hairColor: String? = null
    var eyeColor: String? = null
    var passportId: String? = null
    var countryId: String? = null

    chunks.forEach {
        when(it.first) {
            BIRTH_YEAR -> birthYear = it.second
            ISSUE_YEAR -> issueYear = it.second
            EXPIRATION_YEAR -> expirationYear = it.second
            HEIGHT -> height = it.second
            HAIR_COLOR -> hairColor = it.second
            EYE_COLOR -> eyeColor = it.second
            PASSPORT_ID -> passportId = it.second
            COUNTRY_ID -> countryId = it.second
            else -> println("Unknown Key: ${it.first}")
        }
    }

    return Passport(
        birthYear,
        issueYear,
        expirationYear,
        height,
        hairColor,
        eyeColor,
        passportId,
        countryId
    )
}

fun isPassportValidTemporarily(passport: Passport): Boolean {
    return passport.birthYear != null
            && passport.issueYear != null
            && passport.expirationYear != null
            && passport.height != null
            && passport.hairColor != null
            && passport.eyeColor != null
            && passport.passportId != null
}

fun isBirthYearValid(passport: Passport): Boolean {
    val year = passport.birthYear?.toIntOrNull()

    return year != null && year in 1920..2002
}
fun isIssueYearValid(passport: Passport): Boolean {
    val year = passport.issueYear?.toIntOrNull()

    return year != null && year in 2010..2020
}
fun isExpirationYearValid(passport: Passport): Boolean {
    val year = passport.expirationYear?.toIntOrNull()

    return year != null && year in 2020..2030
}

enum class HeightUnits {
    IN,
    CM
}

fun isHeightValid(passport: Passport): Boolean {
    if(passport.height == null) {
        return false
    }

    val unit = when(passport.height.takeLast(2)) {
        "in" -> HeightUnits.IN
        "cm" -> HeightUnits.CM
        else -> null
    } ?: return false

    val size = passport.height.dropLast(2).toIntOrNull() ?: return false

    return when(unit) {
        HeightUnits.IN -> size in 59..76
        HeightUnits.CM -> size in 150..193
    }
}

fun isHairColorValid(passport: Passport): Boolean {
    return passport.hairColor != null && passport.hairColor.matches(Regex("#(a|b|c|d|e|f|\\d){6}"))
}

fun isEyeColorValid(passport: Passport): Boolean {
    return when(passport.eyeColor) {
        "amb", "blu", "brn", "gry", "grn", "hzl", "oth" -> true
        else -> false
    }
}

fun isPassportIdValid(passport: Passport): Boolean {
    return passport.passportId != null && passport.passportId.matches(Regex("\\d{9}"))
}

fun isPassportValid(passport: Passport): Boolean {
    return isBirthYearValid(passport)
            && isIssueYearValid(passport)
            && isExpirationYearValid(passport)
            && isHeightValid(passport)
            && isHairColorValid(passport)
            && isEyeColorValid(passport)
            && isPassportIdValid(passport)
}

fun main() {
    val passportRecords = input.map { parseInput(it) }
    println("------------ PART 1 ------------")
    val validPassportTemporaryCount = passportRecords.count { isPassportValidTemporarily(it)}
    println("result: $validPassportTemporaryCount")

    println()

    println("------------ PART 2 ------------")
    val validPassportCount = passportRecords.count { isPassportValid(it) }
    println("result: $validPassportCount")
}