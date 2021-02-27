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
