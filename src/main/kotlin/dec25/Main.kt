package dec25

fun transformKey(subject: Long, key: Long): Long {
    val intermediateKey = key * subject
    return intermediateKey % 20201227
}

tailrec fun findLoopSize(subject: Long, target: Long, accumulator: Long = 1, loopCount: Int = 0): Int {
    if(accumulator == target) {
        return loopCount
    }

    val newAcc = transformKey(subject, accumulator)

    return findLoopSize(subject, target, newAcc, loopCount + 1)
}


fun getEncryptionKey(publicKey1: Long, publicKey2: Long, subject: Long): Long {
    val loopSize1 = findLoopSize(subject, publicKey1)
    val loopSize2 = findLoopSize(subject, publicKey2)

    val encryptionKey1 = (1..loopSize1).fold(1L) { acc, _ ->
        transformKey(publicKey2, acc)
    }

    val encryptionKey2 = (1..loopSize2).fold(1L) { acc, _ ->
        transformKey(publicKey1, acc)
    }

    if(encryptionKey1 != encryptionKey2) {
        throw Exception("Failed to decrypt keys, $encryptionKey1 != $encryptionKey2")
    }

    return encryptionKey1
}

fun main() {
    println("------------ PART 1 ------------")
    val result = getEncryptionKey(doorKeyInput, cardKeyInput, 7)
    println("result: $result")
}