package dec1

private fun findValue(arr: List<Int>): Int {
        val mappedData = arr.map { it to it }.toMap()

        val value1 = arr.find { mappedData.containsKey(2020 - it) } ?: return -1

        println("value1: $value1")

        val value2 = 2020 - value1

        println("value2: $value2")

        return value1 * value2
    }

    fun main() {
        val result = findValue(Data.input)
        println("result: $result")
    }
