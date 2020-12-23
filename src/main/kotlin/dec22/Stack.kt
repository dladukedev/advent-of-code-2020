package dec22

class Stack<T> private constructor(private val items: List<T>) {
    constructor(): this(emptyList())

    fun isEmpty() = items.isEmpty()

    fun peek(): T? {
        return items.first()
    }

    fun push(value: T): Stack<T> {
        return Stack(listOf(value) + items)
    }

    fun pop(): Pair<T, Stack<T>> {
        if(isEmpty()) {
            throw IllegalAccessException("No Elements in Stack")
        }

        val (item, remaining) = items.headTail()

        return item to Stack(remaining)
    }
}