package dec22

fun <T> List<T>.headTail(): Pair<T, List<T>> {
    if (this.isEmpty()) {
        throw Exception("Invalid, Empty List")
    }
    val head = this.first()
    val tail = this.drop(1)

    return head to tail
}