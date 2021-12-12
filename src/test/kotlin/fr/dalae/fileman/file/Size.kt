package fr.dalae.fileman.file

import kotlin.math.roundToLong

fun Int.MB(): Long {
    return this * 1024 * 1024L
}

fun Double.MB(): Long {
    return (this * 1024 * 1024).roundToLong()
}

fun Int.KB(): Long {
    return this * 1024L
}

fun Double.KB(): Long {
    return (this * 1024).roundToLong()
}

/**
 * A way of setting a file length by number of blocks of size HASH_BLOCK_SIZE
 */
fun Int.BLOCK(): Long {
    return this * HashUtils.HASH_BLOCK_SIZE.toLong()
}
